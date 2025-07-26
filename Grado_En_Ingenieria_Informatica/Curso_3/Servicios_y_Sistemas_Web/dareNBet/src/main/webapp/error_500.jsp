<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error 500 - Internal Server Error</title>
    </head>
    <body>
        <h1>Error 500 - Internal Server Error</h1>
        <p>Ocurrió un error inesperado en el servidor.</p>
        <p>Para continuar, haz clic en el botón de retroceso de tu navegador.</p>

        <hr>
        <h2>Detalles del error:</h2>
        <p><strong>Excepción:</strong> <%= exception.getClass().getName() %></p>
        <p><strong>Mensaje:</strong> <%= exception.getMessage() %></p>

        <h3>Stack trace:</h3>
        <pre>
<%
    exception.printStackTrace(new java.io.PrintWriter(out));
%>
        </pre>
    </body>
</html>


