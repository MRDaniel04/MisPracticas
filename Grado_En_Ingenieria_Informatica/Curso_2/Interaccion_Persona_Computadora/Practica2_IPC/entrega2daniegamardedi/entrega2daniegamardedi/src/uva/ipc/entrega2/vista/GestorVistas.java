/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author mardedi
 * @author daniega
 */

package uva.ipc.entrega2.vista;

import javax.swing.*;

/**
 * Clase que permite gestionar el cambio de las vistas de la aplicación
 */

public class GestorVistas {
    
    private JFrame estadoActual;
    
    /**
     * Constructor de la clase gestorVistas, que inicia la vista inicial de nuestra aplicación
     */
    
    public GestorVistas(){
        mostrarVista1();
    }
    
    /**
     * Método que permite mostrar la vista inicial de nuestra aplicación (VistaInicial.class).
     * También libera los recursos de la vista anterior a la nueva (en caso de que hubiera vista anterior).
     */
    
    public void mostrarVista1(){
        if(estadoActual!=null){
            estadoActual.setVisible(false);
            estadoActual.dispose();
        }
        estadoActual=new VistaInicial();
        estadoActual.setVisible(true);
        estadoActual.setLocationRelativeTo(null);
    }
    
    /**
     * Método que permite mostrar la vista con los datos de la tarjeta de nuestra aplicación (VistaTarjeta.class).
     * También libera los recursos de la vista anterior a la nueva.
     */
    
    public void mostrarVista2(){
        if(estadoActual!=null){
            estadoActual.setVisible(false);
            estadoActual.dispose();
        }
        estadoActual=new VistaTarjeta();
        estadoActual.setVisible(true);
        estadoActual.setLocationRelativeTo(null);
    }
    
    /**
     * Método que permite mostrar la vista con las opciones de recarga de la 
     * tarjeta en nuestra aplicación (VistaRecargaTarjeta.class).
     * También libera los recursos de la vista anterior a la nueva.
     */
    
    public void mostrarVista3(){
        if(estadoActual!=null){
            estadoActual.setVisible(false);
            estadoActual.dispose();
        }
        estadoActual=new VistaRecargaTarjeta();
        estadoActual.setVisible(true);
        estadoActual.setLocationRelativeTo(null);
    }
    
    /**
     * Método que permite mostrar la vista con los billetes adquiridos por el usuario 
     * en nuestra aplicación (VistaMisViajes.class).
     * También libera los recursos de la vista anterior a la nueva.
     */
    
    public void mostrarVista4(){
        if(estadoActual!=null){
            estadoActual.setVisible(false);
            estadoActual.dispose();
        }
        estadoActual=new VistaMisViajes();
        estadoActual.setVisible(true);
        estadoActual.setLocationRelativeTo(null);
    }
    
    /**
     * Método que permite mostrar la vista con las distintas opciones de
     * adquisición de billetes en nuestra aplicación (y de pago)(Vista.class).
     * También libera los recursos de la vista anterior a la nueva.
     */
    
    public void mostrarVista5(){
        if(estadoActual!=null){
            estadoActual.setVisible(false);
            estadoActual.dispose();
        }
        estadoActual=new Vista();
        estadoActual.setVisible(true);
        estadoActual.setLocationRelativeTo(null);
    }
}