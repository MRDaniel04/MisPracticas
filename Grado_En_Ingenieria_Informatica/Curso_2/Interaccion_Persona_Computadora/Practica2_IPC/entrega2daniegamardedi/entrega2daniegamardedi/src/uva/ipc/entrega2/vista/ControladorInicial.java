/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author mardedi
 * @author daniega
 */
package uva.ipc.entrega2.vista;

import uva.ipc.entrega2.modelo.ModeloEntrega;
import uva.ipc.entrega2.main.main;

/**
 * Clase controlador para la Vista Inicial
 */

public class ControladorInicial {
    private VistaInicial miVista;
    private ModeloEntrega miModelo;
    
    /**
     * Constructor de la clase ControladorInicial
     * @param miVista - Vista asociada a este controlador
     */
    
    public ControladorInicial(VistaInicial miVista){
        this.miVista=miVista;
        this.miModelo=main.getModelo();
    }
    
    /**
     * Método que permite mostrar la vista de compra de un nuevo billete (Vista.class).
     */
    
    public void procesarEventoComprarBilleteButton(){
        main.getGestorVistas().mostrarVista5();
    }
    
    /**
     * Método que permite mostrar la vista de opciones de la tarjeta del usuario (VistaTarjeta.class).
     */
    
    public void procesarEventoAccesoConTarjetaButton(){
        main.getGestorVistas().mostrarVista2();
    }
}