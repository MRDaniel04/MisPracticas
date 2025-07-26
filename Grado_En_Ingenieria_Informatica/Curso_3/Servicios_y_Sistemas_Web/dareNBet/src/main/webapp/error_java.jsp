<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page isErrorPage="true" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Java Error</h1>
        <p>Sorry, Java has thrown an exception.</p>
        <p>To continue, click the Back button.</p>
        <br>
        <h2>Details</h2>
        <p>
         Type: <%= exception.getClass() %><br>
         Message: <%= exception.getMessage() %><br>
        </p>
    </body>
</html>
