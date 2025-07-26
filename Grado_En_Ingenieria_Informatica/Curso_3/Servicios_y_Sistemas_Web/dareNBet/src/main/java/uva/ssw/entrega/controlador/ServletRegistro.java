package uva.ssw.entrega.controlador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Importar HttpSession
import uva.ssw.entrega.bd.UsuarioDAO;
import uva.ssw.entrega.modelo.Usuario;

import java.io.IOException;
import java.sql.SQLException; // Importar SQLException

/**
 * Servlet para el registro de usuarios y inicio de sesión automático tras registro.
 * Versión SIMPLIFICADA sin validaciones extra ni hashing.
 * @author ainhoa (con modificaciones)
 */
@WebServlet(name = "ServletRegistro", urlPatterns = {"/ServletRegistro"})
public class ServletRegistro extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO dao = new UsuarioDAO();

    /**
     * Maneja GET: Redirige a la página de registro estática o JSP.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/formularioRegistro.html");
    }

    /**
     * Maneja POST: Procesa el formulario de registro.
     * Valida coincidencias básicas, inserta en BD y, si tiene éxito, inicia sesión.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Establecer codificación
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 2. Obtener parámetros del formulario
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellidos");
        String username = request.getParameter("username");
        String edadStr = request.getParameter("edad");
        String email = request.getParameter("email");
        String confirmEmail = request.getParameter("ConfirmEmail");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("ConfirmPassword");
        String telefono = request.getParameter("numeroTelefono");

        String vistaError = "/error_Registro.jsp"; // Vista para mostrar errores de registro
        String vistaExitoYLogin = "/configuraciones.jsp"; // URL para redirigir tras éxito

        // 3. Validaciones MÍNIMAS (las que tenías originalmente)
        // Validar campos requeridos implícitamente por 'required' en HTML,
        // pero buena práctica validar aquí también por si algo falla.
        if (nombre == null || apellido == null || username == null || edadStr == null ||
            email == null || confirmEmail == null || password == null || confirmPassword == null ||
            telefono == null) {
             request.setAttribute("error", "Faltan campos obligatorios (error inesperado).");
             request.getRequestDispatcher(vistaError).forward(request, response);
             return;
        }

        // Validar coincidencia de emails
        if (!email.equals(confirmEmail)) {
            request.setAttribute("error", "Los correos electrónicos no coinciden.");
            request.getRequestDispatcher(vistaError).forward(request, response);
            return;
        }

        // Validar coincidencia de contraseñas
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher(vistaError).forward(request, response);
            return;
        }

        // Convertir edad (asumiendo que la validación HTML ya forzó a ser número)
        int edad = 0; // Valor por defecto en caso de error, aunque no debería pasar
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
             // Si llega aquí, algo falló en la validación HTML del navegador
             request.setAttribute("error", "La edad proporcionada no es un número válido.");
             request.getRequestDispatcher(vistaError).forward(request, response);
             return;
        }


        // 4. Crear el objeto Usuario (SIN HASHING)
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(username); // Usar trim() es buena práctica
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setEdad(edad);
        nuevoUsuario.setCorreoElectronico(email);
        nuevoUsuario.setNumeroTelefono(telefono);
        nuevoUsuario.setNCreditos(0); // Créditos iniciales

        // --- SIN HASHING (¡INSEGURO!) ---
        nuevoUsuario.setContrasena(password); // Guardar contraseña en texto plano

        // 5. Insertar en la Base de Datos y Manejar Error de Duplicados
        try {
            dao.insertar(nuevoUsuario); // Intentar insertar
            System.out.println("Registro exitoso para: " + nuevoUsuario.getNombreUsuario());

            // ----- INICIO: Lógica de inicio de sesión automático -----
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", nuevoUsuario); // ¡USA EL MISMO NOMBRE QUE EN LOGIN!
            session.setMaxInactiveInterval(30 * 60);
            System.out.println("Sesión iniciada automáticamente para: " + nuevoUsuario.getNombreUsuario());

            // 6. Redirigir a la página de configuración
             response.sendRedirect(request.getContextPath() + vistaExitoYLogin);
            // ----- FIN: Lógica de inicio de sesión automático -----

        } catch (SQLException e) {
            // Error SQL durante la inserción. Podría ser por usuario/email duplicado
            // o cualquier otro problema de BD.
            System.err.println("Error SQL durante el registro: " + e.getMessage());
            e.printStackTrace();
            // Mensaje de error más genérico, pero podrías intentar analizar el código de error SQL
            // para diferenciar entre duplicado y otros errores si es necesario.
            request.setAttribute("error", "Error al guardar en la base de datos. El nombre de usuario o email podrían ya existir.");
            request.getRequestDispatcher(vistaError).forward(request, response);
        } catch (Exception e) {
            // Captura para otros errores inesperados durante la inserción
            System.err.println("Error inesperado durante el registro: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error inesperado durante el registro.");
            request.getRequestDispatcher(vistaError).forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet simplificado para registrar usuarios e iniciar sesión automáticamente.";
    }
}