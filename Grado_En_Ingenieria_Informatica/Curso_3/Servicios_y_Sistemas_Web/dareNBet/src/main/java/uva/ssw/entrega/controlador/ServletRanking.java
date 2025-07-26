package uva.ssw.entrega.controlador;

import uva.ssw.entrega.bd.RankingDao; // Ajustado al nombre de clase correcto // Asegúrate de usar exactamente el mismo nombre de clase
import uva.ssw.entrega.modelo.Apuesta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ServletRanking", urlPatterns = {"/ranking"})
public class ServletRanking extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Instancia del DAO
        RankingDao dao = new RankingDao();
        List<Apuesta> top10 = dao.obtenerTop10PorLikes();

        // Poner en request
        request.setAttribute("top10Likes", top10);
        
        
        request.getRequestDispatcher("ranking.jsp").forward(request, response);
    }
}