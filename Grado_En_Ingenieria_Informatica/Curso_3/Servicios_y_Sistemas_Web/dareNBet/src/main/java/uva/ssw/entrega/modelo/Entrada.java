/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.modelo;
import java.util.Date;
import java.util.List;

public class Entrada {
    private int id;
    private String titulo;
    private Date fechaPublicacion;
    private String descripcion;
    private String icono;
    private List<Usuario> usuariosVisualizadores;

    public Entrada() { }

    public Entrada(int id, String titulo, Date fechaPublicacion, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public int getId() {
        return id;
    }
  
    public void setId(int id) {
        this.id = id;
    }
  
    public String getTitulo() {
        return titulo;
    }
  
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
  
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }
  
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
  
    public String getDescripcion() {
        return descripcion;
    }
  
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
  
    public List<Usuario> getUsuariosVisualizadores() {
        return usuariosVisualizadores;
    }
  
    public void setUsuariosVisualizadores(List<Usuario> usuariosVisualizadores) {
        this.usuariosVisualizadores = usuariosVisualizadores;
    }
}
