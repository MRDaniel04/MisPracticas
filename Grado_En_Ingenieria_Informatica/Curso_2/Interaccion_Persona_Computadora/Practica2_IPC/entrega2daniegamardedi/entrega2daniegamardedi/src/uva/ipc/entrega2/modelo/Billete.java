/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author mardedi
 * @author daniega
 */

package uva.ipc.entrega2.modelo;

import java.time.LocalTime;
import java.time.LocalDate;

/**
 * Clase billete que permite guardar las diversas características de los Billetes.
 * @author danig
 */

public class Billete {
    private LocalDate fecha;
    private LocalTime hora;
    private String duracion;
    private String precio;
    private String origen;
    private String destino;
    
    /**
     * Constructor de la clase billete
     * @param fecha - fecha de salida del tren
     * @param hora - hora de salida del tren
     * @param origen - origen del trayecto
     * @param destino - destino del trayecto
     * @param duracion - duracion del trayecto
     * @param precio - precio del trayecto
     */
    
    public Billete(LocalDate fecha,LocalTime hora ,String origen,String destino ,String duracion, String precio) {
        this.fecha=fecha;
        this.hora=hora;
        this.origen=origen;
        this.destino=destino;
        this.duracion = duracion;
        this.precio = precio;
    }

    /**
     * Método get para la fecha de salida del tren
     * @return fecha - LocalDate
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Método set para la fecha de salida del tren
     * @param fecha - LocalDate
     */
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    /**
     * Método set para el origen del trayecto
     * @param origen - String
     */
    
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    
    /**
     * Método get para el origen del trayecto
     * @return origen - String
     */
    
    public String getOrigen() {
        return origen;
    }

    /**
     * Método get para el destino del trayecto
     * @return destino - String
     */
    
    public String getDestino() {
        return destino;
    }

    /**
     * Método set para el destino del trayecto
     * @param destino - String
     */
    
    public void setDestino(String destino) {
        this.destino = destino;
    }
    
    /**
     * Método get para la duración del trayecto
     * @return duracion - String
     */

    public String getDuracion() {
        return duracion;
    }
    
    /**
     * Método set para la duración del trayecto
     * @param duracion - String
     */

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    /**
     * Método get para el precio del trayecto
     * @return precio - String
     */
    
    public String getPrecio() {
        return precio;
    }

    /**
     * Método set para el precio del trayecto
     * @param precio - String
     */
    
    public void setPrecio(String precio) {
        this.precio = precio;
    }

    /**
     * Método get para la hora del trayecto
     * @return hora - LocalTime
     */
    
    public LocalTime getHora() {
        return hora;
    }

    /**
     * Método set para la hora del trayecto
     * @param hora - LocalTime
     */
    
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    /**
     * Método toString() de la clase Billete
     * @return String con las características del billete
     */
    
    @Override
    public String toString() {
        return getFecha()+";"+getHora()+";"+getOrigen()+";"+getDestino()+";"+getDuracion()+";"+getPrecio();
    }
   
}