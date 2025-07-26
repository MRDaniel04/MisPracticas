package uva.ssw.entrega.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.entrega.bd.ComentarioDAO;
import uva.ssw.entrega.modelo.Comentario;
import uva.ssw.entrega.modelo.Post;
import uva.ssw.entrega.modelo.Usuario;

import java.io.IOException;
import java.util.Date; // Para la fecha del comentario si la estableces aquí

@WebServlet(name = "ServletComentario", urlPatterns = {"/crearComentario"})
public class ServletComentario extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 1. Obtener parámetros del formulario
        String idPost = request.getParameter("idPost");
        String contenidoComentario = request.getParameter("contenidoComentario");

        // 2. Obtener usuario de la sesión
        HttpSession session = request.getSession(false); // No crear nueva sesión si no existe
        Usuario autorComentario = null;
        if (session != null) {
            autorComentario = (Usuario) session.getAttribute("usuarioLogueado");
        }

        // 3. Construir URL de redirección (con ancla al post comentado)
        String anclaPost = (idPost != null && !idPost.trim().isEmpty()) ? "#post-" + idPost.trim() : "";
        String urlRedireccionForo = request.getContextPath() + "/foro" + anclaPost;

        // 4. Validaciones
        if (autorComentario == null) {
            System.out.println("ServletComentario: Usuario no logueado intentando comentar.");
            session.setAttribute("errorVoto", "Debes iniciar sesión para comentar."); // Reusamos errorVoto o creamos errorComentario
            response.sendRedirect(urlRedireccionForo);
            return;
        }

        if (idPost == null || idPost.trim().isEmpty()) {
            System.err.println("ServletComentario: idPost faltante o vacío.");
            session.setAttribute("errorVoto", "Error al identificar el post para el comentario.");
            response.sendRedirect(urlRedireccionForo); // O una página de error más genérica
            return;
        }

        if (contenidoComentario == null || contenidoComentario.trim().isEmpty()) {
            System.out.println("ServletComentario: Contenido del comentario vacío.");
            session.setAttribute("errorVoto", "El comentario no puede estar vacío.");
            response.sendRedirect(urlRedireccionForo);
            return;
        }
        // Podrías añadir validación de longitud máxima para el comentario

        // 5. Crear objeto Comentario
        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setContenido(contenidoComentario.trim());
        nuevoComentario.setAutor(autorComentario);

        Post postAsociado = new Post(); // Solo necesitamos el ID del post para la FK
        postAsociado.setId(idPost.trim());
        nuevoComentario.setPost(postAsociado);

        // La fecha la establecerá el DAO o la BD con DEFAULT CURRENT_TIMESTAMP.
        // Si quieres establecerla aquí:
        // nuevoComentario.setFechaComentario(new Date());

        // 6. Interactuar con el DAO para insertar
        ComentarioDAO comentarioDAO = new ComentarioDAO();
        boolean exitoInsercion = false;
        try {
            exitoInsercion = comentarioDAO.insertarComentarioPost(nuevoComentario);

            if (exitoInsercion) {
                System.out.println("ServletComentario: Comentario guardado exitosamente para post ID: " + idPost);
                session.setAttribute("exitoVoto", "¡Comentario publicado!"); // Reusamos o creamos exitoComentario
            } else {
                System.err.println("ServletComentario: DAO no pudo insertar el comentario para post ID: " + idPost);
                session.setAttribute("errorVoto", "No se pudo publicar tu comentario. Inténtalo de nuevo.");
            }
        } catch (Exception e) { // Captura más genérica por si el DAO lanza otras excepciones
            System.err.println("ServletComentario: Excepción al insertar comentario para post ID: " + idPost);
            e.printStackTrace();
            session.setAttribute("errorVoto", "Error interno al publicar el comentario.");
        }

        // 7. Redirigir de vuelta al foro (a la posición del post)
        response.sendRedirect(urlRedireccionForo);
    }

    /**
     * GET no es el método apropiado para crear comentarios.
     * Redirigir al foro o a una página de error.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/foro");
    }
}