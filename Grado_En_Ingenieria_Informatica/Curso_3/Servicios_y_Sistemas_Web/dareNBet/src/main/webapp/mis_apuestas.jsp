<%-- 
    Document   : misApuestas
    Created on : 25 may 2025, 17:07:11
    Author     : fredi
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Mis Apuestas - DareNBet</title>
        <link href="${pageContext.request.contextPath}/estilos/estilo.css" rel="stylesheet"/>
        <link href="${pageContext.request.contextPath}/estilos/estiloApuestas.css" rel="stylesheet"/>
    </head>
    <body>
        <header class="site-header">
            <div class="logo"><a href="${pageContext.request.contextPath}/apuestas"><img src="${pageContext.request.contextPath}/resources/dareNbet.png" alt="Logo"/></a></div>
            <nav class="main-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/apuestas">Apuestas</a></li>
                    <li><a href="${pageContext.request.contextPath}/mis_apuestas" class="active">Mis Apuestas</a></li>
                    <li><a href="${pageContext.request.contextPath}/foro">Foro</a></li>
                    <li><a href="${pageContext.request.contextPath}/blog">Blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/ranking">Ranking</a></li>
                    <li><a href="${pageContext.request.contextPath}/configuraciones"><img src="${pageContext.request.contextPath}/resources/user.png" alt="Perfil"/></a></li>
                </ul>
            </nav>
        </header>

        <main>
            <div class="container mis-apuestas-section">
                <h1 class="page-title">Mis Apuestas</h1>

                <c:if test="${not empty errorSesion}">
                    <div class="alert alert-warning">${errorSesion}</div>
                </c:if>

                <c:choose>
                    <c:when test="${empty misApuestas}">
                        <p class="no-data">Todavía no has realizado ninguna apuesta.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="cards-container">
                            <c:forEach var="ua" items="${misApuestas}">
                                <div class="bet-card" id="${ua.apuesta.id}">
                                    <div class="card-header">
                                        <h3 class="card-title">${ua.apuesta.titulo}</h3>
                                        <time class="card-date" datetime="${ua.fechaApuesta}">
                                            <fmt:formatDate value="${ua.fechaApuesta}" pattern="dd/MM/yyyy HH:mm"/>
                                        </time>
                                    </div>
                                    <div class="card-body">
                                        <p><span class="label">Opción:</span> ${ua.opcion.texto}</p>
                                        <p><span class="label">Cuota:</span> ${ua.opcion.cuota}</p>
                                        <p><span class="label">Importe apostado:</span> ${ua.importe}</p>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
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
