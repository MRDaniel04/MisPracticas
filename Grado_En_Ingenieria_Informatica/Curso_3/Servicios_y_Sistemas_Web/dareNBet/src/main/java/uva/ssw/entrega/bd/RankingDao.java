package uva.ssw.entrega.bd;

import uva.ssw.entrega.modelo.Apuesta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RankingDao {

    /** Devuelve las 10 apuestas con más likes. */
    public List<Apuesta> obtenerTop10PorLikes() {
        String sql =
            "SELECT id, titulo, nLikes " +
            "FROM Apuesta " +
            "ORDER BY nLikes DESC " +
            "LIMIT 10";
        List<Apuesta> lista = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Apuesta a = new Apuesta();
                a.setId(rs.getString("id"));
                a.setTitulo(rs.getString("titulo"));
                a.setNLikes(rs.getInt("nLikes"));
                lista.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}