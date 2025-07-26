/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebas;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.entrega.bd.ConnectionPool;

@WebServlet("/pruebaConexion")
public class PruebaConexionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        Connection conn = ConnectionPool.getInstance().getConnection();
        
        if (conn != null) {
            out.println("¡Conexión exitosa!");
            try {
                String sql = "SELECT 1";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    out.println("Resultado de la consulta: " + rs.getInt(1));
                } else {
                    out.println("No se obtuvo resultado en la consulta.");
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                out.println("Error al ejecutar la consulta: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConnectionPool.getInstance().freeConnection(conn);
            }
        } else {
            out.println("No se pudo obtener la conexión.");
        }
    }
}