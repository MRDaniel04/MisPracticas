package mars.pipeline.tomasulo;

import mars.pipeline.StageRegisters;
import mars.pipeline.BranchPredictor.*;
import mars.pipeline.Decode;
import mars.pipeline.DiagramaMulticiclo.InstructionInfo;
import mars.pipeline.DiagramaMulticiclo.InstructionInfo.Instruction_type;
import mars.pipeline.tomasulo.estaciones_alumnos.EstacionPF_alumnos;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;

/**
 * Implementación de Tomasulo para la práctica 2. Como suponemos que no hay
 * especulación dado que la predicción de saltos es ideal, la etapa Commit no se
 * utiliza (simplemente se limita a eliminar entradas del Reorder buffer)
 *
 * @author Francisco Andújar
 */
public class TomasuloIdeal_alumnos extends Tomasulo_alumnos {

    /**
     *
     * Crea la ruta de datos de Tomasulo para la práctica 2
     */
    public TomasuloIdeal_alumnos() {
        super(BranchPredictor_type.ideal);
        conEspeculacion = false;
    }

    /**
     * Devuelve el número de instrucciones especuladas correctamente hasta el
     * momento
     *
     * @return Número de instrucciones especuladas correctamente
     */
    @Override
    public int getNumInstruccionesConfirmadas() {
        return instrucciones;
    }

    /**
     * Realiza la etapa IF de la ruta de datos.
     *
     * @param next_if Registro ínter-etapa con la nueva instrucción leída de
     * memoria
     */
    @Override
    public void IF(StageRegisters next_if) {
        //copio el actual contenido a la etapa ID/Issue (ya habrá reservado la estación de trabajo anteriormente)
        StageRegisters if_reg = stage_regs[0];
        if_reg.copyTo(stage_regs[1]);
        //copio el enviado por el simulador a la actual etapa IF
        next_if.copyTo(stage_regs[0]);
        int instruction = if_reg.getInstruction();
        //cuenta de instrucciones y saltos

        InstructionInfo info = new InstructionInfo(instruction, cycle);

        if (Decode.isBranch(instruction)) {
            instrucciones++;
            instruccionesConfirmadas++;
            saltos++;
            //La predicción de saltos es ideal
            aciertos_predictor++;
            //Anotamos el resultado del salto y la predicción
            if_reg.setPrediccion(bp.getPrediction(instruction, if_reg.getAddress()));
            if_reg.setResultadoSalto(bp.is_branch_taken(instruction));
        } else {
            if_reg.setPrediccion(false);
            if_reg.setResultadoSalto(false);
            if (!no_mas_instrucciones) //resto de contadores
            {
                instrucciones++;
                instruccionesConfirmadas++;
                switch (info.getTipo()) {
                    case enteros:
                        enteros++;
                        break;
                    case pflotante:
                        pflotante++;
                        break;
                    case carga:
                        loads++;
                        break;
                    case almacenamiento:
                        stores++;
                        break;
                }
            }
        }
        /*Para visualización del diagrama multiciclo*/
        if (!no_mas_instrucciones) {
            diagrama.addInstruction(info);
            if_reg.setIns_info(info);
        }
    }

