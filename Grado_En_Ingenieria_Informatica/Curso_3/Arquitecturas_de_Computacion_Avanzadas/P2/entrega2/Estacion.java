package mars.pipeline.tomasulo;

import mars.pipeline.DiagramaMulticiclo.InstructionInfo;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;

/**
 * Clase abstracta que representa una estación de reserva genérica
 *
 * @author Francisco J. Andújar
 */
public abstract class Estacion {

    /**
     * Latencia del operador de la estación de reserva.
     */
    protected final int latencia;

    /**
     * Código máquina de la instrucción en la estación de reserva.
     */
    protected int instruction;

    /**
     * Identificador de la entrada del RoB asociada a la instrucción.
     */
    protected int robID;

    /**
     * Entrada en el RoB que generará el primer operando. Es igual a ES_NULO si
     * ya tenemos el operando.
     */
    protected int Qj;

    /**
     * Valor del primer operando.
     */
    protected int Vj;

    /**
     * Entrada en el Rob que generará el segundo operando. Es igual a ES_NULO si
     * ya tenemos el operando.
     */
    protected int Qk;

    /**
     * Valor del segundo operando.
     */
    protected int Vk;

    /**
     * Resultado de la operación. Se conoce desde el principio para no tener que
     * implementar la ejecución de las instrucciones en la etapa EX.
     */
    protected int resultado;

    /**
     * Estado de ejecución de la instrucción en la estación de reserva. Cuando
     * estado=latencia, la ejecución ha terminado.
     */
    protected int estado;

    /**
     * Flag que indica si la estación está ocupada o no.
     */
    protected boolean busy;

    /**
     * Flag que indica si la operación se ha confirmado o no (sólo tiene uso en
     * la estaciones SW).
     */
    protected boolean confirmado;

    /**
     * Marca de tiempo que indica el ciclo en el que la instrucción se guardó en
     * la estación de reserva.
     */
    protected int marca_tiempo;

    /**
     * Desplazamiento con extensión de signo en instrucciones de carga y
     * almacenamiento
     */
    protected int desp;

    /**
     * Dirección de acceso a memoria (carga y almacenamiento).
     */
    protected int dir;
    /**
     * para visualización del diagrama multiciclo
     */
    protected InstructionInfo ins_info;

    /**
     * Crea un estación de reserva con la latencia indicada.
     *
     * @param ciclos ciclos de latencia del operador
     */
    protected Estacion(int ciclos) {
        this.latencia = ciclos;
        busy = false;
    }

    /**
     * Devuelve el ciclo en el que la instrucción se guardó en la estación de
     * reserva
     *
     * @return ciclo de guardado
     */
    public int getMarca_tiempo() {
        return marca_tiempo;
    }

    /**
     * Modifica la marca de tiempo de la estación de reserva.
     *
     * @param marca_tiempo Nuevo ciclo de guardado
     */
    public void setMarca_tiempo(int marca_tiempo) {
        this.marca_tiempo = marca_tiempo;
    }

    /**
     * Devuelve el identificador de la entrada del RoB asociada a esta estación
     * de reserva
     *
     * @return Identificador de entrada del Rob o ES_NULO si la estación está
     * libre
     */
    public int getRob_entry() {
        return robID;
    }

    /**
     * Devuelve la marca del primer operando
     *
     * @return Identificador de entrada del RoB o ES_NULO si el operando ya se
     * ha leído
     */
    public int getQj() {
        return Qj;
    }

    /**
     * Devuelve el valor del primer operando
     *
     * @return Valor de primer operando
     */
    public int getVj() {
        return Vj;
    }

    /**
     * Devuelve la marca del segundo operando
     *
     * @return Identificador de entrada del RoB o ES_NULO si el operando ya se
     * ha leído
     */
    public int getQk() {
        return Qk;
    }

    /**
     ** Devuelve el valor del segundo operando
     *
     * @return Valor del segundo operando
     */
    public int getVk() {
        return Vk;
    }

    /**
     * Devuelve el resultado de la ejecución de la instrucción
     *
     * @return Resultado de la operación
     */
    public int getResultado() {
        return resultado;
    }

    /**
     * Devuelve el estado de la operación en la estación de reserva
     *
     * @return Ciclo de ejecución en el que se encuentra la actual instrucción
     */
    public int getEstado() {
        return estado;
    }

