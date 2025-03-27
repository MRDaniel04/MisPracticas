/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author mardedi
 * @author daniega
 */
package uva.ipc.entrega2.vista;

import uva.ipc.entrega2.modelo.Billete;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import uva.ipc.entrega2.main.main;
import uva.ipc.entrega2.modelo.ModeloEntrega;

/**
 * Clase controlador de la ventana Mis Viajes
 */

public class ControladorMisViajes {
    private VistaMisViajes miVista;
    private ModeloEntrega miModelo;
    private List<Billete> billetes;
    
    /**
     * Constructor de la clase ControladorMisViajes
     * @param miVista - Vista asociada a este controlador
     */
    
    public ControladorMisViajes(VistaMisViajes miVista){
        this.miVista=miVista;
        this.miModelo=main.getModelo();
        if(miModelo.getCambiarBilleteEdicion()){
            eliminar(miModelo.getBillete().toString());
            miModelo.setCambiarBilleteEdicion(false);
            miModelo.setBillete();
        }
        billetes= new ArrayList<>();
        cargarBilletesDesdeArchivo();
    }
    
    /**
     * Procesa el evento de darle al botón de inicio
     */
    
    public void procesarEventoInicioButton() {
        main.getGestorVistas().mostrarVista1();
    }
    
    /**
     * Permite rellenar crear un ArrayList de billetes (clase implementada) para
     * después poder en vista asignarlos a unos de los 2 jlist dependiendo de la fecha.
     */
    
    public void cargarBilletesDesdeArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader("villetes.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
                try {
                    LocalDate fecha = LocalDate.parse(partes[0],formatoFecha);
                    LocalTime hora = LocalTime.parse(partes[1], formatoHora);
                    Billete billete = new Billete(fecha, hora, partes[2], partes[3],partes[4],partes[5]);
                    billetes.add(billete);
                    
                } catch (DateTimeParseException e) {}
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    /**
     * Método que permite eliminar un elemento del archivo de billetes comprados, 
     * ya sea porque el usuario lo devuelve o porque lo edita
     * @param billeteEliminar - String, billete que se desea eliminar
     */
    
    public void eliminar(String billeteEliminar){
        String nombreArchivo = "villetes.csv";
        ArrayList<String> lineasArchivo = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("Fichero lineas: "+linea);
                if (!linea.equals(billeteEliminar)) {
                    lineasArchivo.add(linea);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Eliminar el contenido del archivo original
        try (BufferedWriter clearWriter = new BufferedWriter(new FileWriter(nombreArchivo))) {
            // Escribir una cadena vacía para borrar el contenido
            clearWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for(String linea : lineasArchivo) {
                    bw.write(linea);
                    bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lineasArchivo.clear();

    }

    /**
     * Método getter para obtener el List de los billetes
     * @return billetes - lista de billetes
     */

    public List<Billete> obtenerBilletes() {
        return billetes;
    }
    
    /**
     * Método que realizará diversas acciones, como printear un mensaje de error o
     * pasar al próximo paso de la gestión de los viajes dependiendo de las opciones
     * que el usuario haya elegido.
     */
    
    public void procesarEventoConfirmarButton(String billete){
        int index=0;
        if(miVista.getList2selected()&&miVista.getDevolverBillete()){
            String[]partes=billete.split(";");
            System.out.println("partes"+partes[5]);
            double dinero = Double.parseDouble(partes [5].substring(0,(partes[5].length()-1)));
            System.out.println("dinero:"+dinero);
            miModelo.setsaldo(miModelo.getsaldo()+dinero);
            miModelo.setNumBilletesComprados(miModelo.getNumBilletesComprados()-1);
            index=miVista.getSelectedIndex();
            miVista.removeSelection(index);
            miVista.deselectRadioButtons();
            eliminar(billete);
        }
        
        else if(miVista.getList2selected()&&miVista.getEditarBillete()){
            miModelo.obtenerBillete( billete);
            miModelo.setEditando(true);
            //eliminar(billete);
            main.getGestorVistas().mostrarVista5();
            //TODO: ir
        }
        else if(!miVista.getList2selected()&&miVista.getEditarBillete()){
            miVista.cambioMensajeError("Seleccione adecuadamente el billete que desea modificar", Color.red);
        }
        else if(!miVista.getList2selected()&&miVista.getDevolverBillete()){
            miVista.cambioMensajeError("Seleccione adecuadamente el billete que desea devolver", Color.red);
        }
        else{
            miVista.cambioMensajeError("Seleccione adecuadamente si desea editar su billete o devolverlo", Color.red);
        }
    }
    
    /**
     * Método que permite deseleccionar una lista además de añadir un mensaje de error.
     * Método creado ya que los billetes de la lista de billetes antiguos no se deben poder seleccionar
     * ya que estos no pueden ser modificados ni devueltos.
     */
    
    public void procesaEventoDeselectListas(){
        miVista.cambioMensajeError("No se puede seleccionar un billete antiguo (no se puede devolver ni editar)", Color.DARK_GRAY);
        miVista.deselectList1();
    }
    
}
