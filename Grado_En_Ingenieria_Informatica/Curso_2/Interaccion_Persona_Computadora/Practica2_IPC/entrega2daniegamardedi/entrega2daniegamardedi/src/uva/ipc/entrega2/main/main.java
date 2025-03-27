/**
 * @author mardedi
 * @author daniega
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ipc.entrega2.main;

import uva.ipc.entrega2.vista.GestorVistas;
import uva.ipc.entrega2.modelo.ModeloEntrega;

/**
 * Clase main de nuestra aplicación
 */
        
public class main {
    
    private static GestorVistas gestor;
    private static ModeloEntrega miModelo;
    
    /**
     * Método main de nuestra aplicación que permite iniciarla gracias al gestorVistas
     * @param args - argumentos
     */
    
    public static void main(String [] args) {
        gestor = new GestorVistas();
        miModelo=new ModeloEntrega();
    }
    
    /**
     * Método que permite obtener el modelo asociado a la aplicación
     * de la aplicación.
     * @return 
     */
    
    public static ModeloEntrega getModelo(){
        return miModelo;
    }
    
    /**
     * Método que permite obtener el gestor de Vistas asociado a la aplicación
     * @return 
     */
    
    public static GestorVistas getGestorVistas(){
        return gestor;
    }
}