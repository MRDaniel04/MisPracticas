/**
 * @author mardedi
 * @author daniega
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ipc.entrega2.modelo;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Modelo
 */

public class ModeloEntrega {
    
    private ArrayList<Ruta1> trenes;
    private int pinTarjeta = 1234;
    private double saldoTarjeta=20;
    private int numBilletesComprados=0;
    private Billete billeteEdicion;
    private boolean isEditando=false;
    private boolean estaEditando=false;
    private boolean primeravez = true;
    
    /**
     * Constructor de modelo
     */
    
    public ModeloEntrega() {
        trenes = new ArrayList<>();
        inicializarTrenes();
    }
    
    /**
     * Permite setear si es la primera vez que el usuario abre la app
     * @param primeravez 
     */
    
    public void setprimeravez(boolean primeravez){
        this.primeravez=primeravez;
    }
    
    /**
     * Permite saber si es la primera vez que se ejecuta el programa para un usuario
     * @return - primeravez - boolean
     */
    
    public boolean getprimeravez(){
        return this.primeravez;
    }
    
    /**
     * Permite cambiar el estado de estaEditando
     * @param x - boolean
     */
    
    public void setCambiarBilleteEdicion(boolean x){
        this.estaEditando=x;
        this.isEditando=false;
    }
    
    /**
     * Permite obtener el estado de estaEditando
     * @return estaEditando - true = ha habido un billete editado hace poco, false = lo contrario
     */
    
    public boolean getCambiarBilleteEdicion(){
        return estaEditando;
    }
    
    /**
     * Método que permite setar el número de billetes comprados por el usuario
     * @param numBilletesComprados - int
     */
    
    public void setNumBilletesComprados(int numBilletesComprados){
        this.numBilletesComprados=numBilletesComprados;
    }
    
    /**
     * Método que permite obtener la cantidad de billetes comprados
     * @return numBilletesComprados - int
     */
    
    public int getNumBilletesComprados(){
        return numBilletesComprados;
    }
    
    /**
     * Método get para la variable booleana isEditando
     * @return isEditando - true = se esta editando un billete, false = no se esta editando un billete
     */
    
    public boolean getIsEditando() {
        return isEditando;
    }

    /**
     * Método set para la variable booleana isEditando que permite saber
     * si se está editando un billete.
     * @param editando 
     */
    
    public void setEditando(boolean editando) {
        isEditando = editando;
    }
    
    /**
     * Método que permite setear el billete editado a unos parametros dados
     * @param linea - parámetros del billete en formato String
     */
    
    public void obtenerBillete(String linea){
        String[] partes = linea.split(";");
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate fecha = LocalDate.parse(partes[0],formatoFecha);
        LocalTime hora = LocalTime.parse(partes[1], formatoHora);
        billeteEdicion = new Billete(fecha, hora, partes[2], partes[3],partes[4],partes[5]);
    }
    
    /**
     * Método get que permite obtener el billete que está siendo editado ahora mismo
     * @return 
     */
    
    public Billete getBillete(){
        return billeteEdicion;
    }
    
    /**
     * Método set para el billete que esta siendo editado en este momento (iniciado a null)
     */
    
    public void setBillete(){
        this.billeteEdicion=null;
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