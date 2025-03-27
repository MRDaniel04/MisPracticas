package uva.ipc.entrega2.vista;

import java.io.BufferedReader;
import java.io.FileReader;
import uva.ipc.entrega2.main.main;
import uva.ipc.entrega2.modelo.ModeloEntrega;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 * @author mardedi
 * @author daniega
 */

/**
 * Clase del controladorTarjeta - controlador de VistaTarjeta
 */

public class ControladorTarjeta {
    private VistaTarjeta miVista;
    private ModeloEntrega miModelo;
    
    /**
     * Constructor de ControladorTarjeta
     * @param vista - vista asociada al controlador
     */
    
    public ControladorTarjeta(VistaTarjeta vista) {
        this.miVista = vista;
        miModelo=main.getModelo();
    }
    
    /**
     * devuelve el valor del número de billetes que el usuario ha adquirido
     * @return int - numero de billetes comprados
     */
    
    public String iniciarNumBiletes(){
        return String.valueOf(miModelo.getNumBilletesComprados());
    }
    
    /**
     * Llama a la funcion de miVista que modifica el text field donde se muestra el saldo
     * pasandole el saldo actual
     */
    
    public void iniciarSaldo(){
        miVista.setTextSaldo(miModelo.getsaldo());
    }
    
    /**
     * Permite volver a la vista anterior (vista inicial)
     */
    
    public void procesarEventoVolverButton(){
        main.getGestorVistas().mostrarVista1();
    }
  
    /**
     * Permite pasar a la siguiente vista si se elige la opcion de 
     * RecargaTarjeta
     */
    
    public void procesarEventoRecargarTarjetaButton(){
        main.getGestorVistas().mostrarVista3();
    }
    
    /**
     * Permite pasar a la siguiente vista si se elige la opcion de 
     * MisViajes
     */
    
    public void procesarEventoMisViajesButton(){
        main.getGestorVistas().mostrarVista4();
    }
    
    /**
     * Permite saber el número de billetes que hay en el fichero
     * para asi obtener el numero que se escribe en el text field de 
     * numero de billetes comprados
     */
    
    public void añadirbilletes(){
        int contadorlineas=0;
        if(miModelo.getprimeravez()){
            miModelo.setprimeravez(false);
            try (BufferedReader br = new BufferedReader(new FileReader("villetes.csv"))) {
                while (br.readLine() != null) {
                    contadorlineas++;
                }
            }catch(Exception e){}
            miModelo.setNumBilletesComprados(contadorlineas);
        }
        if(miModelo.getCambiarBilleteEdicion()){
            miModelo.setNumBilletesComprados(miModelo.getNumBilletesComprados()-1);
        }
    }
}
