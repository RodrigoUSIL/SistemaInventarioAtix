<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%
    // invalida la sesion actual eliminando todos los atributos
    session.invalidate();
    // redirige al login despues de cerrar sesion
    response.sendRedirect("index.jsp");
%>