    /**
     * Consulta si la estación está libre u ocupada
     *
     * @return true si estación libre, false es casoContrario
     */
    public boolean EstaLibre() {
        return !busy;
    }

    /**
     * Determina si la operación de almacenamiento está confirmada. Solo se
     * utiliza en las estaciones de almacenamiento
     *
     * @return true: instrucción confirmada. false: instrucción no confirmada
     */
    public boolean EstaConfirmada() {
        return confirmado;
    }

    /**
     * Confirma la operación de almacenamiento. Solo se utiliza en las
     * estaciones de almacenamiento.
     */
    public void Confirma() {
        confirmado = true;
    }

    /**
     * Libera un estación de reserva
     */
    public void Libera() {
        busy = false;
        Qk = Qj = ES_NULO;
        estado = 0;
    }

    /**
     * Determina si la estación está lista para ejecutar la etapa Writeback
     *
     * @return true: lista para escribir. false: no hay dato a escribir
     */
    public boolean ready_for_WB() {
        return (estado == latencia);
    }

    /**
     * Devuelve información de la instrucción utilizada para la visualización en
     * los ficheros .html
     *
     * @return Información de visualización de la instrucción asociada a la
     * entrada del Rob
     */
    public InstructionInfo getIns_info() {
        return ins_info;
    }

    /**
     * La estación lee el CDB y actualiza sus operandos, si así lo requiere.
     */
    public void leeCDB() {
        int rob = CDB.read().getRob();
        //compruebo primer operando
        if (rob != ES_NULO) {
            if (Qj == rob) {
                Qj = ES_NULO;
                Vj = CDB.read().getValor();
            }
            //compruebo segundo operando
            if (Qk == rob) {
                Qk = ES_NULO;
                Vk = CDB.read().getValor();
            }
        }

    }

    /*
	Métodos a implementar por el alumno
     */
    /**
     * Comprueba el estado del registro Rs, actualizado Qj y Vj en consecuencia
     *
     * @param Rs Identificador del operando Rs
     */
    public void checkQj(int Rs) {
        if (Rs!=0){
            registro estado_rs = registro.getReg(Rs);

            int Qi = estado_rs.getRob();

            if(Qi == ES_NULO){
                this.Qj = ES_NULO;
                this.Vj = estado_rs.getValor();
            }
            else{
                ReorderBuffer rob = ReorderBuffer.getRoBInstance();
                RobEntry entrada_rob = rob.getRoB_entry(Qi);
                if(entrada_rob.isWBdone()){
                    Vj = entrada_rob.getResultado();
                    Qj = ES_NULO;
                }
                else{
                    Qj  = Qi;
                    Vj = 0;
                }
            }
        }
    }

    /**
     * Comprueba el estado del registro Rt, actualizado Qk y Vk en consecuencia
     *
     * @param Rt Identificador del operando Rt
     */
    public void checkQk(int Rt) {
        if(Rt!=0){
            registro estado_rt = registro.getReg(Rt);

            int Qi = estado_rt.getRob();

            if(Qi == ES_NULO){
                this.Qk = ES_NULO;
                this.Vk = estado_rt.getValor();
            }
            else{
                ReorderBuffer rob = ReorderBuffer.getRoBInstance();
                RobEntry entrada_rob = rob.getRoB_entry(Qi);
                if(entrada_rob.isWBdone()){
                    Vk = entrada_rob.getResultado();
                    Qk = ES_NULO;
                }
                else{
                    Qk  = Qi;
                    Vk = 0;
                }
            }
        }
    }

    /**
     *
     * Ejecuta un ciclo de la instrucción guardada la estación de reserva.
     *
     * @param cycle Ciclo actual de ejecución.
     * @return true si la operación ha terminado. false en otro caso
     */
    public abstract boolean Execute(int cycle);

    /**
     * Envía una instrucción a una estación de reserva.
     *
     * @param instruction Instrucción a guardar en la estación de reserva
     * @param rob_entry_id Identificador de la entrada del RoB asociada a la
     * instrucción
     * @param cycle Ciclo actual de ejecución.
     * @return true si la instrucción se ha podido guardar en la estación. false
     * en otro caso
     */
    public abstract boolean Ocupar(int instruction, int rob_entry_id, int cycle);

    /**
     * Genera un string para la impresión por pantalla en formato html del la
     * entrada del Reorder buffer
     *
     * @return String con la información relevante de la entrada del rob
     */
    public abstract String toHtmlTable();
}
