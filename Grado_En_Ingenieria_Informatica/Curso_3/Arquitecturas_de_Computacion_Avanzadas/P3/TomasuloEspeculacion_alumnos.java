package mars.pipeline.tomasulo;

import mars.pipeline.StageRegisters;
import java.util.logging.Level;
import java.util.logging.Logger;
import mars.mips.hardware.AddressErrorException;
import mars.mips.hardware.Memory;
import mars.pipeline.Decode;
import mars.pipeline.BranchPredictor.*;
import static mars.pipeline.DiagramaMulticiclo.HtmlUtils.*;
import mars.pipeline.DiagramaMulticiclo.InstructionInfo;
import mars.pipeline.DiagramaMulticiclo.InstructionInfo.Instruction_type;
import mars.pipeline.predictors.DynamicPredictor;
import mars.pipeline.tomasulo.estaciones_alumnos.EstacionPF_alumnos;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;
import mars.pipeline.tomasulo.estaciones_alumnos.EstacionSW_alumnos;

/**
 * Implementación de Tomasulo para la práctica 3.
 *
 * @author Francisco Andújar
 */
public class TomasuloEspeculacion_alumnos extends Tomasulo_alumnos {

    private boolean emitiendoIncorrectas = false;
    private int ultimaInstucciónIncorrecta;

