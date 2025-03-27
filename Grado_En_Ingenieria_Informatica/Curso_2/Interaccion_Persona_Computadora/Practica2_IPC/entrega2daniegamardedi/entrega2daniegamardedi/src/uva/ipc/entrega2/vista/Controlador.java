/**
 * @author mardedi
 * @author daniega
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ipc.entrega2.vista;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.Timer;
import uva.ipc.entrega2.modelo.ModeloEntrega;

import java.util.List;
import uva.ipc.entrega2.main.main;
import uva.ipc.entrega2.modelo.Billete;

/**
 * Clase Controlador
 */

public class Controlador {
    private Vista miVista;
    private ModeloEntrega miModelo;
    private Timer temporizador;
    private int segundostranscurridos;
    private final String errorPrimerPaso = "No ha seleccionado bien los datos";
    private final String errorSegundoPaso = "No ha seleccionado ningún tren";
    private String idRuta=null;
    private String duracion=null;
    private String precio=null;
    private String hora=null;

    
    /**
     * Constructor de controlador
     * @param vista - vista asociada al controlador
     */
    
    public Controlador(Vista vista) {
        this.miVista = vista;
        this.miModelo = main.getModelo();
    }
    
    /**
     * Llama a la función del modelo que cambia el estado de la variable
     * estaEditando del modelo a true, ya que ha habido un billete editado recientemente
     */
    
    public void cambiarBilleteEdicion(){
        miModelo.setCambiarBilleteEdicion(true);
    }
    
    /**
     * Método getter para obtener el valor de isEditando
     * @return 
     */
    
    public boolean isEditando() {
        return miModelo.getIsEditando();
    }

    /**
     * Llama a la función del modelo que cambia el estado de la variable
     * isEditando del modelo a editando, ya que un billete esta siendo editado
     * @param editando - boolean 
     */
    
    public void setEditando(boolean editando) {
        miModelo.setEditando(editando);
    }
    
    /**
     * Permite obtener el billete que esta siendo  editado ahora mismo
     * @return 
     */
    
    public Billete getBillete(){
        return miModelo.getBillete();
    }
    
    /**
     * Crea una lista con las estaciones disponibles a partir del fichero estaciones.csv
     * @return - Lista con las estaciones disponibles
     */
    
    public List<String> getEstaciones() {
        List<String> estaciones = new ArrayList<>();
        boolean primeraLinea=true;

        try (BufferedReader br = new BufferedReader(new FileReader("estaciones.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if(!primeraLinea){
                    estaciones.add(linea);
                }else{
                    primeraLinea=false;
                }
            }
        } catch (IOException e) {
        }

        return estaciones;
    } 

    /**
     * Actualiza la lista de trenes en la vista utilizando los trenes obtenidos del modelo.
     */
    
    public void listaDeTrenes(){
        miVista.actualizarTrenes(miModelo.getTrenes());
    }
    
    /**
     * Permite saber si una fecha dada ya ha pasado o no
     * @param fecha - fecha con la que se quiere comparar la fecha actual
     * @return boolean true = es fecha futura, false = es fecha pasada
     */
    
    public boolean isFutureDate(Date fecha) {
        Date actual = new Date();
        if(actual.equals(fecha) || actual.before(fecha)){
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * Procesa el evento de la tarjeta contactless
     * Comienza un timer que en el caso de que llegue a 2 segundos tomará la tarjeta aceptada
     * y llamará a la función correspondiente de la vista
     */
    
    public void procesarEventoTimer() {
    segundostranscurridos = 0;
    temporizador = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            segundostranscurridos++;
            if (segundostranscurridos >= 2) {
                temporizador.stop();
                if (miVista.gettarjetaTextFieldState()) {
                    miVista.tarjetaAceptada();
                } else {
                    double precio=miVista.getPrecio(miModelo.getTrenes());
                    if (miModelo.getsaldo() >= precio) {
                        miModelo.setsaldo(miModelo.getsaldo() - precio);
                        miVista.tarjetaAceptadaPin();
                    } else {
                        miVista.cambiomensajetarjeta("Saldo insuficiente");
                    }
                }
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
            miVista.cambiomensajetarjeta("No estuvo 2 segundos");
        }
    }
    
    /**
     * Método que permite obtener el error del primer paso
     * @return 
     */
    
    public String getPrimerPasoError(){
        return errorPrimerPaso;
    }
    
    /**
     * Método que permite obtener el error del segundo paso
     * @return 
     */
    
    public String getSegundoPasoError(){
        return errorSegundoPaso;
    }
    
    
    /**
     * Método que comprueba si el pin introducido por el usuario es el mismo
     * que el que se encuentra guardado en el modelo
     * @param pin 
     */
    
    public void comprobarPinTarjeta(String pin){
        if(Integer.parseInt(pin)==miModelo.getpinTarjeta()){
            miVista.tarjetaAceptadaPin();
        }
        else{
            miVista.errorContrasena();
        }
    }
    
    /**
     * Método que permite mostrar la vista inicial
     */
    
    public void procesarEventoInicioButton(){
        main.getGestorVistas().mostrarVista1();
    }
    
    
    /**
     * Método set para idRuta
     * @param idRuta - identificacion de la ruta del billete
     */
    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    /**
     * Método get para idRuta
     * @return idRuta - String
     */
    public String getIdRuta() {
        return idRuta;
    }

    /**
     * Método set para duracion del trayecto
     * @param duracion - duracion del trayecto del billete
     */
    
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    /**
     * Método get para duración del trayecto
     * @return duracion - String
     */
    public String getDuracion() {
        return duracion;
    }

    /**
     * Método set para el precio del billete
     * @param precio - precio del billete
     */
    
    public void setPrecio(String precio) {
        this.precio = precio;
    }

    /**
     * Método get para precio del billete
     * @return precio - String
     */
    public String getPrecio() {
        return precio;
    }

    /**
     * Método set para la hora de salida del billete
     * @param hora - hora de salida
     */
    
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * // Método get para hora
     * @return hora - String
     */
    
    public String getHora() {
        return hora;
    }
    
    /**
     * Permite llamar a la funcion del modelo que cambia el número de billetes comprados
     */
    
    public void procesarNumeroBilletes(){
        miModelo.setNumBilletesComprados(miModelo.getNumBilletesComprados()+1);
    }
    
    /**
     * Permite saber si ay un billete en edicion ahora mismo
    */
    
    public boolean getEdicion(){
        boolean estado = miModelo.getIsEditando();
        miModelo.setEditando(false);
        return estado;
    }
}