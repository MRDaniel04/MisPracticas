<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uva.ssw.entrega.modelo.Entrada" %>
<%
    List<Entrada> entradas = (List<Entrada>) request.getAttribute("entradas");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Eliminar Entradas - DareNBet</title>
        <link href="estilos/estilo.css" rel="stylesheet" type="text/css"/>
        <link href="estilos/estiloBlog.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <header>
            <div class="logo">
                <img src="resources/dareNbet.png" alt="Logo"/>
            </div>
        </header>

        <main>
            <section class="clasificador" tabindex="0">
                <h1>Eliminar entradas del blog</h1>

                <form action="eliminarEntradas" method="post">
                    <div style="margin-top: 20px;">
                        <button type="submit" class="btnBlog eliminar">🗑️ Eliminar seleccionadas</button>
                        <a href="blog" class="btnBlog">← Volver al blog</a>
                    </div>
                    <%
                        if (entradas != null && !entradas.isEmpty()) {
                            for (Entrada entrada : entradas) {
                    %>
                    <div class="entradaConCheckbox">
                        <input type="checkbox" name="entradaId" value="<%= entrada.getId()%>"/>
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
                    </div>
                    <%
                        }
                    } else {
                    %>
                    <p>No hay entradas disponibles.</p>
                    <% }%>


                </form>
            </section>
        </main>
        <!-- PIE DE PÁGINA -->
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
