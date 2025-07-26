package uva.ssw.entrega.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.entrega.bd.UsuarioDAO;
import uva.ssw.entrega.modelo.Usuario;

import java.io.IOException;

@WebServlet(name = "ServletLogin", urlPatterns = {"/login"}) 
public class ServletLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  
        response.sendRedirect(request.getContextPath() + "/inicio.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        
        String loginPageURL = request.getContextPath() + "/";
       
        String successRedirectURL = request.getContextPath() + "/configuraciones";

        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Intento de login con campos vacíos.");
            response.sendRedirect(loginPageURL); 
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioAutenticado = null;

        try {
            usuarioAutenticado = usuarioDAO.autenticarUsuario(username.trim(), password);

            if (usuarioAutenticado != null) {
               
                HttpSession session = request.getSession(true);
            
                session.setAttribute("usuarioLogueado", usuarioAutenticado);
                session.setMaxInactiveInterval(30 * 60);

                System.out.println("Login exitoso para usuario: " + username + ". Redirigiendo a " + successRedirectURL);
               
                response.sendRedirect(successRedirectURL);

            } else {
              
                System.err.println("Error durante el proceso de login para usuario: " + username);
                System.out.println("Login fallido para usuario: " + username + ". Redirigiendo a " + loginPageURL);
                response.sendRedirect(loginPageURL);
            }

        } catch (Exception e) {
        
            System.err.println("Error durante el proceso de login para usuario: " + username);
            e.printStackTrace();
            response.sendRedirect(loginPageURL); 
        }
    }
}