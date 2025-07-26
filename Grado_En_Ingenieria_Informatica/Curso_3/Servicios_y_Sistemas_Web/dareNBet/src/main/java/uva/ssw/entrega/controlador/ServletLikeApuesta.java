/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.ApuestaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="ServletLikeApuesta", urlPatterns={"/likeApuesta"})
public class ServletLikeApuesta extends HttpServlet {
    private final ApuestaDAO apuestaDao = new ApuestaDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/apuestas");
            return;
        }
        try {
            apuestaDao.incrementLikes(id);
        } catch (SQLException e) {
            throw new ServletException("Error al incrementar likes", e);
        }
        // Redirige manteniendo el hash para no desplazar la vista
        resp.sendRedirect(req.getContextPath() + "/apuestas#" + id);
    }
}