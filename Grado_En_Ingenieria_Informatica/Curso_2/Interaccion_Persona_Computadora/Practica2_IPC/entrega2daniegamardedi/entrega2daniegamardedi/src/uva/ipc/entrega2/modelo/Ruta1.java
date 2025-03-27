/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ipc.entrega2.modelo;


import java.util.ArrayList;

/**
 * @author daniega
 * @author dediego
 */

/**
 * Clase que representa una ruta de tren.
 */
public class Ruta1 {
    private String idRuta;
    private String estacionOrigen;
    private String estacionDestino;
    private int tiempo;
    private double precio;
    private ArrayList<String> horariosSemana;
    private ArrayList<String> horariosFinDeSemana;
    
    
    /**
     * Constructor de la clase Ruta1.
     *
     */
     
    public Ruta1(String idRuta, String estacionOrigen, String estacionDestino, int tiempo, double precio, String horariosSemana, String horariosFinDeSemana){

        this.idRuta = idRuta;
        this.estacionOrigen = estacionOrigen;
        this.estacionDestino = estacionDestino;
        this.tiempo = tiempo;
        this.precio = precio;
        this.horariosSemana = parseHorarios(horariosSemana);
        this.horariosFinDeSemana = parseHorarios(horariosFinDeSemana);
    }
    
    /**
     * Parsea los horarios de la ruta de una cadena a una lista de horarios.
     *
     * @param horarios La cadena que contiene los horarios separados por comas.
     * @return Una ArrayList con los horarios.
     */
    
    private ArrayList<String> parseHorarios(String horarios) {
        String[] horariosArray = horarios.split(",");
        ArrayList<String> horariosList = new ArrayList<>();
        for (String horario : horariosArray) {
            horariosList.add(horario.trim());
        }
        return horariosList;
    }
     
    /**
     * Obtiene el ID de la ruta.
     *
     * @return El ID de la ruta.
     */
    
    public String getIdRuta() {
        return idRuta;
    }
    
    /**
     * Obtiene la estación de origen de la ruta.
     *
     * @return La estación de origen.
     */
    
    public String getEstacionOrigen() {
        return estacionOrigen;
    }
    
    /**
     * Obtiene la estación de destino de la ruta.
     *
     * @return La estación de destino.
     */
    
    public String getEstacionDestino() {
        return estacionDestino;
    }
    
    /**
     * Obtiene el tiempo de duración de la ruta en minutos.
     *
     * @return El tiempo de duración.
     */
    
    public int getTiempo() {
        return tiempo;
    }
    
    /**
     * Obtiene el precio de la ruta.
     *
     * @return El precio de la ruta.
     */
    
    public double getPrecio() {
        return precio;
    }
    
    /**
     * Obtiene los horarios de la ruta durante la semana.
     *
     * @return Los horarios de la semana.
     */
    
    public ArrayList<String> getHorariosSemana() {
        return horariosSemana;
    }
    
    /**
     * Obtiene los horarios de la ruta los fines de semana.
     *
     * @return Los horarios de fin de semana.
     */
    
    public ArrayList<String> getHorariosFinDeSemana() {
        return horariosFinDeSemana;
    }

}
