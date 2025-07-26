package uva.ssw.entrega.controlador;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import uva.ssw.entrega.bd.EntradaDAO;
import uva.ssw.entrega.modelo.Entrada;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "ServletEliminarEntrada", urlPatterns = {"/eliminarEntradas"})
public class ServletEliminarEntrada extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] idsSeleccionados = request.getParameterValues("entradaId");
        EntradaDAO dao = new EntradaDAO();

        if (idsSeleccionados != null) {
            for (String id : idsSeleccionados) {
                try {
                    dao.eliminarEntradaPorId(Integer.parseInt(id));
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/blog");
    }
}
