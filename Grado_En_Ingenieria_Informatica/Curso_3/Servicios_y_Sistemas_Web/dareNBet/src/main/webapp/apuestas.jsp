<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Apuestas - DareNBet</title>
        <link href="${pageContext.request.contextPath}/estilos/estilo.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/estilos/estiloApuestas.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet"/>
        <script>
            function selectOdd(rowId, idx) {
                console.log("Seleccionada opción", idx, "de apuesta", rowId);
            }
        </script>
    </head>
    <body>
        <header class="site-header">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/apuestas">
                    <img src="${pageContext.request.contextPath}/resources/dareNbet.png" alt="Logo"/>
                </a>
            </div>
            <nav class="main-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/apuestas">Apuestas</a></li>
                    <li><a href="${pageContext.request.contextPath}/foro">Foro</a></li>
                    <li><a href="${pageContext.request.contextPath}/blog">Blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/ranking">Ranking</a></li>
                    <li><a href="${pageContext.request.contextPath}/configuraciones"><img src="${pageContext.request.contextPath}/resources/user.png" alt="Perfil"/></a></li>
                </ul>
            </nav>
        </header>

        <main>
            <section class="clasificador">
                <h2 class="main-title">Apuestas</h2>
                <p>Próximos eventos disponibles</p>

                <div class="add-bet-button-container">
                    <a href="${pageContext.request.contextPath}/crearApuesta" class="add-bet-button">Añadir Nueva Apuesta</a>
                </div>

                <c:if test="${param.status == 'ok'}">
                    <p class="success">¡Tu apuesta se ha registrado correctamente!</p>
                </c:if>
                <c:if test="${not empty errorSesion}">
                    <p class="error">${errorSesion}</p>
                </c:if>
                <c:if test="${not empty errorApuesta}">
                    <p class="error">${errorApuesta}</p>
                </c:if>

                <div class="bets-container">
                    <c:choose>
                        <c:when test="${empty sessionScope.usuarioLogueado}">
                            <p>Para apostar, por favor <a href="${pageContext.request.contextPath}/login">inicia sesión</a>.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="apuesta" items="${apuestas}">
                                <c:set var="rowId" value="${apuesta.id}"/>
                                <div class="bet-row upcoming" id="${rowId}">
                                    <div class="bet-header">
                                        <div class="bet-title">${apuesta.titulo}</div>
                                        <div class="bet-timer">
                                            <fmt:formatDate value="${apuesta.fechaFin}" pattern="dd/MM/yyyy"/>
                                            <fmt:formatDate value="${apuesta.fechaFin}" pattern=" HH:mm"/>h
                                        </div>
                                    </div>

                                    <div class="bet-image">
                                        <img src="${apuesta.imagen}" alt="${apuesta.titulo}"/>
                                    </div>

                                    <form action="${pageContext.request.contextPath}/apostar" method="post" class="bet-form">
                                        <input type="hidden" name="apuestaId" value="${apuesta.id}"/>
                                        <div class="bet-teams">
                                            <c:forEach var="opcion" items="${apuesta.opciones}" varStatus="st">
                                                <label class="team" onclick="selectOdd('${rowId}', ${st.index})">
                                                    <input
                                                        type="radio"
                                                        name="opcionId"
                                                        value="${opcion.id}"/>
                                                    <div class="team-content">
                                                        <span class="team-name">${opcion.texto}</span>
                                                        <span class="team-odd">${opcion.cuota}</span>
                                                    </div>
                                                </label>
                                            </c:forEach>
                                        </div>

                                        <div class="bet-action-group">
                                            <input type="number" name="importe" class="stake-input" placeholder="Importe" required min="1"/>
                                            <button type="submit" class="bet-button">Apostar</button>
                                        </div>
                                    </form>

                                    <!-- Likes / Dislikes / Comentarios -->
                                    <div class="forum-buttons">
                                        <a href="${pageContext.request.contextPath}/foro#post-${rowId}" class="forum-link">Debate original</a>
                                    </div>
                                    <div class="bet-footer">
                                        <div class="tags"><span>${apuesta.tags}</span></div>
                                        <div class="likes-dislikes">
                                            <!-- Formulario Like -->
                                            <form action="${pageContext.request.contextPath}/likeApuesta" method="post" style="display:inline">
                                                <input type="hidden" name="id" value="${apuesta.id}"/>
                                                <button type="submit" class="like-btn">👍 <span>${apuesta.NLikes}</span></button>
                                            </form>
                                            <!-- Formulario Dislike -->
                                            <form action="${pageContext.request.contextPath}/dislikeApuesta" method="post" style="display:inline">
                                                <input type="hidden" name="id" value="${apuesta.id}"/>
                                                <button type="submit" class="dislike-btn">👎 <span>${apuesta.NDislikes}</span></button>
                                            </form>
                                            <button class="show-comments-btn">💬 Mostrar comentarios</button>
                                        </div>
                                        <input type="text" class="comment-input" placeholder="Añadir comentario"/>
                                    </div>

                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>
        </main>

        <footer class="site-footer">
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
