<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>CrearEntradaBlog - DareNBet</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="estilos/estilo.css" rel="stylesheet" type="text/css"/>
        <link href="estilos/estilloCrearEntradaBlog.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <!-- CABECERA -->
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

        <!-- CUERPO PRINCIPAL -->
        <main>
            <section class="clasificador" tabindex="0">
                <div class="contenedorTitulo">
                    <h1 id="tituloCrearEntradaBlog">Crear entrada al blog</h1>
                </div>

                <!-- FORMULARIO -->
                <form id="formEntrada" action="crearEntradaBlog" method="post">

                    <div class="campo">
                        <label for="titulo">Título:</label>
                        <input type="text" id="titulo" name="titulo" required>
                    </div>

                    <div class="campo">
                        <label for="fecha">Fecha:</label>
                        <input type="date" id="fecha" name="fecha" required>
                    </div>

                    <div class="campo">
                        <label for="descripcion">Descripción:</label>
                        <textarea id="descripcion" name="descripcion" required></textarea>
                    </div>
                    <div class="campo">
                        <label for="icono">Elegir icono:</label>
                        <select id="icono" name="icono">
                            <option value="resources/news/actualizacionNews.png">IconoActualizacion</option>
                            <option value="resources/news/eventoNews.png">IconoEvento</option>
                            <option value="resources/news/regaloNews.png">IconoRegalo</option>
                        </select>
                    </div>

                    <div class="botones">
                        <button type="submit" id="crear">Crear</button>
                        <button type="reset" id="reiniciar">Reiniciar</button>
                        <button type="button" id="volver" onclick="window.location.href = 'blog'">Volver</button>
                    </div>
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

        <!-- SCRIPT -->
        <script>
            function handleSubmit() {

                alert("Entrada creada correctamente 🎉");


                document.getElementById("formEntrada").reset();


                return true;
            }
        </script>


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
