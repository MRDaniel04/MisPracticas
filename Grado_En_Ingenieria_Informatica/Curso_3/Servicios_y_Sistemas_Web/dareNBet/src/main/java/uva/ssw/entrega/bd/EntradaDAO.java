/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.bd;

import uva.ssw.entrega.modelo.Entrada;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntradaDAO {

    // Método para insertar una entrada en la base de datos
    public boolean insertarEntrada(Entrada entrada) {
        String sql = "INSERT INTO Entrada (titulo, fechaPublicacion, descripcion,icono) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entrada.getTitulo());
            pstmt.setDate(2, new java.sql.Date(entrada.getFechaPublicacion().getTime()));
            pstmt.setString(3, entrada.getDescripcion());
            pstmt.setString(4, entrada.getIcono());

            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
  // Método para obtener la lista de entradas, ordenadas por fecha (descendente)
public List<Entrada> obtenerEntradas() {
    List<Entrada> lista = new ArrayList<>();
    String sql = "SELECT id, titulo, fechaPublicacion, descripcion, icono FROM Entrada ORDER BY fechaPublicacion DESC";
    
    try (Connection conn = ConnectionPool.getInstance().getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String titulo = rs.getString("titulo");
            Date fecha = rs.getDate("fechaPublicacion");
            String descripcion = rs.getString("descripcion");
            String icono = rs.getString("icono");

            Entrada entrada = new Entrada(id, titulo, fecha, descripcion);
            entrada.setIcono(icono); // <- aquí añadimos el icono
            lista.add(entrada);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return lista;
}

    
    
    public boolean eliminarEntradaPorId(int id) {
    String sql = "DELETE FROM Entrada WHERE id = ?";
    try (Connection conn = ConnectionPool.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }

}
