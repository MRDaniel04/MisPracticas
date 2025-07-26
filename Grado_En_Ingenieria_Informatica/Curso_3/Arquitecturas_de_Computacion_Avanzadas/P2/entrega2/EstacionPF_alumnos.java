package mars.pipeline.tomasulo.estaciones_alumnos;

import java.util.logging.Level;
import java.util.logging.Logger;
import mars.ProcessingException;
import mars.mips.instructions.BasicInstruction;
import mars.pipeline.Decode;
import static mars.pipeline.DiagramaMulticiclo.HtmlUtils.getCell;
import mars.pipeline.tomasulo.*;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;

/**
 * Estación de punto flotante
 *
 * @author Francisco Andújar
 */
public class EstacionPF_alumnos extends Estacion {

    private int HI;
    private int LOW;

    /**
     * Crea una estación de enteros
     */
    public EstacionPF_alumnos() {
        super(LAT_PF);
    }

    /**
     * Envía a una estación de reserva una operaciones de multiplicación y
     * división. En este caso, todas las instrucciones usan los registros Rs y
     * Rt. Además, las multiplicaciones puede escribir en un registro Rd.
     *
     * Todas las operaciones escriben en HI y LOW y se deben marcar siempre al
     * actualizar el fichero de registros.
     *
     * Los IDs de HI y LOW son 32 y 33, aunque existen constantes en la clase
     * Tomasulo para acceder a ellos.
     *
     * @param instruction Instrucción a guardar en la estación de reserva.
     * @param rob_entry_id Identificador de la entrada del RoB asociada a la
     * instrucción.
     * @param cycle Ciclo actual de ejecución.
     * @return true si la instrucción se ha podido guardar en la estación. false
     * en otro caso.
     */
    @Override
    public boolean Ocupar(int instruction, int rob_entry_id, int cycle) {
        if (busy) {
            return false;
        }
        this.instruction = instruction;
        busy = true;
        //actualizo marca de tiempo
        marca_tiempo = cycle;
        estado = 0;
        //anoto la entrada de la estación
        this.robID = rob_entry_id;
        /*A rellenar por el alumno: obtención de operandos y 
	actualización de marcas en el banco de registros (cuando corresponda).
	Nota: además del posible registro destino, marcar siempre registro HI y LOW*/

        /*PRACTICA: Inicializamos valor de operandos */
        Qj = ES_NULO;
        Vj = 0;
        Qk = ES_NULO;
        Vk = 0;

        /*PRACTICA: obtenemos rs */
        int rs = Decode.getRs(instruction);
        checkQj(rs);

        /*PRACTICA: obetenemos rt */
        int rt = Decode.getRt(instruction);
        checkQk(rt);

        /*PRACTICA: Registro destino en caso de que exista */
        int dest = Decode.getDestination(instruction);
        if(dest!=0){
            registro registro_dest = registro.getReg(dest);
            if(registro_dest == null){
                busy = false;
                return false;
            }
            registro_dest.marcaRob(rob_entry_id);
        }

        /*PRACTICA: Marcar registros HI y LOW */
        registro registro_HI = registro.getReg(registro.HI);
        if(registro_HI != null){
            registro_HI.marcaRob(rob_entry_id);
        }
        else{
            busy = false;
            return false;       //PRACTICA: Si pasa por aqui, el registro HI no esta bien configurado
        }
        registro registro_LOW = registro.getReg(registro.LOW);
        if(registro_LOW != null){
            registro_LOW.marcaRob(rob_entry_id);
        }
        else{
            busy = false;
            return false;       //PRACTICA: Si pasa por aqui, el registro LOW no esta bien configurado
        }
        
        return true;
    }

    @Override
    public boolean Execute(int cycle) {
        RobEntry rob_entry = ReorderBuffer.getRoBInstance().getRoB_entry(robID);
        if (estado == 0) {
            //añador ciclo inicial
            ins_info.setEX_init(cycle);
            InstructionSetTomasulo set = InstructionSetTomasulo.getInstance();
            BasicInstruction instr = set.findByBinaryCode(instruction);
            if (instr != null) {
                set.setOp1(Vj);
                set.setOp2(Vk);
                try {
                    instr.getSimulationCode().simulate(null);
                } catch (ProcessingException ex) {
                    Logger.getLogger(EstacionPF_alumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultado = set.getLOW();
                HI = set.getHI();
                LOW = set.getLOW();
            }
        }
        //incremento el estado de STAGE en la entrada del rob (visualización)
        ins_info.setEX_end(cycle);
        estado++; //aumento el estado
        rob_entry.setStage("X" + estado);
        return estado == latencia;
    }

    /**
     * Devuelve el resultado de HI en instrucciones de punto flotante.
     *
     * @return Valor de registro HI
     */
    public int getHI() {
        return HI;
    }

    /**
     * Modifica el valor del registro HI en instrucciones de punto flotante
     *
     * @param HI Nuevo valor del registros HI
     */
    public void setHI(int HI) {
        this.HI = HI;
    }

    /**
     * Devuelve el resultado de LOW en instrucciones de punto flotante.
     *
     * @return Valor de registro LOW
     */
    public int getLOW() {
        return LOW;
    }

    /**
     * Modifica el valor del registro LOW en instrucciones de punto flotante
     *
     * @param LOW Nuevo valor del registros LOW
     */
    public void setLOW(int LOW) {
        this.LOW = LOW;
    }

    /**
     * Genera un string para imprimir la información de la estación de reserva
     * en los ficheros .html
     *
     * @return Información de la estación
     */
    @Override
    public String toString() {
        return (busy ? "  SI    "
                + ((Qj != ES_NULO)
                        ? Decode.normaliza("#" + Qj, 15)
                        : Decode.normaliza("    " + Vj, 15))
                + ((Qk != ES_NULO)
                        ? Decode.normaliza("#" + Qk, 15)
                        : Decode.normaliza("    " + Vk, 15))
                + Decode.normaliza("#" + robID, 5)
                + Decode.normaliza((estado == latencia) ? ("" + resultado) : "", 13)
                + estado + "/" + latencia
                : "  NO    ") + "\n";
    }

    /**
     * Genera un string para la impresión por pantalla en formato html del la
     * entrada del Reorder buffer
     *
     * @return String con la información relevante de la entrada del rob
     */
    @Override
    public String toHtmlTable() {
        String str;
        if (busy) {
            str = getCell("Si");
            str += getCell("#" + robID);
            if (Qj != ES_NULO) {
                str += getCell("#" + Qj) + getCell("");
            } else {
                str += getCell("") + getCell(Vj);
            }
            if (Qk != ES_NULO) {
                str += getCell("#" + Qk) + getCell("");
            } else {
                str += getCell("") + getCell(Vk);
            }
            str += getCell((estado == latencia) ? (Integer.toString(resultado)) : "")
                    + getCell(estado + "/" + latencia);
        } else {
            str = getCell("No");
            for (int i = 0; i < 7; i++) {
                str += getCell("");
            }
        }
        return str;
    }
}
