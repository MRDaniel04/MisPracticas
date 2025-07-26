<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Resultado de Créditos ‑ DareNBet</title>

        <!-- Hojas de estilo compartidas -->
        <link href="estilos/estilo.css" rel="stylesheet"/>
        <link href="estilos/estiloConfiguraciones.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap"
              rel="stylesheet">
    </head>

    <body>
        <!-- ▸  CABECERA  ◂ -->
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

        <!-- ▸  CONTENIDO PRINCIPAL  ◂ -->
        <main>
            <section class="clasificador">
                <div class="config-container">
                    <div class="config-center config-block" style="width: 100%; text-align: center;">
                        <h2>Resultado de Créditos</h2>


                        <c:choose>
                            <c:when test="${not empty error}">
                                <p style="color: red; font-size: 18px;">
                                    Error: ${error}
                                </p>
                            </c:when>

                            <c:when test="${estado eq 'ok'}">
                                <p style="color: green; font-size: 18px;">
                                    ¡Has ganado <strong>${creditos}</strong> créditos nuevos! 🎉
                                </p>
                            </c:when>

                            <c:when test="${estado eq 'espera'}">
                                <p style="color: orange; font-size: 18px;">
                                    Debes esperar <strong>${horasRestantes}</strong> horas para reclamar créditos nuevamente.
                                </p>
                            </c:when>
                        </c:choose>


                        <br>
                        <button onclick="window.location.href = 'configuraciones.jsp'" class="bet-button">Volver a Configuración</button>
                    </div>
                </div>
            </section>
        </main>

        <!-- ▸  PIE  ◂ -->
        <footer>
            <div class="contacto">
                <h3>Contacto</h3>
                <p>Email: contacto@pagina.com</p>
                <p>Teléfono: +123 456 789</p>
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