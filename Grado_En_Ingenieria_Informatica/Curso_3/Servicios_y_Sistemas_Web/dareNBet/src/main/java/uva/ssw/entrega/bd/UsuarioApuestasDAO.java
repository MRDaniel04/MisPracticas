/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.bd;

import uva.ssw.entrega.modelo.UsuarioApuesta;
import uva.ssw.entrega.modelo.Usuario;
import uva.ssw.entrega.modelo.Apuesta;
import uva.ssw.entrega.modelo.OpcionApuesta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author fredi
 */
public class UsuarioApuestasDAO {

    // Inserta o actualiza; si ya existía suma el importe y actualiza la opción y la fecha
    private static final String SQL_MERGE =
        "INSERT INTO UsuarioApuesta (usuario_id, apuesta_id, opcion_id, importe) " +
        "VALUES (?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE " +
        "importe = importe + VALUES(importe), " +
        "opcion_id = VALUES(opcion_id), " +
        "fecha_apuesta = CURRENT_TIMESTAMP";

    public void merge(UsuarioApuesta ua) throws SQLException {
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_MERGE)) {
            ps.setInt       (1, ua.getUsuario().getId());
            ps.setString    (2, ua.getApuesta().getId());
            ps.setString    (3, ua.getOpcion().getId());
            ps.setBigDecimal(4, ua.getImporte());
            ps.executeUpdate();
        }
    }

    // Recupera el histórico completo de apuestas de un usuario
    public List<UsuarioApuesta> findByUsuario(int usuarioId) throws SQLException {
        String sql =
          "SELECT ua.apuesta_id, a.titulo, a.imagen, a.fechaFin, " +
          "       ua.opcion_id, o.texto, o.cuota, ua.importe, ua.fecha_apuesta " +
          "FROM UsuarioApuesta ua " +
          "  JOIN Apuesta a ON ua.apuesta_id = a.id " +
          "  JOIN OpcionApuesta o ON ua.opcion_id = o.id " +
          "WHERE ua.usuario_id = ? " +
          "ORDER BY ua.fecha_apuesta DESC";

        List<UsuarioApuesta> lista = new ArrayList<>();
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UsuarioApuesta ua = new UsuarioApuesta();

                    // Usuario
                    Usuario usr = new Usuario();
                    usr.setId(usuarioId);
                    ua.setUsuario(usr);

                    // Apuesta
                    Apuesta ap = new Apuesta();
                    ap.setId(rs.getString("apuesta_id"));
                    ap.setTitulo(rs.getString("titulo"));
                    ap.setImagen(rs.getString("imagen"));
                    Timestamp tsFin = rs.getTimestamp("fechaFin");
                    if (tsFin != null) ap.setFechaFin(new java.util.Date(tsFin.getTime()));
                    ua.setApuesta(ap);

                    // Opción de apuesta
                    OpcionApuesta op = new OpcionApuesta();
                    op.setId(rs.getString("opcion_id"));
                    op.setTexto(rs.getString("texto"));
                    op.setCuota(rs.getBigDecimal("cuota"));
                    ua.setOpcion(op);

                    // Importe y fecha de apuesta
                    ua.setImporte(rs.getBigDecimal("importe"));
                    Timestamp tsA = rs.getTimestamp("fecha_apuesta");
                    if (tsA != null) ua.setFechaApuesta(new java.util.Date(tsA.getTime()));

                    lista.add(ua);
                }
            }
        }
        return lista;
    }
}