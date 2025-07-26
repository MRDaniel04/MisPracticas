/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.bd;

import uva.ssw.entrega.modelo.Apuesta;
import uva.ssw.entrega.modelo.OpcionApuesta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;              
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class ApuestaDAO {

    // Ahora insertamos fechaPublicacion y fechaFin y tags
    private static final String SQL_INSERT_APUESTA =
        "INSERT INTO Apuesta("
      + "id, titulo, imagen, fechaPublicacion, fechaFin, tags"
      + ") VALUES (?,?,?,?,?,?)";

    private static final String SQL_INSERT_OPCION =
        "INSERT INTO OpcionApuesta(id, apuesta_id, texto, cuota, votos) VALUES (?,?,?,?,0)";

     private static final String SQL_INCREMENT_LIKES =
        "UPDATE Apuesta SET nLikes = nLikes + 1 WHERE id = ?";
    
    /**
     * Inserta una apuesta y sus opciones en una única transacción.
     */
    public void insertar(Apuesta ap, List<OpcionApuesta> opciones) throws SQLException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            con.setAutoCommit(false);

            // 1) Insertar la apuesta (incluye fechaPublicacion)
            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_APUESTA)) {
                ps.setString(1, ap.getId());
                ps.setString(2, ap.getTitulo());
                ps.setString(3, ap.getImagen());
             
                ps.setDate(4, new Date(ap.getFechaPublicacion().getTime()));
                ps.setDate(5, new Date(ap.getFechaFin().getTime()));
                ps.setString(6, ap.getTags());
                ps.executeUpdate();
            }

            // 2) Insertar cada opción
            try (PreparedStatement ps = con.prepareStatement(SQL_INSERT_OPCION)) {
                for (OpcionApuesta op : opciones) {
                    ps.setString(1, UUID.randomUUID().toString());
                    ps.setString(2, ap.getId());
                    ps.setString(3, op.getTexto());
                    ps.setBigDecimal(4, op.getCuota());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            pool.freeConnection(con);
        }
    }
    
     /**
     * Incrementa en 1 el contador de “me gusta” de la apuesta indicada.
     */
    public void incrementLikes(String apuestaId) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectionPool.getInstance().getConnection();
            ps  = con.prepareStatement(SQL_INCREMENT_LIKES);
            ps.setString(1, apuestaId);
            ps.executeUpdate();
        } finally {
            if (ps  != null) ps.close();
            if (con != null) ConnectionPool.getInstance().freeConnection(con);
        }
    }
    
    /**
    * Incrementa en 1 el contador de “dislikes” de la apuesta indicada.
    */
    public void incrementDislikes(String apuestaId) throws SQLException {
       String sql = "UPDATE Apuesta SET nDislikes = nDislikes + 1 WHERE id = ?";
       try (Connection con = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, apuestaId);
            ps.executeUpdate();
        }
    }

    
    public List<Apuesta> findAll() throws SQLException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        List<Apuesta> lista = new ArrayList<>();
        String sql = "SELECT * FROM Apuesta ORDER BY fechaPublicacion DESC";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Apuesta ap = new Apuesta();
                ap.setId(rs.getString("id"));
                ap.setNVisualizaciones(rs.getInt("nVisualizaciones"));
                ap.setNLikes(rs.getInt("nLikes"));
                ap.setNDislikes(rs.getInt("nDislikes"));
                ap.setNCreditosTotal(rs.getInt("nCreditosTotal"));
                ap.setTitulo(rs.getString("titulo"));
                ap.setDescripcion(rs.getString("descripcion"));
                ap.setImagen(rs.getString("imagen"));
                ap.setFechaPublicacion(rs.getDate("fechaPublicacion"));
                ap.setFechaFin(rs.getDate("fechaFin"));
                ap.setTags(rs.getString("tags"));
                lista.add(ap);
            }
        } finally {
            pool.freeConnection(con);
        }
        return lista;
    }  
}