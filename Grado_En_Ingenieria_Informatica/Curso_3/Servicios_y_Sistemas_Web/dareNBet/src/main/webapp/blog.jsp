<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uva.ssw.entrega.modelo.Entrada" %>
<%@ page import="uva.ssw.entrega.modelo.Usuario" %>
<%
    List<Entrada> entradas = (List<Entrada>) request.getAttribute("entradas");
%>
<%
    // Recuperamos el usuario de la sesión
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
%>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Blog - DareNBet</title>
        <link href="estilos/estilo.css" rel="stylesheet" type="text/css"/>
        <link href="estilos/estiloBlog.css" rel="stylesheet" type="text/css"/>
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
                    <li><a href="ranking">Ranking</a></li>
                    <li><a href="configuraciones"><img src="resources/user.png" alt="Perfil"/></a></li>
                </ul>
            </nav>
        </header>

        <!-- Cuerpo principal -->
        <main>
            <section class="clasificador" tabindex="0">
                <h1 id="tituloBlog">Blog de Noticias</h1>
                <% if (u != null && u.isAdmin()) { %>
                <div class="contenedorBotonesBlog">
                    <div class="botonIzquierda">
                        <a href="crearEntradaBlog" class="btnBlog">➕ Crear Entrada</a>
                    </div>
                    <div class="botonDerecha">
                        <a href="eliminarEntradaVista" class="btnBlog eliminar">🗑️ Eliminar Entrada</a>
                    </div>
                </div>
                <% } %>

                <%
                    if (entradas != null && !entradas.isEmpty()) {
                        for (Entrada entrada : entradas) {
                %>
                <section class="news" tabindex="0">
                    <div class="iconoNews">
                        <img src="<%= request.getContextPath() + "/" + entrada.getIcono()%>" alt="Icono News">
                    </div>
                    <div class="bodyNews">
                        <div class="tituloNews">
                            <p><%= entrada.getTitulo()%></p>
                        </div>
                        <div class="fechaNews">
                            <p><%= entrada.getFechaPublicacion()%></p>
                        </div>
                        <div class="textoNews">
                            <p><%= entrada.getDescripcion()%></p>
                        </div>
                    </div>
                </section>
                <%
                    }
                } else {
                %>
                <p>No hay entradas disponibles.</p>
                <%
                    }
                %>
            </section>
        </main>

        <!-- Pie de página -->
        <footer>
            <div class="contacto">
                <h3>Contacto</h3>
                <p>Email: contacto@pagina.com</p>
                <p>Teléfono: +123 456 789</p>
            </div>
        </footer>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const tema = localStorage.getItem('tema');
                if (tema === 'oscuro') {
                    document.body.classList.add('tema-oscuro');
                }
            });
        </script>


    </body>
</html>
