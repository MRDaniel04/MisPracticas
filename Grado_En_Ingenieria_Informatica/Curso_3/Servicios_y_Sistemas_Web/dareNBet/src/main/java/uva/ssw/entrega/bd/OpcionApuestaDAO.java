/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import uva.ssw.entrega.modelo.OpcionApuesta;

public class OpcionApuestaDAO {

    private static final String SQL_FIND_BY_APUESTA =
        "SELECT id, apuesta_id, texto, cuota, votos "
      + "FROM OpcionApuesta "
      + "WHERE apuesta_id = ?";

    /**
     * Obtiene la lista de opciones de una apuesta dado su ID.
     *
     * @param apuestaId Identificador de la apuesta
     * @return Lista de OpcionApuesta (puede estar vacía si no hay opciones)
     * @throws SQLException Si ocurre un error de acceso a la base de datos
     */
    public List<OpcionApuesta> findByApuestaId(String apuestaId) throws SQLException {
        List<OpcionApuesta> lista = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectionPool.getInstance().getConnection();
            ps  = con.prepareStatement(SQL_FIND_BY_APUESTA);
            ps.setString(1, apuestaId);
            rs  = ps.executeQuery();
            while (rs.next()) {
                OpcionApuesta op = new OpcionApuesta();
                op.setId(        rs.getString("id"));
                op.setApuestaId( rs.getString("apuesta_id"));
                op.setTexto(     rs.getString("texto"));
                op.setCuota(     rs.getBigDecimal("cuota"));
                op.setVotos(     rs.getInt("votos"));
                lista.add(op);
            }
        } finally {
            if (rs  != null) rs.close();
            if (ps  != null) ps.close();
            if (con != null) ConnectionPool.getInstance().freeConnection(con);
        }
        return lista;
    }
}