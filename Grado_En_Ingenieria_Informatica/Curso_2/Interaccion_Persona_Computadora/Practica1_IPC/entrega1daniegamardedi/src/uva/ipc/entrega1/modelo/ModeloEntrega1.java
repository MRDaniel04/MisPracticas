/**
 * @author mardedi
 * @author daniega
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ipc.entrega1.modelo;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase Modelo
 */

public class ModeloEntrega1 {
    
    private ArrayList<Ruta1> trenes;
    private int pinTarjeta = 1234;
    private double saldoTarjeta=20;
    
    /**
     * Constructor de modelo
     */
    
    public ModeloEntrega1() {
        trenes = new ArrayList<>();
        inicializarTrenes();
    }
    
    /**
     * Obtiene el ArrayList de todas las rutas de los trenes.
     * @return La lista de trenes.
     */
       
    public ArrayList<Ruta1> getTrenes() {
        return this.trenes;
    }
   
    /**
     * Inicializa el ArrayList de trenes a partir del archivo "rutas.csv".
     * El archivo debe tener el formato: idRuta;estacionOrigen;estacionDestino;tiempo;precio;horariosSemana;horariosFinDeSemana
     */
    
    private void inicializarTrenes() {
        String line;
        String split = ";";
        boolean primeraLinea=true;
        try (BufferedReader br = new BufferedReader(new FileReader("rutas.csv"))) {
            while ((line = br.readLine()) != null) {
                if(!primeraLinea){
                    String[] data = line.split(split);
                    String idRuta = data[0];
                    String estacionOrigen = data[1];
                    String estacionDestino = data[2];
                    int tiempo = Integer.parseInt(data[3]);
                    double precio = Double.parseDouble(data[4]);
                    String horariosSemana = data[5];
                    String horariosFinDeSemana = data[6];
                    trenes.add(new Ruta1(idRuta, estacionOrigen, estacionDestino, tiempo, precio,  horariosSemana, horariosFinDeSemana));
                }else{
                    primeraLinea=false;
                }
            }
        } catch (IOException e) {
        }
    }
    
    /**
     * Método que permite cambiar el pin de la tarjeta
     * @param nuevoPin - int - nuevo pin de la tarjeta
     */
    
    public void setpinTarjeta(int nuevoPin){
        pinTarjeta=nuevoPin;
    }
    
    /**
     * Método que permite obtener el pin actual de la tarjeta
     * @return pinTarjeta - int
     */
    
    public int getpinTarjeta(){
        return pinTarjeta;
    }
    
    /**
     * Método que permite modificar el saldo actual
     * @param saldo - double - nuevo saldo 
     */
    
    public void setsaldo(double saldo){
        this.saldoTarjeta=saldo;
    }
    
    /**
     * Método que permite obtener el saldo actual de la tarjeta
     * @return saldoTarjeta - double
     */
    
    public double getsaldo(){
        return saldoTarjeta;
    }
}