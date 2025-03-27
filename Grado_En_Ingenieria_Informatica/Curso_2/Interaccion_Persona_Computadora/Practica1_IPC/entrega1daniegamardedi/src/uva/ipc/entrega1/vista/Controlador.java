/**
 * @author mardedi
 * @author daniega
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ipc.entrega1.vista;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Timer;
import uva.ipc.entrega1.modelo.ModeloEntrega1;
import uva.ipc.entrega1.modelo.Ruta1;
import java.util.List;

/**
 * Clase Controlador
 */

public class Controlador {
    private Vista miVista;
    private ModeloEntrega1 miModelo;
    private Timer temporizador;
    private int segundostranscurridos;
    private final String errorPrimerPaso = "No ha seleccionado bien los datos";
    private final String errorSegundoPaso = "No ha seleccionado ningún tren";
    
    /**
     * Constructor de controlador
     * @param vista - vista asociada al controlador
     */
    
    public Controlador(Vista vista) {
        this.miVista = vista;
        this.miModelo = new ModeloEntrega1();
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
}