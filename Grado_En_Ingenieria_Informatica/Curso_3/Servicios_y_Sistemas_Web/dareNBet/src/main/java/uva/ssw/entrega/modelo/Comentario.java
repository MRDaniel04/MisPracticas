package uva.ssw.entrega.modelo;

import java.io.Serializable; // Importar Serializable
import java.util.Date;       // Importar Date para fechaComentario

public class Comentario implements Serializable { // Implementar Serializable

    private static final long serialVersionUID = 1L; // Buena práctica

    private int id;
    private String contenido;
    private Usuario autor; // Cambiado de 'usuario' a 'autor' para claridad
    private Post post;       // El post al que pertenece el comentario
    private Apuesta apuesta; // La apuesta a la que pertenece (si aplica)
    private Date fechaComentario; // Fecha de publicación del comentario

    // Constructor vacío
    public Comentario() { }

    // Constructor principal (para comentarios de Posts)
    // Podrías tener otro para Apuestas si lo necesitas, o manejarlo en el DAO
    public Comentario(String contenido, Usuario autor, Post post) {
        this.contenido = contenido;
        this.autor = autor;
        this.post = post;
        // La fecha se puede establecer al crear o la BD la pondrá por defecto
        // El ID será autogenerado por la BD
    }


    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Usuario getAutor() { // Getter para autor
        return autor;
    }

    public void setAutor(Usuario autor) { // Setter para autor
        this.autor = autor;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Apuesta getApuesta() {
        return apuesta;
    }

    public void setApuesta(Apuesta apuesta) {
        this.apuesta = apuesta;
    }

    public Date getFechaComentario() {
        return fechaComentario;
    }

    public void setFechaComentario(Date fechaComentario) {
        this.fechaComentario = fechaComentario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", contenido='" + contenido + '\'' +
                ", autor=" + (autor != null ? autor.getNombreUsuario() : "null") +
                ", post=" + (post != null ? post.getId() : "null") +
                ", apuesta=" + (apuesta != null ? apuesta.getId() : "null") +
                ", fechaComentario=" + fechaComentario +
                '}';
    }
}