/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package uva.ssw.entrega.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.entrega.bd.UsuarioDAO;


@WebServlet(name = "ServletConfiguraciones", urlPatterns = {"/configuraciones"})
public class ServletConfiguraciones extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int usuarioId = 1; 

        try {
            int creditos = usuarioDAO.obtenerCreditos(usuarioId);
            request.setAttribute("creditos", creditos);
        } catch (Exception e) {
            request.setAttribute("error", "No se pudieron cargar los créditos");
        }

        request.getRequestDispatcher("/configuraciones.jsp").forward(request, response);
    }
}
