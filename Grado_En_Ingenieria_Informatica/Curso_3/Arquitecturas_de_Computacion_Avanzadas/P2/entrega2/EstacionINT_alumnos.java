package mars.pipeline.tomasulo.estaciones_alumnos;

import java.util.logging.Level;
import java.util.logging.Logger;
import mars.Globals;
import mars.ProcessingException;
import mars.mips.instructions.BasicInstruction;
import mars.mips.instructions.BasicInstructionFormat;
import mars.pipeline.Decode;
import static mars.pipeline.DiagramaMulticiclo.HtmlUtils.getCell;
import mars.pipeline.tomasulo.*;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;

/**
 * Estación de enteros
 *
 * @author Francisco Andújar
 */
public class EstacionINT_alumnos extends Estacion {

    /**
     * Crea una estación de enteros
     */
    public EstacionINT_alumnos() {
        super(LAT_ENTEROS);
    }

    /**
     * Envía a una estación de reserva una operaciones con enteros (excepto
     * multiplicación y división) A estas unidades se envía las instrucciones:
     *
     * Tipo R (excepto multiplicación y división, que utilizan dos registros de
     * entrada (Rs y Rd) y un registro destino (Rd).
     *
     * Las aritmético-lógicas tipo I, que utilizan un registro de entrada (Rs),
     * un registro destino (Rt) e inmediato.
     *
     * Los saltos para su resolución, que utilizan dos registros de entrada (Rs
     * y Rt).
     *
     * Sin embargo, existen algunas excepciones. Por ello se recomienda utilizar
     * las funciones proporcionadas por la clase Decode para consultar los
     * operandos fuente y destino.
     *
     * @param instruction Instrucción a guardar en la estación de reserva
     * @param rob_entry_id Identificador de la entrada del RoB asociada a la
     * instrucción
     * @param cycle Ciclo actual de ejecución.
     * @return true si la instrucción se ha podido guardar en la estación. false
     * en otro caso
     */
    @Override
    public boolean Ocupar(int instruction, int rob_entry_id, int cycle) {
        if (busy) {
            return false;
        }
        this.instruction = instruction;
        busy = true;
        //actualizar marca de tiempo
        marca_tiempo = cycle;
        estado = 0;
        //obtengo el tipo de la instrucción
        BasicInstructionFormat type = Decode.getFormat(instruction);
        //Anotar  id de entrada en el ROB
        this.robID = rob_entry_id;

        /*PRACTICA: Inicializamos valor de operandos */
        Qj = ES_NULO;
        Vj = 0;
        Qk = ES_NULO;
        Vk = 0;
        
        /*PRACTICA: 1º operando, siempre se obtiene */
        int rs = Decode.getRs(instruction);
        checkQj(rs);

        /*PRACTICA: 2º operando, solo para formato R o formato salto condicional */
            /*PRACTICA: En caso de que sea I_BRANCH_FORMAT o R_FORMAT*/
            if(BasicInstructionFormat.I_BRANCH_FORMAT.equals(type)||BasicInstructionFormat.R_FORMAT.equals(type)){
                int rt = Decode.getRt(instruction);
                checkQk(rt);
            }
            /*PRACTICA: en caso de que sea I_FORMAT*/
            else if(BasicInstructionFormat.I_FORMAT.equals(type)){
                Qk = ES_NULO;
                Vk = Decode.getInm(instruction);
            }
        /*PRACTICA: escritura en registro*/
        int dest = Decode.getDestination(instruction);
        if(dest!=0){
            registro reg_dest = registro.getReg(dest);
            if(reg_dest==null){
                busy = false;
                return false;
            }
            reg_dest.marcaRob(this.robID);
        }
        return true;
    }

    @Override
    public boolean Execute(int cycle) {
        RobEntry rob_entry = ReorderBuffer.getRoBInstance().getRoB_entry(robID);
        if (estado == 0) {
            //añado el ciclo inicial
            ins_info.setEX_init(cycle);
            InstructionSetTomasulo set = InstructionSetTomasulo.getInstance();
            BasicInstruction instr = set.findByBinaryCode(instruction);
            if (instr != null) {
                if (Decode.hasShamt(instruction)) {
                    set.setOp1(Vk);
                    set.setOp2(Decode.getShamt(instruction));
                } else {
                    set.setOp1(Vj);
                    set.setOp2(Vk);
                }
                try {
                    instr.getSimulationCode().simulate(null);
                } catch (ProcessingException ex) {
                    Logger.getLogger(EstacionINT_alumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultado = set.getResult();
            }
        }
        //pongo el estado de la entrada del rob a EX
        rob_entry.setStage("EX");
        estado++; //aumento el estado
        if (estado == latencia) {
            ins_info.setEX_end(cycle);
            return true;
        }
        return false;
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
