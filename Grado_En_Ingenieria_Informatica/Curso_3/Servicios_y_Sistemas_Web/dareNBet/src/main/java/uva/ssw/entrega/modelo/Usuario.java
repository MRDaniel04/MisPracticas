
package uva.ssw.entrega.modelo;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Usuario {
    private static final AtomicInteger contadorID = new AtomicInteger(1); //
    private int id;
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private int edad;
    private String contrasena; 
    private String correoElectronico;
    private String numeroTelefono; 
    private int nCreditos;
    private String imagen;
    private Date fechaInscripcion;
    private List<Tema> temas;
    private Timestamp ultimaRecompensa;
    private boolean isAdmin;

    public Usuario() { }

    public Usuario(String nombreUsuario, String nombre, String apellido,int edad, String contrasena,
                   String correoElectronico,String numeroTelefono,boolean isAdmin) {
        this.id=contadorID.getAndIncrement();
        this.nombreUsuario=nombreUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad=edad;
        this.contrasena = contrasena;
        this.correoElectronico = correoElectronico;
        this.numeroTelefono = numeroTelefono;
        this.nCreditos = 50;
        this.fechaInscripcion = Date.from(Instant.now()); 
        this.isAdmin = isAdmin;
        
    }
    
    public int getidUsuario(){
        return id;
    }
    
    public void setidUsuario(int id){
        this.id = id;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Getters y Setters
    // Getter
    public Timestamp getUltimaRecompensa() {
        return ultimaRecompensa;
}

// Setter
    public void setUltimaRecompensa(Timestamp ultimaRecompensa) {
        this.ultimaRecompensa = ultimaRecompensa;
    }
   
  
    public String getNombre() {
        return nombre;
    }
  
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
  
    public String getApellido() {
        return apellido;
    }
  
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
  
    public String getContrasena() {
        return contrasena;
    }
  
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
  
    public String getCorreoElectronico() {
        return correoElectronico;
    }
  
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
  
    
  
    public String getNumeroTelefono() {
        return numeroTelefono;
    }
  
    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }
  
    public int getNCreditos() {
        return nCreditos;
    }
  
    public void setNCreditos(int nCreditos) {
        this.nCreditos = nCreditos;
    }
  
    public String getImagen() {
        return imagen;
    }
  
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
  
    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }
  
    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
  
    public List<Tema> getTemas() {
        return temas;
    }
  
    public void setTemas(List<Tema> temas) {
        this.temas = temas;
    }

    public void setEdad(int edad){
        this.edad = edad;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public int getId(){
        return id;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setId(int usuarioId) {
        this.id = id;
    }
}
