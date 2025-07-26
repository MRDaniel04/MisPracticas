/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.ApuestaDAO;
import uva.ssw.entrega.modelo.Apuesta;
import uva.ssw.entrega.modelo.OpcionApuesta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;        
import java.util.List;
import java.util.UUID;


@WebServlet(
    name = "ServletCrearApuesta",
    urlPatterns = {"/crearApuesta"}     
)
public class ServletCrearApuesta extends HttpServlet {

    private final ApuestaDAO dao = new ApuestaDAO();

    /* ========== GET ⇒ mostrar formulario ========== */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

       
        req.getRequestDispatcher("/crearApuesta.jsp")
           .forward(req, resp);
    }

    /* ========== POST ⇒ procesar formulario ========== */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // ----- 1. recoger parámetros -----------------
        String titulo   = req.getParameter("titulo");
        String imagen   = req.getParameter("imagen");
        String fechaRaw = req.getParameter("fecha");
        String tags     = req.getParameter("tags");

        java.sql.Date sqlFin = java.sql.Date.valueOf(fechaRaw.substring(0,10));
        Date fechaFin  = new Date(sqlFin.getTime());

        Apuesta ap = new Apuesta();
        ap.setId(UUID.randomUUID().toString());
        ap.setTitulo(titulo);
        ap.setImagen(imagen);
        ap.setFechaFin(fechaFin);
        ap.setFechaPublicacion(new Date());
        ap.setTags(tags);

        List<OpcionApuesta> opciones = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String txt   = req.getParameter("opcion" + i);
            String cuota = req.getParameter("cuota"  + i);
            if (txt != null && !txt.isBlank()) {
                opciones.add(new OpcionApuesta(
                    UUID.randomUUID().toString(),
                    ap.getId(),
                    txt.trim(),
                    new BigDecimal(
                        (cuota == null || cuota.isBlank()) ? "0" : cuota
                    )
                ));
            }
        }

        // ----- 2. persistir y redirigir ---------------
        try {
            dao.insertar(ap, opciones);
            resp.sendRedirect(req.getContextPath() + "/apuestas");
        } catch (Exception e) {
            throw new ServletException("Error creando la apuesta", e);
        }
    }
}
