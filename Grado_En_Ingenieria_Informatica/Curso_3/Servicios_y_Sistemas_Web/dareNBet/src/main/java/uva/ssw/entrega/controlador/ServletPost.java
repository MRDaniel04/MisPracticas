package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.PostDAO;
import uva.ssw.entrega.bd.UsuarioDAO;
import uva.ssw.entrega.bd.ComentarioDAO; // <-- IMPORTAR ComentarioDAO
import uva.ssw.entrega.modelo.Post;
import uva.ssw.entrega.modelo.Usuario;
import uva.ssw.entrega.modelo.Comentario; // <-- IMPORTAR Comentario

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList; // Necesario para la lista vacía en catch
import java.util.List;
import java.util.UUID;

@WebServlet(name = "ServletPost", urlPatterns = {"/foro"})
public class ServletPost extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int ANONYMOUS_USER_ID = 1; // ID para el usuario anónimo si aplica

    /**
     * Maneja GET: Carga los posts y sus comentarios para mostrar en foro.jsp.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PostDAO postDAO = new PostDAO();
        ComentarioDAO comentarioDAO = new ComentarioDAO(); // Instancia de ComentarioDAO
        List<Post> listaPosts = null;

        try {
            // 1. Obtener la lista de posts
            listaPosts = postDAO.obtenerPosts(); // Este método ya debería calcular likes/dislikes

            // 2. Cargar comentarios para cada post
            if (listaPosts != null && !listaPosts.isEmpty()) {
                for (Post post : listaPosts) {
                    List<Comentario> comentariosDelPost = comentarioDAO.obtenerComentariosPorPost(post.getId());
                    post.setComentarios(comentariosDelPost); // Asignar la lista de comentarios al objeto Post
                }
            }
            request.setAttribute("postsForo", listaPosts);

        } catch (Exception e) {
            System.err.println("Error al obtener posts y/o comentarios en doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorForo", "Error al cargar el contenido del foro. Inténtalo más tarde.");
            request.setAttribute("postsForo", new ArrayList<Post>()); // Enviar lista vacía en caso de error
        }

        String urlVista = "/foro.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(urlVista);
        dispatcher.forward(request, response);
    }

    /**
     * Maneja POST: Procesa la creación de un nuevo post.
     * Después de crear, recarga todos los posts (con sus comentarios) y reenvía a foro.jsp.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String titulo = request.getParameter("tituloPost");
        String contenido = request.getParameter("contenidoPost");

        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = null;
        if (session != null) {
            usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        }

        Usuario autorDelPost = null;
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Lógica para determinar el autor (Logueado o Anónimo)
        if (usuarioLogueado != null) {
            autorDelPost = usuarioLogueado;
            System.out.println("Post será creado por usuario logueado: ID=" + autorDelPost.getidUsuario());
        } else {
            System.out.println("No hay usuario logueado, intentando obtener/crear usuario anónimo con ID=" + ANONYMOUS_USER_ID);
            try {
                autorDelPost = usuarioDAO.obtenerOCrearUsuarioAnonimo(ANONYMOUS_USER_ID);
                if (autorDelPost == null) {
                    System.err.println("¡ERROR CRÍTICO! No se pudo obtener ni crear el usuario anónimo (ID=" + ANONYMOUS_USER_ID + ").");
                    request.setAttribute("errorCreacionPost", "Error del sistema: No se pudo asignar autor anónimo al post.");
                } else {
                     System.out.println("Post será creado por usuario anónimo: ID=" + autorDelPost.getidUsuario());
                }
            } catch (Exception e) {
                 System.err.println("Excepción inesperada al obtener/crear usuario anónimo (ID=" + ANONYMOUS_USER_ID + "): " + e.getMessage());
                 e.printStackTrace();
                 request.setAttribute("errorCreacionPost", "Error interno del sistema al procesar usuario anónimo.");
                 autorDelPost = null;
            }
        }

        // Validaciones
        boolean datosValidos = true;
        if (autorDelPost == null) {
             if (request.getAttribute("errorCreacionPost") == null) { // Evitar sobreescribir error más específico
                 request.setAttribute("errorCreacionPost", "No se pudo determinar el autor del post.");
             }
            datosValidos = false;
        } else if (titulo == null || titulo.trim().isEmpty() || contenido == null || contenido.trim().isEmpty()) {
             // Solo poner mensaje de error si no había ya uno por fallo de autor
            if (request.getAttribute("errorCreacionPost") == null) {
                request.setAttribute("errorCreacionPost", "El título y el contenido del post no pueden estar vacíos.");
            }
            datosValidos = false;
        }

        // Intentar crear el post
        if (datosValidos) {
            Post nuevoPost = new Post();
            nuevoPost.setId(UUID.randomUUID().toString()); // Generar ID único para el post
            nuevoPost.setTitulo(titulo.trim());
            nuevoPost.setContenido(contenido.trim());
            nuevoPost.setAutor(autorDelPost);
            // nVisualizaciones, fechaPublicacion se manejan en el DAO al insertar

            PostDAO postDAO = new PostDAO(); // Instancia para insertar
            boolean exitoInsercion = false;
            try {
                 exitoInsercion = postDAO.insertarPost(nuevoPost);
                 if (exitoInsercion) {
                     request.setAttribute("exitoCreacionPost", "¡Post publicado correctamente!");
                 } else {
                     // Solo establecer si no hay ya un error de validación o de autor
                     if (request.getAttribute("errorCreacionPost") == null) {
                        request.setAttribute("errorCreacionPost", "No se pudo publicar el post (DAO retornó false).");
                     }
                 }
            } catch (Exception e) {
                System.err.println("Error al llamar a postDAO.insertarPost en doPost: " + e.getMessage());
                e.printStackTrace();
                if (request.getAttribute("errorCreacionPost") == null) {
                    request.setAttribute("errorCreacionPost", "Error interno al guardar el post.");
                }
            }
        }

        // --- Recargar SIEMPRE la lista de posts y sus comentarios DESPUÉS de la operación ---
        // Esta sección es idéntica a la lógica de doGet para asegurar consistencia.
        PostDAO postDAOparaRecarga = new PostDAO();
        ComentarioDAO comentarioDAOparaRecarga = new ComentarioDAO();
        List<Post> listaPostsActualizada = null;
        try {
            listaPostsActualizada = postDAOparaRecarga.obtenerPosts(); // Ya calcula likes/dislikes

            if (listaPostsActualizada != null && !listaPostsActualizada.isEmpty()) {
                for (Post post : listaPostsActualizada) {
                    post.setComentarios(comentarioDAOparaRecarga.obtenerComentariosPorPost(post.getId()));
                }
            }
            request.setAttribute("postsForo", listaPostsActualizada);
        } catch (Exception e) {
             System.err.println("Error al RE-obtener posts y comentarios en doPost: " + e.getMessage());
             e.printStackTrace();
             // Guardar este error si no hay uno más específico de la creación
             if (request.getAttribute("errorCreacionPost") == null && request.getAttribute("exitoCreacionPost") == null) {
                 request.setAttribute("errorForo", "Error al actualizar la lista de posts después de la operación.");
             }
             // Asegurar que postsForo no es null para el JSP
             if (request.getAttribute("postsForo") == null) {
                 request.setAttribute("postsForo", new ArrayList<Post>());
             }
        }
        // --- Fin Recarga ---

        // Reenviar al JSP
        String urlVista = "/foro.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(urlVista);
        dispatcher.forward(request, response);
    }
}