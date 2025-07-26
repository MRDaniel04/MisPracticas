/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.modelo;

import java.util.Date;
import java.util.List;

public class Apuesta {
    private String id;
    private int nVisualizaciones;
    private int nLikes;
    private int nDislikes;
    private int nCreditosTotal;
    private String titulo;
    private String descripcion;
    private String imagen;
    private Date fechaPublicacion;
    private Date fechaFin;
    private Creador creador;
    private List<OpcionApuesta> opciones;

    private String tags; 
    public Apuesta() { }

    public Apuesta(String id,
                   int nVisualizaciones,
                   int nLikes,
                   int nDislikes,
                   int nCreditosTotal,
                   String titulo,
                   String descripcion,
                   String imagen,
                   Date fechaPublicacion,
                   Date fechaFin,
                   Creador creador,
                   String tags)   
    {
        this.id = id;
        this.nVisualizaciones = nVisualizaciones;
        this.nLikes = nLikes;
        this.nDislikes = nDislikes;
        this.nCreditosTotal = nCreditosTotal;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaFin = fechaFin;
        this.creador = creador;
        this.tags = tags;      
    }

    // Getters y Setters 

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getNVisualizaciones() {
        return nVisualizaciones;
    }
    public void setNVisualizaciones(int nVisualizaciones) {
        this.nVisualizaciones = nVisualizaciones;
    }

    public int getNLikes() {
        return nLikes;
    }
    public void setNLikes(int nLikes) {
        this.nLikes = nLikes;
    }

    public int getNDislikes() {
        return nDislikes;
    }
    public void setNDislikes(int nDislikes) {
        this.nDislikes = nDislikes;
    }

    public int getNCreditosTotal() {
        return nCreditosTotal;
    }
    public void setNCreditosTotal(int nCreditosTotal) {
        this.nCreditosTotal = nCreditosTotal;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Date getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Creador getCreador() {
        return creador;
    }
    public void setCreador(Creador creador) {
        this.creador = creador;
    }

 
    public String getTags() {      
        return tags;
    }

    public void setTags(String tags) {  
        this.tags = tags;
    }
    
    public List<OpcionApuesta> getOpciones() {
        return opciones;
    }
    public void setOpciones(List<OpcionApuesta> opciones) {
        this.opciones = opciones;
    }
    
}
