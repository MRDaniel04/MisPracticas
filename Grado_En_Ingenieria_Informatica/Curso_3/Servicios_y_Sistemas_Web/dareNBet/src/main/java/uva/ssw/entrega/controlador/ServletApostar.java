/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.UsuarioApuestasDAO;
import uva.ssw.entrega.bd.UsuarioDAO;
import uva.ssw.entrega.modelo.UsuarioApuesta;
import uva.ssw.entrega.modelo.Usuario;
import uva.ssw.entrega.modelo.Apuesta;
import uva.ssw.entrega.modelo.OpcionApuesta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.math.BigDecimal;


/**
*
* @author fredi
*/
@WebServlet("/apostar")
public class ServletApostar extends HttpServlet {
    private final UsuarioApuestasDAO uaDao = new UsuarioApuestasDAO();
    private final UsuarioDAO usuarioDao   = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Usuario usr = (session != null)
                    ? (Usuario) session.getAttribute("usuarioLogueado")
                    : null;
        if (usr == null) {
            req.setAttribute("errorSesion", "Debes iniciar sesión para apostar.");
            RequestDispatcher rd = req.getRequestDispatcher("/apuestas.jsp");
            rd.forward(req, resp);
            return;
        }

        // Leer y validar importe
        BigDecimal importe;
        try {
            importe = new BigDecimal(req.getParameter("importe"));
            if (importe.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NumberFormatException();
            }
        } catch (Exception ex) {
            req.setAttribute("errorApuesta", "Importe inválido.");
            RequestDispatcher rd = req.getRequestDispatcher("/apuestas.jsp");
            rd.forward(req, resp);
            return;
        }

        try {
            // 1) Comprobar créditos
            int creditos = usuarioDao.obtenerCreditos(usr.getId());
            if (importe.intValue() > creditos) {
                req.setAttribute("errorApuesta", "No tienes créditos suficientes (" + creditos + ").");
                RequestDispatcher rd = req.getRequestDispatcher("/apuestas.jsp");
                rd.forward(req, resp);
                return;
            }

            // 2) Restar créditos
            usuarioDao.updateCreditos(usr.getId(), -importe.intValue());

            // 3) Actualizar sesión con créditos nuevos
            usr.setNCreditos(creditos - importe.intValue());
            session.setAttribute("usuarioLogueado", usr);

            // 4) Crear y registrar UsuarioApuesta
            UsuarioApuesta ua = new UsuarioApuesta();
            ua.setUsuario(usr);

            Apuesta a = new Apuesta();
            a.setId(req.getParameter("apuestaId"));
            ua.setApuesta(a);

            OpcionApuesta op = new OpcionApuesta();
            op.setId(req.getParameter("opcionId"));
            ua.setOpcion(op);

            ua.setImporte(importe);

            uaDao.merge(ua);

            // 5) Redirigir manteniendo hash para no desplazar vista
            String apuestaId = req.getParameter("apuestaId");
            resp.sendRedirect(req.getContextPath() + "/apuestas#" + apuestaId);
        } catch (Exception e) {
            throw new ServletException("Error al registrar apuesta", e);
        }
    }
}