    /**
     * Realiza la etapa ISSUE. (A implementar)
     *
     * @return Devuelve false si la emisión se detiene debido a un riesgo
     * estructural (no hay estaciones de reserva o entradas en el Reorder
     * buffer). Si la instrucción se emite correctamente devuelve true
     */
    @Override
    protected boolean Issue() {
        /*Primer paso: obtenemos el tipo de instrucción*/
        StageRegisters registroIssue = stage_regs[1];
        //Esta  comprobación es para evitar emitir instrucciones nop
        if (stage_regs[1].getInstruction() == 0) {
            return true;
        }
        Instruction_type type = registroIssue.getIns_info().getTipo();

        /*Segundo paso: comprobar si hay una estación de reserva o buffer de memoria libre para esta instrucción
        y entradas en el rob.  Las estación que debemos consultar dependerán del tipo de instrucción.
        Si no se cumples las dos condiciones, terminamos la función devolviendo false*/

        /*PRACTICA: Dependiendo del tipo de la instruccion, debemos revisar si hay una estacion libre de ese tipo
        * y en caso de que asi sea, asignarla como la libre
        */
        Estacion estacion_libre = null;
        switch (type){
            case enteros:
                for(Estacion estacion : est_Enteros){
                    if(estacion.EstaLibre()){
                        estacion_libre = estacion;
                        break;
                    }
                }
                break;
            case pflotante:
                for (Estacion estacion : est_PF) {
                    if (estacion.EstaLibre()) {
                        estacion_libre = estacion;
                        break;
                    }
                }
                break;
            case carga:
                for (Estacion estacion : est_Load) {
                    if (estacion.EstaLibre()) {
                        estacion_libre = estacion;
                        break;
                    }
                }
                break;
            case almacenamiento:
                for (Estacion estacion : est_Store) {
                    if (estacion.EstaLibre()) {
                        estacion_libre = estacion;
                        break;
                    }
                }
                break;
            default:
                return false;
        }
        /*PRACTICA: si no hay libre salimos */
        if(estacion_libre == null){
            return false;
        }
        /*PRACTICA: Asegurarse de que hay una entrada libre en el Reorder Buffer */
        if (rob.EstaLleno()){
            return false;
        }
        /*Paso 3. Añadimos la instrucción al reorder buffer
        y anotamos el id de la entrada*/
        /*PRACTICA: Una vez ya hemos comprobado que tenemos los recursos disponibles */
        /*PRACTICA: Anadimos la instruccion al ROB */
        int rob_entry = rob.addEntry(registroIssue,cycle);

        /*Paso 4: Añadir la instrucción a la estación de reserva correspondiente.
         Para ello:
            -Si la instrucción tiene operando Rs y no es el registro $0:
                * Leemos el fichero de registros y comprobamos la etiqueta de Rt.
                    --> Si es cero, guardamos el valor en Vj
                    --> Si no, comprobamos si el resultado está en una entrada del rob
                    --> Si no, apuntamos el id del rob en Qj
            -Si la instrucción tiene operando Rt y no es el registro $0:
                * Leemos el fichero de registros y comprobamos la etiqueta de Rt.
                   --> Si es cero, guardamos el valor en Vk
                   --> Si no, comprobamos si el resultado está en una entrada del rob
                    --> Si no, apuntamos el id del rob en Qk
            - Si la instrucción tiene un operador inmediato (loads, store y bifuraciones),
              tambien debe añadirse
           -Por último, si la instrucción tiene un operando de destino,
            actualizamos el fichero de registros.
                * El operador de punto flotante siempre tiene como destino HI y LOW,
                  además de un posible tercer registro
        Para la realización de este paso, debemos implementar  las funciones
       ocupar() de cada subclase de Estación, además de checkQj() y checkQk()
        de la clase Estacion
         */
        
        estacion_libre.Ocupar(stage_regs[1].getInstruction(), rob_entry, cycle);
        RobEntry robEntry = rob.getRoB_entry(rob_entry);
        if(robEntry == null){
            return false;
        }

        /*Indicamos a la entrada del Rob el número de la estación de reserva*/
        robEntry.setEstacion(estacion_libre.robID);
        /*Visualización: añadimos el ciclo en el que la instrucción realizó la etapa Issue.
        Tambien guardamos el resultado final de la operación
        Así evitamos tener que implementar todas las operaciones de MIPS)
         */
        registroIssue.getIns_info().setIssue(cycle);
        estacion_libre.ins_info = registroIssue.getIns_info();
        estacion_libre.resultado = registroIssue.getRdValue();

        return true;
    }

    /**
     * Ejecuta la etapa EX de todos los operadores (Ya implementado).
     */
    @Override
    protected void Execute() {
        /*Para cada tipo de operador:
            Comprobar si el operador está ocupado
            Si está libre, escoger la estación que contenga la instrucción más antigua
            cuyos operadores están listos

            Operados enteros*/
        int estacion = opEnteros.obtenerEstacion(cycle);
        if (estacion != ES_NULO) {
            if (est_Enteros[estacion].Execute(cycle)) {
                opEnteros.LiberarOperador();
            }

        }
        /*Operadores punto flotante*/
        estacion = opPF.obtenerEstacion(cycle);
        if (estacion != ES_NULO) {
            if (est_PF[estacion].Execute(cycle)) {
                opPF.LiberarOperador();
            }

        }
        /*Operadores de carga*/
        estacion = opLoad.obtenerEstacion(cycle);
        if (estacion != ES_NULO) {
            if (est_Load[estacion].Execute(cycle)) {
                opLoad.LiberarOperador();
            }

        }
        /*Operadores de Almacenamiento*/
        estacion = opStore.obtenerEstacion(cycle);
        if (estacion != ES_NULO) {
            if (est_Store[estacion].Execute(cycle)) {
                opStore.LiberarOperador();
                //Como no tenemos etapa Commit (todavía), liberamos la estación de reserva
                //y la entrada en el rob de la operación de almacenamiento
                int rob_id = est_Store[estacion].robID;
                est_Store[estacion].Libera();
                rob.getRoB_entry(rob_id).WBdone();
            }

        }

    }

