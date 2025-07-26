package uva.ssw.entrega.modelo;

import java.io.Serializable;
import java.util.ArrayList; // Importar ArrayList
import java.util.Date;
import java.util.List;    // Importar List

public class Post implements Serializable {

    private static final long serialVersionUID = 1L; // Mantener como estaba si es el mismo

    private String id;
    private String titulo;
    private String contenido;
    private Date fechaPublicacion;
    private int nVisualizaciones;
    private int nLikes;     // Este atributo ahora se calcula desde VotosPost, pero lo mantenemos en el modelo
    private int nDislikes;  // Este atributo ahora se calcula desde VotosPost, pero lo mantenemos en el modelo
    private Usuario autor;
    private List<Comentario> comentarios; // Lista de comentarios para este post

    // Constructor sin parámetros
    public Post() {
        this.comentarios = new ArrayList<>(); // Inicializar la lista de comentarios
        // nLikes y nDislikes se establecen después de calcularlos desde VotosPost
        // no se inicializan a 0 aquí si se calculan siempre
    }

    // Constructor con parámetros
    public Post(String id, String titulo, String contenido, Date fechaPublicacion, Usuario autor) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.autor = autor;
        this.nVisualizaciones = 0; // Se puede inicializar nVisualizaciones aquí si se desea
        this.comentarios = new ArrayList<>(); // Inicializar la lista de comentarios
        // nLikes y nDislikes se calcularán y se establecerán externamente (desde el DAO)
        // Por lo tanto, no se inicializan a 0 aquí explícitamente como antes
        // si se van a sobreescribir siempre con los valores calculados.
        // Si quieres un valor por defecto hasta que se calculen, puedes poner this.nLikes = 0; this.nDislikes = 0;
    }

    // Getters y Setters para todos los atributos

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getnVisualizaciones() {
        return nVisualizaciones;
    }

    public void setnVisualizaciones(int nVisualizaciones) {
        this.nVisualizaciones = nVisualizaciones;
    }

    public int getnLikes() {
        return nLikes;
    }

    public void setnLikes(int nLikes) {
        this.nLikes = nLikes;
    }

    public int getnDislikes() {
        return nDislikes;
    }

    public void setnDislikes(int nDislikes) {
        this.nDislikes = nDislikes;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    // Getter y Setter para la lista de comentarios
    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "Post{" +
               "id='" + id + '\'' +
               ", titulo='" + titulo + '\'' +
               ", contenido='" + (contenido != null ? contenido.substring(0, Math.min(contenido.length(), 30)) + "..." : "null") + '\'' + // Acortar contenido para toString
               ", fechaPublicacion=" + fechaPublicacion +
               ", nVisualizaciones=" + nVisualizaciones +
               ", nLikes=" + nLikes +  // Se mostrará el valor calculado
               ", nDislikes=" + nDislikes + // Se mostrará el valor calculado
               ", autor=" + (autor != null ? autor.getNombreUsuario() : "null") +
               ", comentarios_count=" + (comentarios != null ? comentarios.size() : 0) + // Mostrar conteo de comentarios
               '}';
    }
}