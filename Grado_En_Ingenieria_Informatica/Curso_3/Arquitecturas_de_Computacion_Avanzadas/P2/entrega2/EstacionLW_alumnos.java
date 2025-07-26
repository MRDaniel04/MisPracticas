package mars.pipeline.tomasulo.estaciones_alumnos;

import java.util.logging.Level;
import java.util.logging.Logger;
import mars.ProcessingException;
import mars.mips.instructions.BasicInstruction;
import mars.mips.instructions.BasicInstructionFormat;
import mars.pipeline.Decode;
import static mars.pipeline.DiagramaMulticiclo.HtmlUtils.getCell;
import mars.pipeline.tomasulo.*;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;

/**
 * Estación de carga
 *
 * @author Francisco Andújar
 */
public class EstacionLW_alumnos extends Estacion {

    /**
     * Crea una estación de enteros
     */
    public EstacionLW_alumnos() {
        super(LAT_LOAD);
        Qk = ES_NULO; //Siempre va a ser ES_NULO
    }

    /**
     * Envía a una estación de reserva una carga de memoria. Estas unidades
     * tienen un solo registro de origen (Rs) + inmediato; y un registro
     * destino. En el caso del Vk, escribir el valor del Inmediato con extensión
     * de signo, y poner Qk directamente a ES_NULO.
     *
     * @param instruction Instrucción a guardar en la estación de reserva.
     * @param rob_entry_id Identificador de la entrada del RoB asociada a la
     * instrucción.
     * @param cycle Ciclo actual de ejecución.
     * @return true si la instrucción se ha podido guardar en la estación. false
     * en otro caso.
     */
    public boolean Ocupar(int instruction, int rob_entry_id, int cycle) {
        if (busy) {
            return false;
        }
        this.instruction = instruction;
        busy = true;
        //actualizo marca de tiempo
        marca_tiempo = cycle;
        estado = 0;
        //Anotar  id de entrada en el ROB
        this.robID = rob_entry_id;

        /*PRACTICA: Inicializamos valor de operandos */
        Qj = ES_NULO;
        Vj = 0;
        Qk = ES_NULO;
        Vk = 0;

        /*PRACTICA: Siempre una instruccion LW va a ser I_FORMAT */
        if(Decode.getFormat(instruction).equals(BasicInstructionFormat.I_FORMAT)){
            /*PRACTICA: 1º operando*/
            int rs = Decode.getRs(instruction);
            checkQj(rs);
            /*PRACTICA: Obtencion desplazamiento */
            this.desp = Decode.getInm_ExtensionSigno(instruction);
            /*PRACTICA: Obtencion registro destino */
            int dest = Decode.getDestination(instruction);
            registro reg_dest = registro.getReg(dest);
            if(reg_dest == null){
                busy = false;
                return false;
            }
            reg_dest.marcaRob(this.robID);
        }
        else{
            return false;
        }
        return true;
    }

    public boolean Execute(int cycle) {
        RobEntry rob_entry = ReorderBuffer.getRoBInstance().getRoB_entry(robID);
        if (estado == 0) {
            //FASE AC:
            dir = Vj + desp;
            ins_info.setAC(cycle);
            //pongo el estado de la entrada del rob a AC
            rob_entry.setStage("AC");
            estado = 1;
            InstructionSetTomasulo set = InstructionSetTomasulo.getInstance();
            BasicInstruction instr = set.findByBinaryCode(instruction);
            if (instr != null) {
                set.setOp2(Vj);
                set.setOp1(desp);
                try {
                    instr.getSimulationCode().simulate(null);
                } catch (ProcessingException ex) {
                    Logger.getLogger(EstacionLW_alumnos.class.getName()).log(Level.SEVERE, null, ex);
                }
                resultado = set.getResult();
            }
        } else {
            if (estado == 1) {
                //añador ciclo inicial
                ins_info.setEX_init(cycle);
                //pongo el estado de la entrada del rob a MUL

            }

            rob_entry.setStage("M" + estado);
            estado++; //aumento el estado
            ins_info.setEX_end(cycle);

            return (estado == latencia);
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
                        : Decode.normaliza("    0x" + Integer.toHexString(Vj), 15))
                + Decode.normaliza(Integer.toString(desp), 7)
                + Decode.normaliza((estado != 0) ? ("0x" + Integer.toHexString(dir)) : "", 12)
                + Decode.normaliza("#" + robID, 4)
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
                str += getCell("") + getCell("0x" + Integer.toHexString(Vj));
            }
            str += getCell("0x" + Integer.toHexString(desp));
            str += getCell((estado != 0) ? ("0x" + Integer.toHexString(dir)) : "");

            str += getCell((estado == latencia) ? ("" + resultado) : "")
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
