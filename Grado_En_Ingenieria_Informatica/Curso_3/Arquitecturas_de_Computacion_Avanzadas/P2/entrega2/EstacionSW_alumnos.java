package mars.pipeline.tomasulo.estaciones_alumnos;

import mars.pipeline.Decode;
import static mars.pipeline.DiagramaMulticiclo.HtmlUtils.getCell;
import mars.pipeline.tomasulo.*;
import static mars.pipeline.tomasulo.Tomasulo_conf.*;

import mars.mips.instructions.BasicInstructionFormat;

/**
 * Estación de almacenamiento
 *
 * @author Francisco Andújar
 */
public class EstacionSW_alumnos extends Estacion {

    private boolean pendienteLiberacion;

    /**
     * Libera realmente una estacion si se indico su liberación pero todavía es
     * necesario mantener su información para generar los ficheros de estados
     * (necesario en pipelines con especulación)
     */
    public void LiberarSiPendiente() {
        if (pendienteLiberacion) {
            pendienteLiberacion = false;
            Qk = Qj = ES_NULO;
            estado = 0;
        }
    }

    /**
     * Crea una estación de enteros
     */
    public EstacionSW_alumnos() {
        super(LAT_STORE);
        pendienteLiberacion = false;
    }

    /**
     * Libera un estación de reserva
     */
    @Override
    public void Libera() {
        if (conEspeculacion) {
            busy = false;
            pendienteLiberacion = true;
        } else {
            super.Libera();
        }

    }

    /**
     * Consulta si la estación está libre u ocupada
     *
     * @return true si estación libre, false es casoContrario
     */
    @Override
    public boolean EstaLibre() {
        return !(busy || pendienteLiberacion);
    }

    @Override
    public boolean ready_for_WB() {
        return false;
    }

    /**
     * Envía a una estación de reserva una almacenamiento en memoria. Esta
     * función tiene dos operandos: Rs (calculo dirección) y Rt (valor a
     * guardar). En el caso del Vk, escribir el valor del Inmediato con
     * extensión de signo, y poner Qk directamente a ES_NULO.
     *
     * No actualiza los registros, por lo que no es necesario actualizar el
     * fichero de registros.
     *
     * Se debe poner el flag de confirmado a true para la práctica 2. Para la
     * práctica 3 (especulación) se inicializará a false.
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

        /*No modificar las siguientes líneas. Necesario para mostrar correctamente los datos en HTML*/
        if (conEspeculacion) {
            ReorderBuffer.getRoBInstance().getRoB_entry(rob_entry_id).WBdone();
            confirmado = false;
        } else {
            confirmado = true;
        }
        this.instruction = instruction;
        if (busy) {
            return false;
        }

        busy = true;
        //actualizo marca de tiempo
        marca_tiempo = cycle;
        estado = 0;

        //anoto la entrada de la estación
        this.robID = rob_entry_id;
        /*A rellenar por el alumno: obtención de operandos 
        y actualización de marcas en el banco de registros (cuando corresponda)*/

        /*PRACTICA: Siempre una instruccion LW va a ser I_FORMAT */
        if(Decode.getFormat(instruction).equals(BasicInstructionFormat.I_FORMAT)){
            /*PRACTICA: Inicializamos valor de operandos */
            Qj = ES_NULO;
            Vj = 0;
            Qk = ES_NULO;
            Vk = 0;
            int desp = 0;
            /*PRACTICA: Primer operando */
            int rs = Decode.getRs(instruction);
            checkQj(rs);

            /*PRACTICA: Segundo operando */
            int rt = Decode.getRt(instruction);
            checkQk(rt);

            /*PRACTICA: Tercer operando */
            this.desp = Decode.getInm_ExtensionSigno(instruction);
        }else{
            busy = false;
            return false;
        }
        return true;
    }

    @Override
    public boolean Execute(int cycle) {
        RobEntry rob_entry = ReorderBuffer.getRoBInstance().getRoB_entry(robID);
        if (estado == 0) {
            //FASE AC:
            dir = Vj + desp;
            ins_info.setAC(cycle);
            //pongo el estado de la entrada del rob a AC
            if (rob_entry != null) {
                rob_entry.setStage("AC");
            }
            estado = 1;
        } else {
            if (estado == 1) {
                //añador ciclo inicial
                ins_info.setEX_init(cycle);
                //pongo el estado de la entrada del rob a MUL
            }
            //incremento el estado de STAGE en la entrada del rob (visualización)
            if (rob_entry != null) {
                rob_entry.setStage("M" + estado);
            }
            ins_info.setEX_end(cycle);
            estado++; //aumento el estado
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
                + Decode.normaliza("#" + robID, 5)
                + ((Qk != ES_NULO)
                        ? Decode.normaliza("#" + Qk, 15)
                        : Decode.normaliza("    " + Vk, 15))
                + Decode.normaliza(confirmado ? "Si" : "No", 8)
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
        if (busy || pendienteLiberacion) {
            str = getCell("Si");
            str += getCell("#" + robID);

            if (Qj != ES_NULO) {
                str += getCell("#" + Qj) + getCell("");
            } else {
                str += getCell("") + getCell("0x" + Integer.toHexString(Vj));
            }
            str += getCell("0x" + Integer.toHexString(desp));
            str += getCell((estado != 0) ? ("0x" + Integer.toHexString(dir)) : "");

            if (Qk != ES_NULO) {
                str += getCell("#" + Qk) + getCell("");
            } else {
                str += getCell("") + getCell(Vk);
            }
            str += getCell(confirmado ? "Si" : "No")
                    + getCell(estado + "/" + latencia);
        } else {
            str = getCell("No");
            for (int i = 0; i < 9; i++) {
                str += getCell("");
            }
        }
        return str;
    }
}
