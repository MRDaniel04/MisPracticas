<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE htcoml>
<html lang="es">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Crear Apuesta ‑ DareNBet</title>
        <link href="estilos/estiloCrearApuestas.css" rel="stylesheet" type="text/css"/>


        <link href="estilos/estilo.css"         rel="stylesheet"/>
        <link href="estilos/estiloApuestas.css" rel="stylesheet"/>


        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap"
              rel="stylesheet">
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
            <section class="clasificador">
                <h2 class="main-title">Crear Nueva Apuesta</h2>
                <p>Completa los campos para añadir una nueva apuesta</p>


                <form class="bet-form" action="${pageContext.request.contextPath}/crearApuesta" method="post">


                    <div class="form-group">
                        <label for="titulo">Título de la apuesta</label>
                        <input type="text" id="titulo" name="titulo"
                               placeholder="¿Qué sucederá en...?" required>
                    </div>


                    <div class="form-group">
                        <label for="imagen">URL de imagen</label>
                        <input type="url" id="imagen" name="imagen"
                               placeholder="https://ejemplo.com/imagen.jpg" required>
                    </div>


                    <div class="form-group">
                        <label for="fecha">Fecha límite</label>
                        <input type="datetime-local" id="fecha" name="fecha" required>
                    </div>


                    <div class="form-group opciones-container">
                        <h3>Opciones de apuesta</h3>


                        <div class="opcion">
                            <input type="text"  name="opcion1"
                                   placeholder="Opción 1 (Ej. Sí)" required>
                            <input type="number" step="0.01"  name="cuota1"
                                   placeholder="Cuota (Ej. 2.5)" required>
                        </div>


                        <div class="opcion">
                            <input type="text"  name="opcion2"
                                   placeholder="Opción 2 (Ej. No)" required>
                            <input type="number" step="0.01" name="cuota2"
                                   placeholder="Cuota (Ej. 1.5)" required>
                        </div>


                        <div class="opcion">
                            <input type="text"  name="opcion3"
                                   placeholder="Opción 3 (opcional)">
                            <input type="number" step="0.01" name="cuota3"
                                   placeholder="Cuota (opcional)">
                        </div>


                        <div class="opcion">
                            <input type="text"  name="opcion4"
                                   placeholder="Opción 4 (opcional)">
                            <input type="number" step="0.01" name="cuota4"
                                   placeholder="Cuota (opcional)">
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="tags">Categorías o etiquetas</label>
                        <input type="text" id="tags" name="tags"
                               placeholder="Ej. Fútbol, Música, Política" required>
                    </div>


                    <div class="form-group">
                        <button type="submit" class="bet-button">Publicar Apuesta</button>
                    </div>
                </form>
            </section>
        </main>


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
