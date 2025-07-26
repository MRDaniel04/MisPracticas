package uva.ssw.entrega.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.entrega.bd.PostDAO;
import uva.ssw.entrega.modelo.Usuario;

import java.io.IOException;

@WebServlet(name = "ServletVotoPost", urlPatterns = {"/votarPost"})
public class ServletVotoPost extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idPost = request.getParameter("idPost");
        String accion = request.getParameter("accion"); // "like" o "dislike"

        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        // ----- PARA MANTENER POSICIÓN: Construir URL con ancla -----
        String anclaPost = (idPost != null && !idPost.isEmpty()) ? "#post-" + idPost : "";
        String urlRedireccionForo = request.getContextPath() + "/foro" + anclaPost;
        // ----------------------------------------------------------

        if (usuarioLogueado == null) {
            System.out.println("ServletVotoPost: Usuario no logueado intentando votar.");
            session.setAttribute("errorVoto", "Debes iniciar sesión para votar."); // Guardar mensaje en sesión
            response.sendRedirect(urlRedireccionForo);
            return;
        }

        if (idPost == null || idPost.trim().isEmpty() || accion == null || accion.trim().isEmpty()) {
            System.out.println("ServletVotoPost: Parámetros 'idPost' o 'accion' faltantes.");
            session.setAttribute("errorVoto", "Información de voto inválida.");
            response.sendRedirect(urlRedireccionForo);
            return;
        }

        int tipoVoto = 0;
        if ("like".equals(accion)) {
            tipoVoto = 1;
        } else if ("dislike".equals(accion)) {
            tipoVoto = -1;
        } else {
            System.out.println("ServletVotoPost: Acción desconocida '" + accion + "'.");
            session.setAttribute("errorVoto", "Acción de voto desconocida.");
            response.sendRedirect(urlRedireccionForo);
            return;
        }

        PostDAO postDAO = new PostDAO();
PostDAO.ResultadoVoto resultadoDelVoto = postDAO.registrarActualizarVoto(idPost, usuarioLogueado.getidUsuario(), tipoVoto);

// Procesar el resultado
if (resultadoDelVoto.operacionRealizada) {
    session.setAttribute("exitoVoto", resultadoDelVoto.mensaje);
    session.removeAttribute("errorVoto");
    session.removeAttribute("alertaVotoRepetido"); // Limpiar alerta si el voto fue exitoso
} else {
    // Si la operación NO fue realizada, puede ser un error o un voto repetido
    if (resultadoDelVoto.mensaje.startsWith("Ya diste like") || resultadoDelVoto.mensaje.startsWith("Ya diste dislike")) {
        // Es un voto repetido, establecer atributo para la ALERTA en el JSP
        // Usaremos la sesión para que el mensaje sobreviva a la redirección
        session.setAttribute("alertaVotoRepetido", resultadoDelVoto.mensaje);
        session.removeAttribute("errorVoto"); // Limpiar otros errores de voto
        session.removeAttribute("exitoVoto");
    } else {
        // Es otro tipo de error
        session.setAttribute("errorVoto", resultadoDelVoto.mensaje);
        session.removeAttribute("alertaVotoRepetido");
        session.removeAttribute("exitoVoto");
    }
}

response.sendRedirect(urlRedireccionForo); // Redirigir de vuelta al foro (con ancla)
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/foro");
    }
}