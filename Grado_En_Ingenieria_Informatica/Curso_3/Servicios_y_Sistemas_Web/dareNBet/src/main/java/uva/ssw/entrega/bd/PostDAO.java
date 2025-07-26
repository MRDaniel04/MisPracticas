package uva.ssw.entrega.bd;

import uva.ssw.entrega.modelo.Post;
import uva.ssw.entrega.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class PostDAO {

    // --- SQL para insertar un nuevo post (SIN nLikes, nDislikes) ---
    private static final String SQL_INSERT_POST =
            "INSERT INTO Post (id, titulo, contenido, fechaPublicacion, nVisualizaciones, autor_id) " +
            "VALUES (?, ?, ?, ?, ?, ?)"; // 6 parámetros ahora

    /**
     * Obtiene todos los posts de la base de datos, ordenados por fecha descendente.
     * Los contadores de likes y dislikes se calculan mediante subconsultas a VotosPost.
     *
     * @return Una lista de objetos Post, o una lista vacía si no hay posts o si ocurre un error.
     */
    public List<Post> obtenerPosts() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Post> listaPosts = new ArrayList<>();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // ----- QUERY MODIFICADA: Calcula likes/dislikes desde VotosPost -----
        String query = "SELECT p.id, p.titulo, p.contenido, p.fechaPublicacion, p.nVisualizaciones, p.autor_id, " +
                       " (SELECT COUNT(*) FROM VotosPost vp WHERE vp.idPost = p.id AND vp.tipoVoto = 1) AS totalLikes, " +
                       " (SELECT COUNT(*) FROM VotosPost vp WHERE vp.idPost = p.id AND vp.tipoVoto = -1) AS totalDislikes " +
                       "FROM Post p ORDER BY p.fechaPublicacion DESC";

        try {
            connection = pool.getConnection();
            if (connection == null) {
                System.err.println("PostDAO (obtenerPosts): No se pudo obtener conexión.");
                return listaPosts;
            }

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getString("id"));
                post.setTitulo(rs.getString("titulo"));
                post.setContenido(rs.getString("contenido"));

                Timestamp fechaSql = rs.getTimestamp("fechaPublicacion");
                if (fechaSql != null) {
                   post.setFechaPublicacion(new Date(fechaSql.getTime()));
                } else {
                   post.setFechaPublicacion(null);
                }
                post.setnVisualizaciones(rs.getInt("nVisualizaciones"));

                // ----- Obtener likes y dislikes calculados -----
                post.setnLikes(rs.getInt("totalLikes"));
                post.setnDislikes(rs.getInt("totalDislikes"));

                int usuarioId = rs.getInt("autor_id");
                Usuario autor = null;
                if (usuarioId > 0) {
                    try {
                       autor = usuarioDAO.obtenerUsuarioPorId(usuarioId);
                       if (autor == null) {
                          System.err.println("PostDAO (obtenerPosts): No se encontró usuario con ID: " + usuarioId + " para el post ID: " + post.getId());
                          autor = new Usuario();
                          autor.setidUsuario(usuarioId); // Conservar el ID si es posible
                          autor.setNombre("Usuario");
                          autor.setApellido("Desconocido (ID:" + usuarioId +")");
                       }
                    } catch (Exception e) {
                       System.err.println("PostDAO (obtenerPosts): Error al obtener usuario con ID: " + usuarioId + " - " + e.getMessage());
                       // e.printStackTrace(); // Considera si quieres la traza completa aquí
                       autor = new Usuario();
                       autor.setNombre("Error");
                       autor.setApellido("Autor");
                    }
                } else { // Manejar el caso de autor_id <= 0 (ej. usuario anónimo con ID 1 si ANONYMOUS_USER_ID = 1)
                   System.err.println("PostDAO (obtenerPosts): ID de autor " + usuarioId + " para el post ID: " + post.getId() + ". Asignando Anónimo si es 1 o Desconocido.");
                   if (usuarioId == 1) { // Asumiendo que ANONYMOUS_USER_ID es 1
                       autor = new Usuario();
                       autor.setidUsuario(1);
                       autor.setNombre("Usuario");
                       autor.setApellido("Anónimo");
                   } else {
                       autor = new Usuario(); // Placeholder general para ID inválido
                       autor.setNombre("Usuario");
                       autor.setApellido("Inválido");
                   }
                }
                post.setAutor(autor);

                listaPosts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("PostDAO (obtenerPosts): Error SQL al ejecutar la consulta.");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) pool.freeConnection(connection);
        }
        return listaPosts;
    }


    /**
     * Inserta un nuevo post en la base de datos (sin nLikes, nDislikes).
     *
     * @param nuevoPost El objeto Post que contiene la información a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarPost(Post nuevoPost) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        boolean exito = false;

        if (nuevoPost == null || nuevoPost.getId() == null || nuevoPost.getId().isEmpty() ||
            nuevoPost.getTitulo() == null || nuevoPost.getTitulo().trim().isEmpty() ||
            nuevoPost.getContenido() == null || nuevoPost.getContenido().trim().isEmpty() ||
            nuevoPost.getAutor() == null || nuevoPost.getAutor().getidUsuario() <= 0) { // Asumiendo que ID 0 no es válido para un autor real
            System.err.println("PostDAO (insertar): Datos del post inválidos o autor no válido/nulo. Post: " + nuevoPost);
            return false;
        }

        // ----- QUERY INSERT MODIFICADA: Sin nLikes, nDislikes -----
        String query = SQL_INSERT_POST; // Usa la constante definida al inicio

        try {
            connection = pool.getConnection();
            if (connection == null) {
                System.err.println("PostDAO (insertar): No se pudo obtener conexión.");
                return false;
            }

            ps = connection.prepareStatement(query);

            // Establecer los parámetros (6 ahora)
            ps.setString(1, nuevoPost.getId());
            ps.setString(2, nuevoPost.getTitulo().trim());
            ps.setString(3, nuevoPost.getContenido().trim());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // fechaPublicacion
            ps.setInt(5, 0); // nVisualizaciones (se inicializa a 0)
            ps.setInt(6, nuevoPost.getAutor().getidUsuario()); // autor_id

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1) {
                exito = true;
                System.out.println("PostDAO (insertar): Post insertado correctamente con ID: " + nuevoPost.getId());
            } else {
                System.err.println("PostDAO (insertar): La inserción afectó " + filasAfectadas + " filas (se esperaba 1). Post ID: " + nuevoPost.getId());
            }

        } catch (SQLException e) {
            System.err.println("PostDAO (insertar): Error SQL al insertar el post con ID: " + nuevoPost.getId());
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) pool.freeConnection(connection);
        }
        return exito;
    }

    /**
     * Registra o actualiza un voto de un usuario para un post.
     * Ya NO actualiza contadores en la tabla Post.
     *
     * @param idPost El ID del post.
     * @param idUsuario El ID del usuario que vota.
     * @param nuevoTipoVoto El nuevo tipo de voto (1 para like, -1 para dislike).
     * @return Un objeto ResultadoVoto con el estado y mensaje.
     */
    public ResultadoVoto registrarActualizarVoto(String idPost, int idUsuario, int nuevoTipoVoto) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = null;
        PreparedStatement psConsultaVoto = null;
        PreparedStatement psInsertarVoto = null;
        PreparedStatement psActualizarVoto = null;
        // PreparedStatement psActualizarContadoresPost = null; // <--- YA NO SE USA
        ResultSet rs = null;
        ResultadoVoto resultado = new ResultadoVoto();

        String sqlConsultaVoto = "SELECT tipoVoto FROM VotosPost WHERE idPost = ? AND idUsuario = ?";
        String sqlInsertarVoto = "INSERT INTO VotosPost (idPost, idUsuario, tipoVoto) VALUES (?, ?, ?)";
        String sqlActualizarVoto = "UPDATE VotosPost SET tipoVoto = ?, fechaVoto = CURRENT_TIMESTAMP WHERE idPost = ? AND idUsuario = ?";
        // String sqlActualizarPost = "..."; // <--- YA NO SE USA

        try {
            con = pool.getConnection();
            if (con == null) {
                resultado.mensaje = "Error de conexión.";
                return resultado;
            }
            con.setAutoCommit(false);

            psConsultaVoto = con.prepareStatement(sqlConsultaVoto);
            psConsultaVoto.setString(1, idPost);
            psConsultaVoto.setInt(2, idUsuario);
            rs = psConsultaVoto.executeQuery();

            int votoExistente = 0;
            if (rs.next()) {
                votoExistente = rs.getInt("tipoVoto");
            }

            if (votoExistente == nuevoTipoVoto) {
                resultado.mensaje = (nuevoTipoVoto == 1) ? "Ya diste like a este post." : "Ya diste dislike a este post.";
                resultado.operacionRealizada = false;
                con.rollback();
                return resultado;
            }

            int filasVotoAfectadas = 0;
            if (votoExistente == 0) {
                psInsertarVoto = con.prepareStatement(sqlInsertarVoto);
                psInsertarVoto.setString(1, idPost);
                psInsertarVoto.setInt(2, idUsuario);
                psInsertarVoto.setInt(3, nuevoTipoVoto);
                filasVotoAfectadas = psInsertarVoto.executeUpdate();
                System.out.println("Voto insertado: user=" + idUsuario + ", post=" + idPost + ", tipo=" + nuevoTipoVoto);
            } else {
                psActualizarVoto = con.prepareStatement(sqlActualizarVoto);
                psActualizarVoto.setInt(1, nuevoTipoVoto);
                psActualizarVoto.setString(2, idPost);
                psActualizarVoto.setInt(3, idUsuario);
                filasVotoAfectadas = psActualizarVoto.executeUpdate();
                System.out.println("Voto actualizado: user=" + idUsuario + ", post=" + idPost + ", tipo=" + nuevoTipoVoto);
            }

            // ----- SECCIÓN DE ACTUALIZAR CONTADORES EN POST ELIMINADA -----
            // Ahora solo confirmamos si la operación en VotosPost fue exitosa.
            if (filasVotoAfectadas == 1) {
                con.commit();
                resultado.operacionRealizada = true;
                resultado.mensaje = "Voto registrado correctamente.";
            } else {
                con.rollback();
                resultado.mensaje = "Error al procesar el voto en la tabla de votos.";
                System.err.println("No se afectaron filas en VotosPost para user=" + idUsuario + ", post=" + idPost);
            }

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error SQL en registrarActualizarVoto: " + e.getMessage());
            e.printStackTrace();
            resultado.mensaje = "Error de base de datos al procesar el voto.";
        } finally {
            // Cerrar todos los recursos
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psConsultaVoto != null) psConsultaVoto.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psInsertarVoto != null) psInsertarVoto.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psActualizarVoto != null) psActualizarVoto.close(); } catch (SQLException e) { e.printStackTrace(); }
            // try { if (psActualizarContadoresPost != null) psActualizarContadoresPost.close(); } catch (SQLException e) { e.printStackTrace(); } // Ya no existe
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
                pool.freeConnection(con);
            }
        }
        return resultado;
    }

    // Clase interna ResultadoVoto (sin cambios)
    public static class ResultadoVoto {
        public boolean operacionRealizada = false;
        public String mensaje = "";
    }
}