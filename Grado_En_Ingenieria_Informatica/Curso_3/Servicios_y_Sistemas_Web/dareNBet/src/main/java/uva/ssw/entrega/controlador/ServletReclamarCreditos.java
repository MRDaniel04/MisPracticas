package uva.ssw.entrega.controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uva.ssw.entrega.bd.ReclamarCreditosDAO;
import uva.ssw.entrega.modelo.Usuario; 
import java.io.IOException;

@WebServlet(name = "ServletReclamarCreditos", urlPatterns = {"/reclamarCreditos"})
public class ServletReclamarCreditos extends HttpServlet {

    private final ReclamarCreditosDAO dao = new ReclamarCreditosDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    if (request.getCharacterEncoding() == null) {
        request.setCharacterEncoding("UTF-8");
    }

    try {
       
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            request.setAttribute("error", "Usuario no encontrado en la sesión.");
            request.getRequestDispatcher("reclamarCreditos.jsp").forward(request, response);
            return;
        }

        int usuarioId = usuario.getidUsuario();

        ReclamarCreditosDAO.ResultadoReclamo resultado = dao.procesarReclamo(usuarioId);

        if (resultado.puedeReclamar) {
            request.setAttribute("estado", "ok");
            request.setAttribute("creditos", resultado.nuevosCreditos);
            
            usuario.setNCreditos(resultado.nuevosCreditos);
            session.setAttribute("usuario", usuario);

            
        } else {
            request.setAttribute("estado", "espera");
            request.setAttribute("horasRestantes", resultado.horasRestantes);
        }

    } catch (Exception e) {
        request.setAttribute("error", "Error al procesar el reclamo: " + e.getMessage());
    }

    request.getRequestDispatcher("reclamarCreditos.jsp").forward(request, response);
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
