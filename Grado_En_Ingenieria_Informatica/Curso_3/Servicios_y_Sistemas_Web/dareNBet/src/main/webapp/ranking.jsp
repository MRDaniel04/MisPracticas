<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, uva.ssw.entrega.modelo.Apuesta" %>
<%
    List<Apuesta> top10 = (List<Apuesta>) request.getAttribute("top10Likes");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ranking - DareNBet</title>
        <link href="estilos/estilo.css" rel="stylesheet"/>
        <link href="estilos/estiloRanking.css" rel="stylesheet"/>
    </head>
    <body>
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
        <main>
            <h1>Ranking</h1>
            <div class="ranking">
                <details open>
                    <summary>Top 10 apuestas con más likes</summary>
                    <div class="top10Apuestas">
                        <% for (int i = 0; i < top10.size(); i++) {
                        Apuesta a = top10.get(i);%>
                        <div class="apuesta">
                            <div class="rank"><%= i + 1%></div>
                            <a href="apuestas#<%= a.getId()%>">
                                <p><%= a.getTitulo()%></p>
                            </a>
                        </div>
                        <% }%>
                    </div>
                </details>
            </div>
        </main>
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