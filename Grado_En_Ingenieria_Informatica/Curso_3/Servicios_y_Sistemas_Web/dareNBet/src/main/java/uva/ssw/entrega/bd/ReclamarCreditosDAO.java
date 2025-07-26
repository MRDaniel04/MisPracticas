/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uva.ssw.entrega.bd;


import java.sql.*;
import uva.ssw.entrega.bd.ConnectionPool;

public class ReclamarCreditosDAO {
        private static final String SQL_SELECT =
        "SELECT nCreditos, ultimaRecompensa FROM Usuario WHERE id = ?";

    private static final String SQL_UPDATE =
        "UPDATE Usuario SET nCreditos = ?, ultimaRecompensa = ? WHERE id = ?";

    public static class ResultadoReclamo {
        public boolean puedeReclamar;
        public int nuevosCreditos;
        public long horasRestantes;
    }


    public ResultadoReclamo procesarReclamo(int usuarioId) throws SQLException {
    Connection con = null;
    ResultadoReclamo resultado = new ResultadoReclamo();

    try {
        con = ConnectionPool.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(SQL_SELECT);
        ps.setInt(1, usuarioId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            Timestamp ultimaRecompensa = rs.getTimestamp("ultimaRecompensa");
            int creditosActuales = rs.getInt("nCreditos");
            if (rs.wasNull()) {
                creditosActuales = 0;
            }

            boolean puedeReclamar = false;

            if (ultimaRecompensa == null) {
                puedeReclamar = true;
            } else {
                long tiempoTranscurrido = ahora.getTime() - ultimaRecompensa.getTime();
                puedeReclamar = tiempoTranscurrido >= 86400000;
            }

            if (puedeReclamar) {
                int nuevosCreditos = creditosActuales + 100;

                PreparedStatement update = con.prepareStatement(SQL_UPDATE);
                update.setInt(1, nuevosCreditos);
                update.setTimestamp(2, ahora);
                update.setInt(3, usuarioId);
                update.executeUpdate();
                update.close();

                resultado.puedeReclamar = true;
                resultado.nuevosCreditos = nuevosCreditos;
            } else {
                long faltaMs = 86400000 - (ahora.getTime() - ultimaRecompensa.getTime());
                resultado.puedeReclamar = false;
                resultado.horasRestantes = faltaMs / 3600000;
            }
        }

        rs.close();
        ps.close();
    } finally {
        if (con != null) ConnectionPool.getInstance().freeConnection(con);
    }

    return resultado;
}


}
