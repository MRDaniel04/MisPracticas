/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.ApuestaDAO;
import uva.ssw.entrega.bd.OpcionApuestaDAO;
import uva.ssw.entrega.modelo.Apuesta;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="ServletListarApuestas", urlPatterns={"/apuestas"})
public class ServletListarApuestas extends HttpServlet {
    private final ApuestaDAO dao = new ApuestaDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        List<Apuesta> apuestas = null;
        String error = null;
        try {
            apuestas = dao.findAll();
            OpcionApuestaDAO opcionDao = new OpcionApuestaDAO();
            for (Apuesta ap : apuestas) {
                ap.setOpciones(opcionDao.findByApuestaId(ap.getId()));
            }
            request.setAttribute("apuestas", apuestas);
        } catch (SQLException e) {
            error = e.getMessage();
            request.setAttribute("error", error);
        }
        // Forward al JSP en minúsculas
        RequestDispatcher rd = request.getRequestDispatcher("/apuestas.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
