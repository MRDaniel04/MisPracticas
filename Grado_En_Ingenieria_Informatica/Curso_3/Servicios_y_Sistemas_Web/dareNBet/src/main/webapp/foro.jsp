<%-- foro.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Foro - DareNBet</title>
        <% String basePath = request.getContextPath();%>
        <link href="<%=basePath%>/estilos/estilo.css" rel="stylesheet" type="text/css"/>
        <link href="<%=basePath%>/estilos/estiloForo.css" rel="stylesheet" type="text/css"/>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">

    </head>
    <body>
        <header>
            <div class="logo"><img src="<%=basePath%>/resources/dareNbet.png" alt="Logo"/></div>
            <nav>
                <ul>
                    <li><a href="<c:url value='/apuestas'/>">Apuestas</a></li>
                    <li><a href="foro">Foro</a></li>
                    <li><a href="<c:url value='/blog'/>">Blog</a></li>
                    <li><a href="<c:url value='/ranking'/>">Ranking</a></li>
                    <li><a href="<c:url value='/configuraciones'/>"><img src="<%=basePath%>/resources/user.png" alt="Perfil"/></a></li>
                </ul>
            </nav>
        </header>

        <main>
            <section class="clasificador">
                <div class="search-container">
                    <input type="text" class="search-input" placeholder="Buscar...">
                    <button class="search-button">🔍</button>
                </div>
            </section>

            <section class="posts">
                <div class="posts-container">

                    <%-- Formulario para crear nuevo post --%>
                    <div class="post new-post">
                        <form id="nuevoPostForm" action="<c:url value='/foro'/>" method="post">
                            <div class="post-top">
                                <div class="user-container">
                                    <img src="<%=basePath%>/resources/user.png" alt="Foto de perfil" class="perfil-img">
                                    <span class="username">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.usuarioLogueado}">
                                                ${sessionScope.usuarioLogueado.nombre} ${sessionScope.usuarioLogueado.apellido}
                                            </c:when>
                                            <c:otherwise>
                                                Nuevo Post (Anónimo)
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="title-container">
                                    <input type="text" name="tituloPost" class="post-title" placeholder="Escribe el título aquí..." required>
                                </div>
                            </div>
                            <div class="content-container">
                                <textarea name="contenidoPost" class="post-content" placeholder="Escribe el contenido aquí..." required></textarea>
                            </div>
                            <div class="post-actions">
                                <button type="submit" class="new-post-btn">+</button>
                            </div>
                        </form>
                    </div>
                    <%-- Fin Formulario Nuevo Post --%>

                    <%-- Mensajes de la CREACIÓN DE POSTS --%>
                    <c:if test="${not empty exitoCreacionPost}">
                        <p style="color:green; text-align:center; margin:10px; padding:10px; background-color:#e8f5e9; border:1px solid green;">${exitoCreacionPost}</p>
                    </c:if>
                    <c:if test="${not empty errorCreacionPost}">
                        <p style="color:red; text-align:center; margin:10px; padding:10px; background-color:#ffebee; border:1px solid red;">${errorCreacionPost}</p>
                    </c:if>

                    <%-- Mensajes de VOTACIÓN (éxito o error general) --%>
                    <c:if test="${not empty sessionScope.exitoVoto}">
                        <p style="color:green; text-align:center; margin:10px; padding:10px; background-color:#e8f5e9; border:1px solid green;">
                            ${sessionScope.exitoVoto}
                        </p>
                        <% session.removeAttribute("exitoVoto"); %>
                    </c:if>
                    <c:if test="${not empty sessionScope.errorVoto}">
                        <p style="color:red; text-align:center; margin:10px; padding:10px; background-color:#ffebee; border:1px solid red;">
                            ${sessionScope.errorVoto}
                        </p>
                        <% session.removeAttribute("errorVoto");%>
                    </c:if>

                    <%-- Mensaje de error general al cargar el foro --%>
                    <c:if test="${not empty errorForo}">
                        <p style="color:red; text-align:center; margin-top: 20px;">${errorForo}</p>
                    </c:if>

                    <%-- Mensaje si no hay posts --%>
                    <c:if test="${empty errorForo && empty postsForo}">
                        <p style="text-align: center; margin-top: 20px;">Aún no hay posts en el foro. ¡Sé el primero en publicar!</p>
                    </c:if>

                    <%-- Iterar sobre la lista de posts existentes --%>
                    <c:if test="${empty errorForo && not empty postsForo}">
                        <c:forEach var="post" items="${postsForo}">
                            <div class="post" id="post-${post.id}">
                                <div class="post-top">
                                    <div class="user-container">
                                        <img src="<%=basePath%>/resources/user.png" alt="Foto de perfil" class="perfil-img">
                                        <span class="username">
                                            <c:choose>
                                                <c:when test="${not empty post.autor}">
                                                    <c:set var="nombreAutor" value="${post.autor.nombre}" />
                                                    <c:set var="apellidoAutor" value="${post.autor.apellido}" />
                                                    <c:choose>
                                                        <c:when test="${not empty nombreAutor or not empty apellidoAutor}">
                                                            ${nombreAutor} ${apellidoAutor}
                                                        </c:when>
                                                        <c:when test="${post.autor.idUsuario == 1}"> <%-- Asumiendo ID 1 es Anónimo --%>
                                                            Usuario Anónimo
                                                        </c:when>
                                                        <c:when test="${post.autor.idUsuario > 0}">
                                                            Usuario (ID: ${post.autor.idUsuario})
                                                        </c:when>
                                                        <c:otherwise>
                                                            Usuario Desconocido
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    Usuario Desconocido
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                    <div class="title-container">
                                        <h2 class="post-title"><c:out value="${post.titulo}"/></h2>
                                    </div>
                                </div>
                                <div class="content-container">
                                    <p class="post-content"><c:out value="${post.contenido}"/></p>
                                </div>
                                <div class="post-actions">
                                    <div class="actions-row">
                                        <div class="likes-dislikes-container">
                                            <form action="<c:url value='/votarPost'/>" method="post" style="display: inline;">
                                                <input type="hidden" name="idPost" value="${post.id}">
                                                <input type="hidden" name="accion" value="like">
                                                <button type="submit" class="like-button" <c:if test="${empty sessionScope.usuarioLogueado}">disabled title="Inicia sesión para votar"</c:if>>👍</button>
                                                </form>
                                                <span class="like-count">${post.nLikes}</span>
                                            <form action="<c:url value='/votarPost'/>" method="post" style="display: inline; margin-left: 10px;">
                                                <input type="hidden" name="idPost" value="${post.id}">
                                                <input type="hidden" name="accion" value="dislike">
                                                <button type="submit" class="dislike-button" <c:if test="${empty sessionScope.usuarioLogueado}">disabled title="Inicia sesión para votar"</c:if>>👎</button>
                                                </form>
                                                <span class="dislike-count">${post.nDislikes}</span>
                                        </div>
                                        <div class="right-actions">
                                            <div class="toggle-container">
                                                <button class="toggle-comments">Mostrar comentarios (<c:out value="${post.comentarios.size()}"/>)</button>
                                                <%-- Muestra el número de comentarios si la lista existe y no está vacía --%>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="comments-container empty"> <%-- La clase 'empty' o 'expanded' se manejará con JS --%>
                                        <div class="comments-list">
                                            <%-- FORMULARIO PARA NUEVO COMENTARIO --%>
                                            <div class="comment new-comment-form">
                                                <form action="<c:url value='/crearComentario'/>" method="post" class="comment-form-inline">
                                                    <input type="hidden" name="idPost" value="${post.id}">
                                                    <input type="text" name="contenidoComentario" class="comment-input"
                                                           placeholder="Escribe tu comentario..." required
                                                           <c:if test="${empty sessionScope.usuarioLogueado}">disabled title="Inicia sesión para comentar"</c:if>>
                                                           <button type="submit" class="submit-comment-btn"
                                                           <c:if test="${empty sessionScope.usuarioLogueado}">disabled title="Inicia sesión"</c:if>>💬 Enviar</button>
                                                    </form>
                                                </div>

                                            <%-- MOSTRAR COMENTARIOS EXISTENTES --%>
                                            <c:if test="${not empty post.comentarios}">
                                                <c:forEach var="comentario" items="${post.comentarios}">
                                                    <div class="comment">
                                                        <span class="comment-username">
                                                            <c:out value="${comentario.autor.nombreUsuario != null ? comentario.autor.nombreUsuario : (comentario.autor.nombre != null ? comentario.autor.nombre : 'Usuario Anónimo')}"/>:
                                                        </span>
                                                        <p class="comment-text"><c:out value="${comentario.contenido}"/></p>
                                                        <small class="comment-date">
                                                            <c:if test="${not empty comentario.fechaComentario}">
                                                                <fmt:formatDate value="${comentario.fechaComentario}" pattern="dd/MM/yyyy HH:mm"/>
                                                            </c:if>
                                                        </small>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${empty post.comentarios}">
                                                <p class="no-comments-yet">
                                                    Aún no hay comentarios. ¡Sé el primero!
                                                </p>
                                            </c:if>
                                            <%-- FIN MOSTRAR COMENTARIOS --%>
                                        </div>
                                    </div>
                                    <div style="font-size: 0.8em; color: #555; text-align: right; margin-top: 5px;">
                                        Publicado:
                                        <c:choose>
                                            <c:when test="${not empty post.fechaPublicacion}">
                                                <fmt:formatDate value="${post.fechaPublicacion}" pattern="dd/MM/yyyy HH:mm"/>
                                            </c:when>
                                            <c:otherwise>
                                                Fecha no disponible
                                            </c:otherwise>
                                        </c:choose>
                                        | Visto: ${post.nVisualizaciones} veces
                                    </div>
                                </div> <%-- Fin post-actions --%>
                            </div> <%-- Fin post --%>
                        </c:forEach>
                    </c:if>

                </div> <%-- Fin posts-container --%>
            </section> <%-- Fin section.posts --%>
        </main>

        <footer>
            <div class="contacto">
                <h3>Contacto</h3>
                <p>Email: contacto@pagina.com</p>
                <p>Teléfono: +123 456 789</p>
            </div>
        </footer>

        <%-- ===== SCRIPT PARA LA ALERTA DE VOTO REPETIDO ===== --%>
        <c:if test="${not empty sessionScope.alertaVotoRepetido}">
            <script type="text/javascript">
                document.addEventListener('DOMContentLoaded', function () {
                    alert("${sessionScope.alertaVotoRepetido}");
                });
            </script>
            <%
                session.removeAttribute("alertaVotoRepetido");
            %>
        </c:if>
        <%-- =================================================== --%>




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