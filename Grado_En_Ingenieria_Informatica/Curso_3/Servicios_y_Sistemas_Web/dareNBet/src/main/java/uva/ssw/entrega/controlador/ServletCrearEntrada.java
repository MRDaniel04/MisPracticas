package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.EntradaDAO;
import uva.ssw.entrega.modelo.Entrada;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet que procesa la creación de una nueva entrada de blog.
 */
@WebServlet(name = "ServletCrearEntrada", urlPatterns = {"/crearEntradaBlog"})
public class ServletCrearEntrada extends HttpServlet {

    private final EntradaDAO dao = new EntradaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redirige al formulario en caso de GET
        resp.sendRedirect(req.getContextPath() + "/crearEntradaBlog.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Codificación
        if (req.getCharacterEncoding() == null) {
            req.setCharacterEncoding("UTF-8");
        }

        // 2) Recogida de parámetros del formulario
        String icono = req.getParameter("icono");
        String titulo     = req.getParameter("titulo");
        String fechaRaw   = req.getParameter("fecha");  // formato: yyyy-MM-dd
        String descripcion = req.getParameter("descripcion");

        // 3) Conversión de fecha: de String a java.util.Date
        Date fechaPublicacion;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fechaPublicacion = sdf.parse(fechaRaw);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Formato de fecha inválido", e);
        }

        // 4) Crear objeto Entrada
        Entrada entrada = new Entrada();
        entrada.setTitulo(titulo);
        entrada.setFechaPublicacion(fechaPublicacion);
        descripcion = descripcion.stripLeading();
        entrada.setDescripcion(descripcion);
        entrada.setIcono(icono);

        // 5) Insertar en BD usando DAO
        try {
            boolean insertado = dao.insertarEntrada(entrada);

            // 6) Redirigir al blog si fue exitoso
            if (insertado) {
                resp.sendRedirect(req.getContextPath() + "/blog");
            } else {
                req.setAttribute("error", "No se pudo crear la entrada.");
                req.getRequestDispatcher("crearEntradaBlog.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException("Error al insertar la entrada en la base de datos", e);
        }
    }
}
