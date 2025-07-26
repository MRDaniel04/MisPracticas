<%@ page import="java.util.List, uva.ssw.entrega.modelo.Usuario" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Página con Menú Superior</title>
        <link href="estilos/estilo.css" rel="stylesheet" type="text/css"/>

    </head>
<body>
    <!-- Menú superior -->
    <header>
        <div class="logo">
            <img src="resources/dareNbet.png" alt="Logo"/>
        </div>
        <nav>
            <ul>
                <li><a href="apuestas">Apuestas</a></li>
                <li><a href="foro">Foro</a></li>
                <li><a href="blog">Blog</a></li>
                <li><a>Ranking</a></li>
                <li><a href="configuraciones"><img src="resources/user.png" alt="Perfil"/></a></li>
            </ul>
        </nav>
    </header>
    <main>
    <div class="container">
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p class="error"><%= error %></p>
            <p><a href="registro.html">Volver al formulario de registro</a></p>
        <%
            } else {
                Usuario usuario = (Usuario) session.getAttribute("usuario");
        %>
            <p class="success">¡Usuario registrado correctamente!</p>
            <p>Bienvenido, <strong><%= usuario.getNombre() %> <%= usuario.getApellido() %></strong>.</p>
            <p>Tu nombre de usuario es: <strong><%= usuario.getNombreUsuario()%></strong></p>
        <%
            }
        %>
    </main>
    <footer>
        <div class="contacto">
            <h3>Contacto</h3>
            <p>Email: contacto@pagina.com</p>
            <p>Teléfono: +123 456 789</p>
        </div>
    </footer>
</html>
