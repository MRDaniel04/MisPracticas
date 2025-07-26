package uva.ssw.entrega.controlador;


import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uva.ssw.entrega.bd.ConnectionPool;
import uva.ssw.entrega.modelo.Usuario;

@WebServlet(name="ServletInicio",urlPatterns={"/inicio"})
public class ServletInicio extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
         response.sendRedirect("formularioRegistro.jsp");
    }
}

