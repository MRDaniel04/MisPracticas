package uva.ssw.entrega.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import uva.ssw.entrega.modelo.Usuario;
// Importa la librería BCrypt si la vas a usar para hashing (recomendado)
// import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    // --- SQL Statements ACTUALIZADOS (sin DNI) ---

    

    private static final String SQL_INSERT_USUARIO = "INSERT INTO Usuario " +
        "(nombreUsuario, nombre, apellido, edad, contraseña, correoElectronico, numeroTelefono, nCreditos, imagen, fechaInscripcion, isAdmin) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    
    private static final String SQL_INSERT_ANONIMO_CON_ID = "INSERT INTO Usuario " +
        "(id, nombreUsuario, nombre, apellido, edad, contraseña, correoElectronico, numeroTelefono, nCreditos, imagen, fechaInscripcion, isAdmin) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";



    // SELECT por ID (sin DNI)
    private static final String SQL_SELECT_BY_ID = "SELECT id, nombreUsuario, nombre, apellido, edad, contraseña, correoElectronico, " +
    "numeroTelefono, nCreditos, imagen, fechaInscripcion, ultimaRecompensa, isAdmin " +
    "FROM Usuario WHERE id = ?";

    private static final String SQL_SELECT_FOR_AUTH_BY_USERNAME = "SELECT id, nombreUsuario, nombre, apellido, edad, contraseña, correoElectronico, " +
        "numeroTelefono, nCreditos, imagen, fechaInscripcion, ultimaRecompensa, isAdmin " +
        "FROM Usuario WHERE nombreUsuario = ?";


    // SELECT para créditos (sin cambios)
    private static final String SQL_SELECT_CREDITOS = "SELECT nCreditos FROM Usuario WHERE id = ?";

    // SELECTS para validación de existencia (sin cambios)
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM Usuario WHERE correoElectronico = ?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM Usuario WHERE nombreUsuario = ?";



    // --- Método insertar (ACTUALIZADO sin DNI) ---
    public void insertar(Usuario user) throws SQLException {
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection con = null;
    PreparedStatement ps = null;

    if (user == null) {
        throw new IllegalArgumentException("El usuario a insertar no puede ser nulo.");
    }

    try {
        con = pool.getConnection();
        con.setAutoCommit(false);

        ps = con.prepareStatement(SQL_INSERT_USUARIO, PreparedStatement.RETURN_GENERATED_KEYS);

        // Establecer parámetros (10 en total)
        ps.setString(1, user.getNombreUsuario());
        ps.setString(2, user.getNombre());
        ps.setString(3, user.getApellido());
        ps.setInt(4, user.getEdad());
        ps.setString(5, user.getContrasena());
        ps.setString(6, user.getCorreoElectronico());
        ps.setString(7, user.getNumeroTelefono());
        ps.setInt(8, user.getNCreditos());
        ps.setString(9, user.getImagen());
        ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
        ps.setBoolean(11, user.isAdmin());

        ps.executeUpdate();

        // 🔥 Obtener ID generado automáticamente
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            int idGenerado = generatedKeys.getInt(1);
            user.setidUsuario(idGenerado); // ✅ Asignar el ID al objeto usuario
            System.out.println("Usuario insertado con ID: " + idGenerado);
        }

        con.commit();
    } catch (SQLException ex) {
        if (con != null) {
            try { con.rollback(); } catch (SQLException e) { e.printStackTrace(); }
        }
        throw ex;
    } finally {
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        if (con != null) pool.freeConnection(con);
    }
}


     // --- Método obtenerUsuarioPorId (ACTUALIZADO sin DNI) ---
    public Usuario obtenerUsuarioPorId(int usuarioId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;
        boolean esAdmin = false;

        try {
            connection = pool.getConnection();
            if (connection == null) {
                System.err.println("DAO Error: No se pudo obtener conexión.");
                return null;
            }

            ps = connection.prepareStatement(SQL_SELECT_BY_ID); // SQL sin DNI
            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                // Mapear columnas (sin DNI)
                usuario.setidUsuario(rs.getInt("id"));
                usuario.setNombreUsuario(rs.getString("nombreUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setEdad(rs.getInt("edad"));
                usuario.setCorreoElectronico(rs.getString("correoElectronico"));
                // Se salta DNI
                usuario.setNumeroTelefono(rs.getString("numeroTelefono"));
                usuario.setNCreditos(rs.getInt("nCreditos"));
                usuario.setImagen(rs.getString("imagen"));

                Timestamp fechaInscDb = rs.getTimestamp("fechaInscripcion");
                if (fechaInscDb != null) usuario.setFechaInscripcion(new java.util.Date(fechaInscDb.getTime()));

                Timestamp ultimaRecompensaDb = rs.getTimestamp("ultimaRecompensa");
                if (ultimaRecompensaDb != null) usuario.setUltimaRecompensa(ultimaRecompensaDb);
                
                esAdmin = rs.getBoolean("isAdmin");
                usuario.setAdmin(esAdmin);
                

            }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener usuario por ID (" + usuarioId + "): " + e.getMessage());
            e.printStackTrace();
            usuario = null;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) pool.freeConnection(connection);
        }
        return usuario;
    }

    // --- Método obtenerOCrearUsuarioAnonimo (ACTUALIZADO sin DNI) ---
    public synchronized Usuario obtenerOCrearUsuarioAnonimo(int idAnonimo) {
        Usuario anonimo = obtenerUsuarioPorId(idAnonimo); // Usa la versión actualizada de obtenerUsuarioPorId
        if (anonimo != null) {
            System.out.println("Usuario anónimo (ID=" + idAnonimo + ") encontrado.");
            return anonimo;
        }

        System.out.println("Usuario anónimo (ID=" + idAnonimo + ") no encontrado. Intentando crear...");
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        boolean creadoExitosamente = false;

        try {
            con = pool.getConnection();
            con.setAutoCommit(false);

            ps = con.prepareStatement(SQL_INSERT_ANONIMO_CON_ID); // SQL con 11 placeholders

            // Datos por defecto (sin DNI)
            String nombreUsuarioAnonimo = "anonimo" + idAnonimo;
            String nombreAnonimo = "Usuario";
            String apellidoAnonimo = "Anónimo";
            int edadAnonima = 0;
            String passAnonima = "*";
            String emailAnonimo = "anonimo" + idAnonimo + "@darenbet.local";
            // No hay DNI
            String telAnonimo = null;
            int creditosAnonimo = 0;
            String imagenAnonima = null;
            Timestamp fechaActual = new Timestamp(System.currentTimeMillis());

            // Establecer parámetros en ORDEN según SQL_INSERT_ANONIMO_CON_ID (11 parámetros)
            ps.setInt(1, idAnonimo);            // Posición 1
            ps.setString(2, nombreUsuarioAnonimo); // Posición 2
            ps.setString(3, nombreAnonimo);       // Posición 3
            ps.setString(4, apellidoAnonimo);     // Posición 4
            ps.setInt(5, edadAnonima);            // Posición 5
            ps.setString(6, passAnonima);         // Posición 6
            ps.setString(7, emailAnonimo);        // Posición 7
            // Se salta DNI
            ps.setString(8, telAnonimo);       // Posición 8: numero_telefono
            ps.setInt(9, creditosAnonimo);     // Posición 9: nCreditos
            ps.setString(10, imagenAnonima);      // Posición 10: imagen
            ps.setTimestamp(11, fechaActual);     // Posición 11: fechaInscripcion
            ps.setBoolean(12, false);
            
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 1) {
                con.commit();
                creadoExitosamente = true;
                System.out.println("Usuario anónimo (ID=" + idAnonimo + ") creado.");
            } else {
                con.rollback();
                System.err.println("Inserción anónimo afectó " + filasAfectadas + " filas. Rollback.");
            }

        } catch (SQLException ex) {
            System.err.println("Error SQL al crear usuario anónimo (ID=" + idAnonimo + "): " + ex.getMessage());
             if (con != null) {
                try { con.rollback(); } catch (SQLException e) { e.printStackTrace(); }
             }
        } finally {
             try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
             if (con != null) pool.freeConnection(con);
        }

        // Re-intentar obtener después de crear
        if (creadoExitosamente || anonimo == null) {
            System.out.println("Re-intentando obtener anónimo (ID=" + idAnonimo + ") post-creación.");
            anonimo = obtenerUsuarioPorId(idAnonimo); // Usa la versión actualizada de obtenerUsuarioPorId
        }

        if (anonimo == null) {
            System.err.println("¡FALLO CRÍTICO! No se pudo obtener/crear usuario anónimo (ID=" + idAnonimo + ").");
        }
        return anonimo;
    }

    public int obtenerCreditos(int usuarioId) throws SQLException {
    Connection con = null; PreparedStatement ps = null; ResultSet rs = null; int creditos = 0;
    try { con = ConnectionPool.getInstance().getConnection(); ps = con.prepareStatement(SQL_SELECT_CREDITOS); ps.setInt(1, usuarioId); rs = ps.executeQuery(); if (rs.next()) { creditos = rs.getInt("nCreditos"); } }
    catch (SQLException e) { System.err.println("Error SQL obtener créditos (ID=" + usuarioId + "): " + e.getMessage()); e.printStackTrace(); throw e; }
    finally { try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); } try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); } if (con != null) ConnectionPool.getInstance().freeConnection(con); }
    return creditos;
}


    /**
     * Verifica credenciales buscando SOLO por nombre de usuario
     * y comparando la contraseña directamente (¡INSEGURO!).
     *
     * @param nombreUsuario El nombre de usuario proporcionado para el login.
     * @param passwordProporcionada La contraseña en texto plano.
     * @return El objeto Usuario si la autenticación es exitosa, null en caso contrario.
     */
    public Usuario autenticarUsuario(String nombreUsuario, String passwordProporcionada) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuarioEncontrado = null;

        // Validación básica de parámetros
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
            passwordProporcionada == null || passwordProporcionada.isEmpty()) {
             System.out.println("DAO: Intento de autenticación con nombre de usuario o contraseña vacíos.");
            return null;
        }

        // Usa la query SQL específica para buscar por nombreUsuario
        String query = SQL_SELECT_FOR_AUTH_BY_USERNAME;

        System.out.println("--- DAO: autenticarUsuario (por nombreUsuario) ---");
        System.out.println("Buscando por nombreUsuario: [" + nombreUsuario + "]");

        try {
            connection = pool.getConnection();
            if (connection == null) {
                System.err.println("DAO Error: No se pudo obtener conexión.");
                return null;
            }

            ps = connection.prepareStatement(query);
            // Establecer el nombreUsuario en el placeholder de la query
            ps.setString(1, nombreUsuario);

            rs = ps.executeQuery();

            if (rs.next()) {
                // Usuario encontrado por nombreUsuario
                String passwordAlmacenada = rs.getString("contraseña");
                System.out.println("Usuario encontrado. Pass almacenada: [PROTEGIDO]");

                // --- Verificación de Contraseña (¡INSEGURO!) ---
                // !!! REEMPLAZAR CON HASHING (ej. BCrypt.checkpw()) !!!
                boolean passwordCoincide = passwordProporcionada.equals(passwordAlmacenada);
                System.out.println("¿Contraseñas coinciden (método actual)?: " + passwordCoincide);

                if (passwordCoincide) {
                    // ¡Contraseña correcta! Poblar el objeto Usuario.
                    usuarioEncontrado = new Usuario();
                    usuarioEncontrado.setidUsuario(rs.getInt("id"));
                    usuarioEncontrado.setNombreUsuario(rs.getString("nombreUsuario")); // importante guardarlo
                    usuarioEncontrado.setNombre(rs.getString("nombre"));
                    usuarioEncontrado.setApellido(rs.getString("apellido"));
                    usuarioEncontrado.setEdad(rs.getInt("edad"));
                    usuarioEncontrado.setCorreoElectronico(rs.getString("correoElectronico"));
                    usuarioEncontrado.setNumeroTelefono(rs.getString("numeroTelefono"));
                    usuarioEncontrado.setNCreditos(rs.getInt("nCreditos"));
                    usuarioEncontrado.setImagen(rs.getString("imagen"));
                    Timestamp fechaInscDb = rs.getTimestamp("fechaInscripcion");
                    if(fechaInscDb != null) usuarioEncontrado.setFechaInscripcion(new java.util.Date(fechaInscDb.getTime()));
                    Timestamp ultimaRecompensaDb = rs.getTimestamp("ultimaRecompensa");
                    if(ultimaRecompensaDb != null) usuarioEncontrado.setUltimaRecompensa(ultimaRecompensaDb);
                    usuarioEncontrado.setAdmin(rs.getBoolean("isAdmin"));
                    // No establecer la contraseña en el objeto de sesión
                    System.out.println("Autenticación exitosa para nombreUsuario: " + nombreUsuario);
                } else {
                    System.out.println("Contraseña INCORRECTA para nombreUsuario: " + nombreUsuario);
                    // usuarioEncontrado permanece null
                }
            } else {
                System.out.println("Usuario NO encontrado con nombreUsuario: [" + nombreUsuario + "]");
                // usuarioEncontrado permanece null
            }

        } catch (SQLException e) {
            System.err.println("Error SQL durante autenticación para nombreUsuario: " + nombreUsuario);
            e.printStackTrace();
            usuarioEncontrado = null;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) pool.freeConnection(connection);
        }

        System.out.println("DAO retornando: " + (usuarioEncontrado == null ? "NULL" : "Objeto Usuario (ID: " + usuarioEncontrado.getidUsuario() + ")"));
        return usuarioEncontrado;
    }


    // --- Métodos de validación de existencia (Sin cambios necesarios) ---
    // (El código es el mismo, no es necesario repetirlo)
     public boolean existeUsuarioPorEmail(String email) throws SQLException {
         String sql = SQL_COUNT_BY_EMAIL; Connection con = null; PreparedStatement ps = null; ResultSet rs = null;
         try { con = ConnectionPool.getInstance().getConnection(); ps = con.prepareStatement(sql); ps.setString(1, email); rs = ps.executeQuery(); if (rs.next()) return rs.getInt(1) > 0; }
         finally { try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); } try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); } if (con != null) ConnectionPool.getInstance().freeConnection(con); } return false;
    }

     public boolean existeUsuarioPorNombreUsuario(String nombreUsuario) throws SQLException {
         String sql = SQL_COUNT_BY_USERNAME; Connection con = null; PreparedStatement ps = null; ResultSet rs = null;
         try { con = ConnectionPool.getInstance().getConnection(); ps = con.prepareStatement(sql); ps.setString(1, nombreUsuario); rs = ps.executeQuery(); if (rs.next()) return rs.getInt(1) > 0; }
         finally { try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); } try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); } if (con != null) ConnectionPool.getInstance().freeConnection(con); } return false;
    }
        
     
    /**
     * Ajusta (suma o resta) la cantidad de créditos de un usuario.
     * @param usuarioId  Id del usuario
     * @param delta      Número de créditos a añadir (o negativo para restar)
     * @throws SQLException
     */
    public void updateCreditos(int usuarioId, int delta) throws SQLException {
        String sql = "UPDATE Usuario SET nCreditos = nCreditos + ? WHERE id = ?";
        try (Connection con = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        }
    }
    
    


} // Fin de la clase UsuarioDAO