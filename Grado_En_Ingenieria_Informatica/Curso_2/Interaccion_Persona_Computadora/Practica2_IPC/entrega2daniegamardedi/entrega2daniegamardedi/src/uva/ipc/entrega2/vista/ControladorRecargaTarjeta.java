/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author mardedi
 * @author daniega
 */
package uva.ipc.entrega2.vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import uva.ipc.entrega2.modelo.ModeloEntrega;
import uva.ipc.entrega2.main.main;
import java.awt.Color;



public class ControladorRecargaTarjeta {
    private VistaRecargaTarjeta miVista;
    private ModeloEntrega miModelo;
    private Timer temporizador;
    private int segundostranscurridos;
    /**
     * Constructor de controlador
     * @param vista - vista asociada al controlador
     */
    
    public ControladorRecargaTarjeta(VistaRecargaTarjeta vista) {
        this.miVista = vista;
        this.miModelo=main.getModelo();
    }
    
    /**
     * Permite llamar al método de miVista que cambia el contenido del textfield
     * del saldo con la cantidad de saldo disponible obtenida del modelo
     */
    
    public void iniciarSaldo(){
        miVista.setTextSaldo(miModelo.getsaldo());
    }

    /**
     * Procesa el evento de dejar 2 segundos el ratón encima de la tarjeta para
     * asi conseguir el efecto de pago contactless. Cuando termina, muestra un textfield 
     * para escribir el pin y unos jlabels
     */
    
    public void procesarEventoTimer() {
    segundostranscurridos = 0;
    temporizador = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            segundostranscurridos++;
            if (segundostranscurridos >= 2) {
                temporizador.stop();
                miVista.mostrarPIN();
            }
        }
    });
    temporizador.start();
    }
    
    /**
     * Procesa el evento de retirada de el ratón (de la tarjeta)
     * En este caso , lo que pasará es que no se hayan cumplido los 2 segundos que debe
     * de estar el ratón encima del text field generando un mensaje de error
     */
    
    public void procesarEventoTimerEnd(){
        temporizador.stop();
        if(segundostranscurridos<2){
            miVista.cambioMensajeTarjeta("No estuvo 2 segundos",Color.RED);
        }
    }
    
    /**
     * Comprueba que el pin de la tarjeta sea correcto
     * @param pin - pin que el usuario introduce en el text field
     */
    
    public void comprobarPinTarjeta(String pin){
        if(Integer.parseInt(pin)==miModelo.getpinTarjeta()){
            miModelo.setsaldo(miModelo.getsaldo()+miVista.getRecarga());
            main.getGestorVistas().mostrarVista1();
            //TODO: Mensaje de todo correcto en pantalla de inicio
        }
        else{
            miVista.errorContrasena();
        }
    }
    
    /**
     * Procesa evento de darle al boton de volver (cambio de vista)
     */
    
    public void VolverButtonEvento(){
        main.getGestorVistas().mostrarVista2();
    }
}
