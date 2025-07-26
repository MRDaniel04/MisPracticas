<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="uva.ssw.entrega.modelo.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
%>
<p style="color:red; font-weight: bold;">⚠️ No has iniciado sesión.</p>
<%
        return;
    }
%>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Configuración - DareNBet</title>
        <link href="${pageContext.request.contextPath}/estilos/estilo.css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath}/estilos/estiloConfiguraciones.css" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    </head>

    <body>
        <header>
            <div class="logo">
                <img src="${pageContext.request.contextPath}/resources/dareNbet.png" alt="Logo"/>
            </div>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/apuestas">Apuestas</a></li>
                    <li><a href="${pageContext.request.contextPath}/foro">Foro</a></li>
                    <li><a href="${pageContext.request.contextPath}/blog">Blog</a></li>
                    <li><a href="ranking">Ranking</a></li>
                    <li><a href="${pageContext.request.contextPath}/configuraciones">
                            <img src="${pageContext.request.contextPath}/resources/user.png" alt="Perfil">
                        </a></li>
                </ul>
            </nav>
        </header>

        <main>
            <section class="clasificador">
                <div class="config-container">
                    <!-- Perfil (izquierda) -->
                    <div class="config-left config-block">
                        <div class="profile">
                            <img src="${pageContext.request.contextPath}/resources/account.png" alt="Usuario">
                            <h2>Mi Perfil</h2>
                            <ul>
                                <li><strong>Nombre:</strong> <%= usuario.getNombre()%></li>
                                <li><strong>Correo:</strong> <%= usuario.getCorreoElectronico()%></li>
                                <li><a href="${pageContext.request.contextPath}/DareNBet/inicio.html">Vincular Creador</a></li>
                                <li><a href="#">Cerrar Sesión</a></li>
                            </ul>
                        </div>
                    </div>

                    <!-- Preferencias (centro) -->
                    <div class="config-center config-block">
                        <h2>Preferencias</h2>
                        <div class="config-section">
                            <h3>Tema</h3>
                            <div class="tema-selector">
                                <label for="tema-claro">
                                    <input type="radio" id="tema-claro" name="tema" value="claro" checked> Claro
                                </label>
                                <label for="tema-oscuro">
                                    <input type="radio" id="tema-oscuro" name="tema" value="oscuro"> Oscuro
                                </label>
                            </div>
                        </div>

                        <div class="config-section">
                            <h3>Me Interesa</h3>
                            <input type="text" placeholder="Agregar intereses" class="input-intereses">
                        </div>

                        <div class="config-section activity">
                            <h3>Actividad Reciente</h3>
                            <ul>
                                <li>
                                    <a href="${pageContext.request.contextPath}/mis_apuestas" class="link-ver-mis-apuestas">
                                      Ver mis apuestas
                                    </a>
                                </li>
                                <li>2 comentarios en el foro</li>
                            </ul>
                        </div>
                    </div>


                    <div class="config-right config-block">
                        <h2>Tus Créditos</h2>
                        <p>💰 <span id="creditos"><%= usuario.getNCreditos()%></span></p>
                        <form action="${pageContext.request.contextPath}/reclamarCreditos" method="post">
                            <button type="submit" class="btn-creditos">Obtener más</button>
                        </form>


                        <div class="config-section">
                            <h3>Estadísticas de Creador</h3>
                            <div class="config-section">
                                <h3>Apuestas realizadas</h3>
                                <p>1000 Apuestas realizadas</p>
                            </div>

                            <div class="config-section">
                                <h3>Visualizaciones totales sobre sus apuestas:</h3>
                                <p>23000 visualizaciones</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main>

        <footer>
            <div class="barra-inferior">
                <div class="contacto">
                    <h3>Contacto</h3>
                    <p>Email: contacto@pagina.com</p>
                    <p>Teléfono: +123 456 789</p>
                </div>
            </div>
        </footer>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const temaGuardado = localStorage.getItem('tema');
                if (temaGuardado === 'oscuro') {
                    document.body.classList.add('tema-oscuro');
                    document.getElementById('tema-oscuro')?.setAttribute("checked", true);
                }

                document.querySelectorAll('input[name="tema"]').forEach((input) => {
                    input.addEventListener('change', (e) => {
                        if (e.target.value === 'oscuro') {
                            document.body.classList.add('tema-oscuro');
                            localStorage.setItem('tema', 'oscuro');
                        } else {
                            document.body.classList.remove('tema-oscuro');
                            localStorage.setItem('tema', 'claro');
                        }
                    });
                });
            });
        </script>


    </body>
</html>
