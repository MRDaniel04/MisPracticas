/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.EntradaDAO;
import uva.ssw.entrega.modelo.Entrada;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletBlog", urlPatterns = {"/blog"})
public class ServletBlog extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntradaDAO dao = new EntradaDAO();
        List<Entrada> entradas = dao.obtenerEntradas();
        request.setAttribute("entradas", entradas);
        request.getRequestDispatcher("blog.jsp").forward(request, response);
    }
}