    /**
     *
     * Crea la ruta de datos de Tomasulo para la práctica 3
     *
     * @param bp Tipo de predictor
     */
    public TomasuloEspeculacion_alumnos(BranchPredictor_type bp) {
        super(bp);
        conEspeculacion = true;
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
            saltos++;
            //Anotamos el resultado del salto y la predicción
            boolean prediccion = bp.getPrediction(instruction, if_reg.getAddress());
            boolean resultado = bp.is_branch_taken(instruction);
            if_reg.setPrediccion(prediccion);
            if_reg.setResultadoSalto(resultado);
            if (prediccion != resultado) {
                //mala predicción. Se emitirán instrucciones incorrectas
                this.emitiendoIncorrectas = true;
                this.ultimaInstucciónIncorrecta = (prediccion)
                        ? if_reg.branch_address() : if_reg.getAddress() + 4;
            }
        } else {
            if_reg.setPrediccion(false);
            if_reg.setResultadoSalto(false);
            if (!no_mas_instrucciones) //resto de contadores
            {
                instrucciones++;
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

        /*PRACTICA: indice para guardar que estación de reserva procesa la instrucción */
        int id = 0;
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
                    id++;
                }
                break;
            case pflotante:
                for (Estacion estacion : est_PF) {
                    if (estacion.EstaLibre()) {
                        estacion_libre = estacion;
                        break;
                    }
                    id++;
                }
                break;
            case carga:
                for (Estacion estacion : est_Load) {
                    if (estacion.EstaLibre()) {
                        estacion_libre = estacion;
                        break;
                    }
                    id++;
                }
                break;
            case almacenamiento:
                for (Estacion estacion : est_Store) {
                    if (estacion.EstaLibre()) {
                        estacion_libre = estacion;
                        break;
                    }
                    id++;  
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
        robEntry.setEstacion(id);
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
                //Liberamos la estación de reserva
                est_Store[estacion].Libera();

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
     * Ejecuta la etapa Commit. (A implementar)
     */
    @Override
    protected void Commit() {
        //obtenemos la primera entrada del rob
        RobEntry head = rob.getHead();

        /*Comprobamos que la estación está ocupada y
	    instrucción ha ejecutado la etapa writeback.
          Si no, terminamos*/

        /*PRACTICA: comprobamos head no ulo, que no este ocupada y que la etapa WB esta hecha */
        if (head == null || !head.EstaOcupada() || !head.isWBdone()){
            return;
        }
        Instruction_type type = head.getIns_info().getTipo();
        instruccionesConfirmadas++;

        /*Paso 1: Comprobamos el tipo de instrucción.
        Si es un salto hay que comprobar el resultado de la predicción,
        si es un almacenamiento hay que confirmar la escritura
        Si es otro tipo de operación, comprobamos si tiene registro destino y en
        ese caso, actualizamos los registros
         */
        if (head.isBranch()) {
            /*Paso 2. Se ha comprobado que es un salto.
 	Se debe comprobar si la predicción y el resultado
	del salto es el mismo*/
            if (head.resultadoPrediccion() != head.resultadoSalto()) {
                fallos_predictor++;
                /*Paso 2.1: se ha fallado la predicción. Hay que eliminar
                el trabajo realizado especulativamente. Comenzamos liberando
		los registros*/
                //Atención: en este punto: mantener esta instrucción
                emitiendoIncorrectas = false;
                /*PRACTICA: Bucle por todos los registros */
                for(int i=0; i < registro.NUM_REGISTROS; i++){
                    registro reg = registro.getReg(i);
                    if(reg != null){
                        reg.Libera();
                    }
                }
                /*Paso 2.2 Liberar estaciones de reserva.
                Con la excepción de las estaciones de almacenamiento.
                Debemos comprobar si la escritura a memoria está confirmada o no.
                En ese caso, no liberamos la estación de reserva para que
		se termine de realizar la escritura
                 */
                /*PRACTICA: Bucle por todas las estaciones. En caso de que SW este confirmada NO LIBERAMOS */
                for (Estacion estacion : est_Todas){
                    if(estacion instanceof EstacionSW_alumnos){
                        EstacionSW_alumnos estSW = (EstacionSW_alumnos) estacion;
                        if(!estSW.EstaConfirmada()){
                            estacion.Libera();
                        }
                    }
                    else{
                        estacion.Libera();
                    }
                }
                /*Paso 2.3: Liberamos los operadores. De nuevo,
                el operador de escritura no liberamos para que pueda completar
                su ejecución*/
                /*PRACTICA: Liberamos opEnteros, opPF y opLoad. opSW no para finalizar escrituras */
                opEnteros.LiberarOperador();
                opPF.LiberarOperador();
                opLoad.LiberarOperador();
                /*Paso 2.4 Habría que actualizar el PC. En nuestro caso no es necesario
                Pero debemos actualizar los registros ínter-etapa.
		Basta con poner un nop (código máquina 0) en los registros*/
                /*Información de visualización. Incluir*/
                stage_regs[0].getIns_info().setDiscard(cycle);
                stage_regs[1].getIns_info().setDiscard(cycle);
                /*PRACTICA: En tomasulo.java establecemos que stage_regs.length = 2, dando a entender
                 * (viene explicado en el comentario de la inicialización de state_regs) que solo hay
                 * registro inter-etapa para la etapa IF e ID
                 */
                stage_regs[0].setInstruction(0);
                stage_regs[1].setInstruction(0);
                /*Paso 2.6 Liberamos todas las entradas del reorder buffer
		y actualizamos el número de fallos. 
                 */
                /*PRACTICA: limpiamos ROB */
                rob.clean(cycle);
            } else {
                /*Paso 2.7. Si se acertó la predicción, se actualiza el número de aciertos */
                aciertos_predictor++;
            }
            //Si se está usando un predictor dinámico, se actualiza
            if (bp instanceof DynamicPredictor) {
                DynamicPredictor dp = (DynamicPredictor) bp;
                dp.writeDynPredictor(cycle);
                dp.Actualizar(head.getAddress(), head.getInstruction(), head.resultadoSalto());

            }
        }
        /**
         * Paso 3: si es un almacenamiento, se obtiene el número de estación del
         * ROB y confirmamos la operación de escritura
         */
        /*PRACTICA: Obtenemos el numero de estacion y asegurandonos que es de SW, confirmamos
         * con la funcion Confirma() la escritura.
         */
        else if(type == Instruction_type.almacenamiento){
            Estacion estacionSW = est_Store[head.getEstacion()];
            if(estacionSW instanceof EstacionSW_alumnos){
                ((EstacionSW_alumnos)estacionSW).Confirma();
            }
        }
        /*Paso 4: Comprobamos si la instrucción escribe en algún registro
		Si el destino es distinto de cero, actualizamos el registro
		Si la marca del registro es la entrada de rob actual, liberamos el registro
         */
        /*PRACTICA: en caso de que no sea de SW, comprobamos si existe registro destino */
        else{
            int dest = head.getDestino();   //PRACTICA: obtenemos registro destino (si lo hay)
            if (dest != 0){ //PRACTICA: distinto de 0
                registro regDest = registro.getReg(dest);   //PRACTICA: actualizar reg
                if(regDest != null){
                    regDest.setValor(head.getResultado()); //PRACTICA: marca registro = entrada rob
                    if(regDest.getRob() == rob.getHeadID()){
                        regDest.Libera();   //PRACTICA: liberar
                    }
                }
            }
            /*Paso 4.1: Caso especial. Comprobamos si la instrucción
            es una división o multiplicación (punto flotante)*/
            /*PRACTICA: con las instrucciones de punto flotante siempre hay que revisar
             * registros HI y LOW
             */
            if(type == Instruction_type.pflotante){
                //PRACTICA: logica de HI
                registro regHI = registro.getReg(registro.HI);
                regHI.setValor(head.getHI());
                if(regHI.getRob() == rob.getHeadID()){
                    regHI.Libera();
                }
                //PRACTICA: logica de LOW
                registro regLO = registro.getReg(registro.LOW);
                regLO.setValor(head.getLOW());
                if(regLO.getRob() == rob.getHeadID()){
                    regLO.Libera();
                }
            }
        }
        
        /*Paso 5 Liberamos la entrada del rob que ha hecho el Commit*/
        /*PRACTICA: simplemente quitamos la entrada del rob asociada al ciclo actual */
        rob.removeHead(cycle);
        /*Visualización en ficheros */
        head.setStage("CO");
        head.getIns_info().setCommit(cycle);
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
        boolean retornoIssue = Issue();
        writeStatus();
        cycle++;
        //comprobar si se están emitiendo instrucciones incorrectas.
        if (this.emitiendoIncorrectas) {
            //En ese caso, si la etapa Issue ha completado la emisión,
            //lanzamos una instrucción incorrecta. En caso contrario,
            //no emitimos porque se debe completar la última emisión
            if (retornoIssue) {
                //En este caso, emitimos la última instrucción
                //y devolvemos false.
                StageRegisters rIF = new StageRegisters();
                rIF.setAddress(this.ultimaInstucciónIncorrecta);
                Memory mem = Memory.getInstance();
                int instruction = 0;
                try {
                    instruction = mem.getWordNoNotify(ultimaInstucciónIncorrecta);
                    rIF.setInstruction(instruction);
                } catch (AddressErrorException ex) {
                    Logger.getLogger(TomasuloEspeculacion_alumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
                rIF.setIns_info(new InstructionInfo(rIF.getInstruction(), cycle));
                //actualizamos la nueva instrucción a emitir
                this.ultimaInstucciónIncorrecta += 4;
                IF(rIF);
                return false;
            }

        }

        return retornoIssue;
    }

    private boolean check_estacionSW() {
        for (Estacion estacion : est_Store) {
            if (estacion.busy) {
                return true;
            }
        }
        return false;
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
        while (rob.getEntradasOcupadas() != 0 || check_estacionSW()) {
            //incremento el ciclo de reloj
            Commit();
            WB();
            Execute();
            writeStatus();
            cycle++;
        }
        if (bp instanceof DynamicPredictor) {
            DynamicPredictor dp = (DynamicPredictor) bp;
            dp.writeDynPredictor(cycle - 1);
            dp.writeDynPredictor(cycle - 1);
        }
    }

    @Override
    public String toString() {
        return "Tomasulo con especulación (P3-alumnos). Predictor: " + this.bp + "\n" + super.toString();
    }

    @Override
    public void writeStatus() {
        super.writeStatus();
        for (Estacion e : est_Store) {
            ((EstacionSW_alumnos) e).LiberarSiPendiente();
        }
    }

    /**
     * Genera una cadena con el código HTML que permite navegar por los ficheros
     * HTML de profilling
     *
     * @param cycle
     * @return
     */
    protected String NavegacionEstado(int cycle) {
        String str = getRef("Resumen", "Resumen.html");
        str += getRef("Diagrama Multiciclo", "diag" + cycle + ".html");
        if (bp instanceof DynamicPredictor) {
            DynamicPredictor dp = (DynamicPredictor) bp;
            String pname = this.getPreditor_type().name();
            int btb_cycle = dp.getUltimaActualizacion();
            str += getRef(pname + " (ultima act.)", pname + btb_cycle + ".html");
            str += "<br>\n";
        }
        str += getRef("Ciclo 1", "cycle1.html");
        if (cycle > 1) {
            str += getRef("[-1]", "cycle" + (cycle - 1) + ".html");
        } else {
            str += "<a>[-1]</a>&nbsp;&nbsp;";
        }
        if (cycle > 6) {
            str += getRef("[-5]", "cycle" + (cycle - 5) + ".html");
        } else {
            str += "<a>[-5]</a>&nbsp;&nbsp;";
        }
        str += "<a>" + negrita("Ciclo " + cycle) + "</a>&nbsp;&nbsp;";
        if (cycle + 1 <= limite) {
            str += getRef("[+1]", "cycle" + (cycle + 1) + ".html");
        } else {
            str += "<a>[+1]</a>&nbsp;&nbsp;";
        }
        if (cycle + 5 <= limite) {
            str += getRef("[+5]", "cycle" + (cycle + 5) + ".html");
        } else {
            str += "<a>[+5]</a>&nbsp;&nbsp;";
        }
        str += "<br>";
        return str;
    }
}