    /**
     * Ejecuta la etapa Writeback, escribiendo el resultado en el CDB y
     * actualizando estaciones de reserva y el Reorder buffer (a implementar).
     */
    protected void WB() {
        Estacion estacion = null;
        RobEntry entry = null;
        /*Primer paso: buscar la estación preparada para el writeback
        con la instrucción más antigua*/
        /*PRACTICA: Inicializacion de variables */
        int cicloMasAntiguo = Integer.MAX_VALUE;
        int id_rob = 0;
        int resultado = 0;
        int hi_value = 0;
        int lo_value = 0;
        /*PRACTICA: Iteramos sobre todas las estaciones, buscando la mas antigua */
        for(Estacion e : est_Todas){
            if(e.busy && e.ready_for_WB() && e.getMarca_tiempo()<cicloMasAntiguo){
                cicloMasAntiguo = e.getMarca_tiempo();
                estacion = e;
            }
        }
        /*Segundo paso: actualizar el CDB. Si no hemos obtenido ninguna estación,
        escribimos una marca nula en el rob y terminamos la ejecución de la etapa.
        En caso contrario, actualizamos el CDB con la entrada del rob
        y el resultado de la estación de reserva
         */
        
        /*PRACTICA: Leemos el CDB */
        CDB cdb = CDB.read();

        /*PRACTICA: Marca nula en CDB si no hemos encontrado estacion para escribir */
        if (estacion == null){
            cdb.update(ES_NULO,0);
        }
        /*PRACTICA: Actualizamos CDB si encontramos estacion */
        else{
            /*PRACTICA: Obtenemos los datos del registro */
            id_rob = estacion.getRob_entry();
            entry = rob.getRoB_entry(id_rob);
            resultado = estacion.getResultado();
            if(estacion instanceof EstacionPF_alumnos){
                hi_value = ((EstacionPF_alumnos)estacion).getHI();
                lo_value = ((EstacionPF_alumnos)estacion).getLOW();
            }
            /*PRACTICA: Escribimos en CDB */
            cdb.update(id_rob, resultado);

            /* Paso 3. Recorremos las estaciones de reserva comprobando
        los operandos de aquellas estaciones que están ocupadas.
        si alguna  necesita el valor escrito en el CDB,
        actualizamos los valores de Vj y Qj (o Vk y Qk)*/
        /*PRACTICA: Actualizamos el dato en aquellas estaciones que estuvieran esperandolo */
        for(Estacion e : est_Todas){
            if(e.busy){
                if(e.Qj == id_rob){
                    e.Vj = resultado;
                    e.Qj = ES_NULO;
                }
                if(e.Qk == id_rob){
                    e.Vk = resultado;
                    e.Qk = ES_NULO;
                }
            }
        }
        /*Paso 4: Actualizamos el contenido del Reorder buffer
            Indicamos que la estación ya ha realizado el writeback.
            También habría que actualizar el resultado de la operación,
            aunque no es necesario ya que por la implementación del simulador
            lo conocemos desde el inicio.

            Caso especial (WB de instrucción de Punto flotante).
            Hay que pasar el contenido de los registros HI y LOW explícitamente
            al reorder buffer
         */
        /*PRACTICA: Actualizamos el contenido del reorder buffer */
        RobEntry robEntry = rob.getRoB_entry(id_rob);
        robEntry.setResultado(resultado);
        robEntry.WBdone();
        if(estacion instanceof EstacionPF_alumnos){
            robEntry.setHI(hi_value);
            robEntry.setLOW(lo_value);
        }
        /*Para la visualización en los ficheros html*/
        entry.setStage("WB");
        entry.getIns_info().setWB(cycle);
        /*Paso 5: liberamos la estación de reserva*/
        estacion.Libera();
        }
        
    }

    /**
     * Ejecuta la etapa Commit. Se implementará correctamente en la práctica 3.
     * Ahora se limita a eliminar las entradas en el RoB que han realizado la
     * etapa WB.
     */
    @Override
    protected void Commit() {
        rob.LimpiaRoB(cycle);
    }

    /**
     * Devuelve el ciclo actual
     *
     * @return ciclo actual
     */
    @Override
    public int getCycle() {
        return cycle - 1;
    }

    /**
     * Esta función avanza un ciclo la ejecución del pipeline.
     *
     * @return Si la lectura de instrucciones se detiene debido a la detención
     * de la etapa Issue, devuelve false. Si se puede leer una nueva
     * instrucción, devuelve true.
     */
    @Override
    protected boolean UpdatePipeline() {
        //incremento el ciclo de reloj
        Commit();
        WB();
        Execute();
        //ejecuto las etapas
        boolean retorno = Issue();
        writeStatus();
        cycle++;
        return retorno;
    }

    /**
     * Finaliza la ejecución del pipeline cuando ya no quedan instrucciones a
     * leer de la memoria de instrucciones.
     */
    @Override
    public void finalizar() {
        /*Finalizar el programa*/
 /*1 Paso a la etapa IF un stageRegister con un nop*/
        no_mas_instrucciones = true;
        StageRegisters st = new StageRegisters();
        IF(st);//Con esto tengo un nop en IF y el syscall en ID
        boolean retorno = false;
        while (!retorno) {
            //incremento el ciclo de reloj
            Commit();
            WB();
            Execute();
            //ejecuto las etapas
            retorno = Issue();
            writeStatus();
            cycle++;
        }
        IF(st);//Ahora tengo un nop tanto en IF como ID
        //actualizo el pipeline mientras haya entradas en el rob
        while (rob.getEntradasOcupadas() != 0) {
            //incremento el ciclo de reloj
            Commit();
            WB();
            Execute();
            writeStatus();
            cycle++;
        }
    }

    @Override
    public String toString() {
        return "Tomasulo sin especulación (P2-alumnos). Predicción ideal de saltos\n" + super.toString();
    }
}
