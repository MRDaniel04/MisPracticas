/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.modelo;
import java.util.Date;

public class Creador extends Usuario {

    public Creador() {
        super();
    }

    public Creador(int id,String nombreUsuario, String nombre, String apellido, int edad, String contrasena, String correoElectronico, String numeroTelefono,int nCreditos, String imagen, Date fechaInscripcion, boolean isAdmin) {
        super(nombreUsuario, nombre, apellido,edad, contrasena, correoElectronico,
              numeroTelefono,isAdmin);
    }
    
   
}
