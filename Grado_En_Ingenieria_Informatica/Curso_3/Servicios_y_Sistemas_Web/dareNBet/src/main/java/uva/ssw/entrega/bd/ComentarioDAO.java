package uva.ssw.entrega.bd;

import uva.ssw.entrega.modelo.Comentario;
import uva.ssw.entrega.modelo.Usuario;
import uva.ssw.entrega.modelo.Post; // Necesitarás importar Post si quieres obtenerlo aquí

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ComentarioDAO {

    // SQL para insertar un comentario en un Post
    // Asume que tu tabla Comentario tiene columnas: contenido, usuario_id, post_id, fechaComentario (con default)
    // y que el id del comentario es AUTO_INCREMENT
    private static final String SQL_INSERT_COMENTARIO_POST =
            "INSERT INTO Comentario (contenido, usuario_id, post_id, fechaComentario) VALUES (?, ?, ?, ?)";

    // (Más adelante) SQL para obtener comentarios de un post
    private static final String SQL_SELECT_COMENTARIOS_BY_POST_ID =
            "SELECT c.id, c.contenido, c.fechaComentario, " +
            "u.id AS autor_id, u.nombreUsuario AS autor_nombreUsuario, u.nombre AS autor_nombre, u.apellido AS autor_apellido " + // Datos del autor
            "FROM Comentario c " +
            "JOIN Usuario u ON c.usuario_id = u.id " +
            "WHERE c.post_id = ? ORDER BY c.fechaComentario ASC"; // O DESC si quieres los más nuevos primero


    /**
     * Inserta un nuevo comentario para un Post en la base de datos.
     *
     * @param nuevoComentario El objeto Comentario a insertar. Se espera que tenga
     *                        contenido, autor (con ID) y post (con ID).
     *                        El id del comentario y la fecha se manejarán por la BD/aquí.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarComentarioPost(Comentario nuevoComentario) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        boolean exito = false;

        // Validaciones básicas
        if (nuevoComentario == null ||
            nuevoComentario.getContenido() == null || nuevoComentario.getContenido().trim().isEmpty() ||
            nuevoComentario.getAutor() == null || nuevoComentario.getAutor().getidUsuario() <= 0 ||
            nuevoComentario.getPost() == null || nuevoComentario.getPost().getId() == null || nuevoComentario.getPost().getId().trim().isEmpty()) {
            System.err.println("ComentarioDAO (insertarComentarioPost): Datos del comentario inválidos.");
            return false;
        }

        String query = SQL_INSERT_COMENTARIO_POST;

        try {
            connection = pool.getConnection();
            if (connection == null) {
                System.err.println("ComentarioDAO (insertarComentarioPost): No se pudo obtener conexión.");
                return false;
            }
            // No necesitamos transacción explícita para una sola inserción simple,
            // a menos que también actualices contadores en otra tabla.

            ps = connection.prepareStatement(query); // , Statement.RETURN_GENERATED_KEYS para obtener ID si lo necesitas

            ps.setString(1, nuevoComentario.getContenido().trim());
            ps.setInt(2, nuevoComentario.getAutor().getidUsuario());
            ps.setString(3, nuevoComentario.getPost().getId());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // Establecer fecha actual

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1) {
                exito = true;
                System.out.println("ComentarioDAO: Comentario insertado para post ID: " + nuevoComentario.getPost().getId() +
                                   " por usuario ID: " + nuevoComentario.getAutor().getidUsuario());
                // Si necesitaras el ID autogenerado del comentario:
                // ResultSet generatedKeys = ps.getGeneratedKeys();
                // if (generatedKeys.next()) {
                //     nuevoComentario.setId(generatedKeys.getInt(1));
                // }
            } else {
                System.err.println("ComentarioDAO (insertarComentarioPost): La inserción no afectó filas.");
            }

        } catch (SQLException e) {
            System.err.println("ComentarioDAO (insertarComentarioPost): Error SQL al insertar comentario.");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) pool.freeConnection(connection);
        }
        return exito;
    }


    /**
     * Obtiene todos los comentarios para un post específico, ordenados por fecha.
     *
     * @param idPost El ID del post del que se quieren obtener los comentarios.
     * @return Una lista de objetos Comentario.
     */
    public List<Comentario> obtenerComentariosPorPost(String idPost) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Comentario> comentarios = new ArrayList<>();

        if (idPost == null || idPost.trim().isEmpty()) {
            System.err.println("ComentarioDAO (obtenerComentariosPorPost): ID de post inválido.");
            return comentarios; // Devuelve lista vacía
        }

        String query = SQL_SELECT_COMENTARIOS_BY_POST_ID;

        try {
            connection = pool.getConnection();
            if (connection == null) {
                System.err.println("ComentarioDAO (obtenerComentariosPorPost): No se pudo obtener conexión.");
                return comentarios;
            }

            ps = connection.prepareStatement(query);
            ps.setString(1, idPost);
            rs = ps.executeQuery();

            while (rs.next()) {
                Comentario comentario = new Comentario();
                comentario.setId(rs.getInt("id"));
                comentario.setContenido(rs.getString("contenido"));

                Timestamp fechaSql = rs.getTimestamp("fechaComentario");
                if (fechaSql != null) {
                    comentario.setFechaComentario(new Date(fechaSql.getTime()));
                }

                // Crear y poblar el objeto Usuario autor
                Usuario autor = new Usuario();
                autor.setidUsuario(rs.getInt("autor_id"));
                autor.setNombreUsuario(rs.getString("autor_nombreUsuario"));
                autor.setNombre(rs.getString("autor_nombre"));
                autor.setApellido(rs.getString("autor_apellido"));
                // No necesitamos todos los datos del autor aquí, solo los relevantes para mostrar
                comentario.setAutor(autor);

                // No necesitamos poblar el objeto Post completo aquí, ya sabemos el idPost
                // Si quisieras, podrías crear un objeto Post simple solo con el ID
                // Post postAsociado = new Post();
                // postAsociado.setId(idPost);
                // comentario.setPost(postAsociado);

                comentarios.add(comentario);
            }

        } catch (SQLException e) {
            System.err.println("ComentarioDAO (obtenerComentariosPorPost): Error SQL al obtener comentarios para post ID: " + idPost);
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) pool.freeConnection(connection);
        }
        return comentarios;
    }
}