    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package uva.ssw.entrega.controlador;

    import uva.ssw.entrega.bd.UsuarioApuestasDAO;
    import uva.ssw.entrega.modelo.Usuario;

    import jakarta.servlet.RequestDispatcher;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.*;

    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.List;
import uva.ssw.entrega.modelo.UsuarioApuesta;

    /**
     *
     * @author fredi
     */
    @WebServlet("/mis_apuestas")
public class ServletMisApuestas extends HttpServlet {
    private final UsuarioApuestasDAO uaDao = new UsuarioApuestasDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Comprobar sesión
        HttpSession session = req.getSession(false);
        Usuario usr = (session != null)
                    ? (Usuario) session.getAttribute("usuarioLogueado")
                    : null;
        if (usr == null) {
            req.setAttribute("errorSesion", "Debes iniciar sesión para ver tus apuestas.");
            RequestDispatcher rd = req.getRequestDispatcher("/apuestas.jsp");
            rd.forward(req, resp);
            return;
        }

        // 2) Recuperar listado completo de UsuarioApuesta
        List<UsuarioApuesta> misAps;
        try {
            misAps = uaDao.findByUsuario(usr.getId());
        } catch (SQLException ex) {
            throw new ServletException("Error al cargar tus apuestas", ex);
        }

        // 3) Pasar al JSP
        req.setAttribute("misApuestas", misAps);
        RequestDispatcher rd = req.getRequestDispatcher("/mis_apuestas.jsp");
        rd.forward(req, resp);
    }
}
