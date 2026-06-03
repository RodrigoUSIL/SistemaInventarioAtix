<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%@page import="modelo.Usuario"%>
<%
    // verifica si hay sesion activa, si no redirige al login
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioActivo == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Menu Principal - Sistema de Inventarios</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f0f0f0;
            min-height: 100vh;
        }

        .header {
            background: #8B0033;
            color: white;
            padding: 16px 24px;
        }

        .header h1 {
            font-size: 18px;
            font-weight: bold;
        }

        .header p {
            font-size: 12px;
            color: #f5b8c8;
            margin-top: 4px;
        }

        .contenido {
            padding: 30px;
        }

        .label-menu {
            font-size: 13px;
            font-weight: bold;
            color: #555;
            margin-bottom: 16px;
        }

        .btn-modulo {
            display: block;
            width: 320px;
            padding: 12px 16px;
            background: white;
            border: 1px solid #ccc;
            border-radius: 3px;
            font-size: 13px;
            color: #333;
            text-decoration: none;
            margin-bottom: 10px;
            cursor: pointer;
        }

        .btn-modulo:hover { background: #f5f5f5; }

        .footer {
            background: #ebebeb;
            padding: 12px 24px;
            position: fixed;
            bottom: 0;
            width: 100%;
            display: flex;
            justify-content: flex-end;
        }

        .btn-cerrar {
            background: #8B0033;
            color: white;
            border: none;
            border-radius: 3px;
            padding: 8px 16px;
            font-size: 12px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
        }

        .btn-cerrar:hover { background: #6d0028; }
    </style>
</head>
<body>

    <div class="header">
        <h1>Sistema de Gestión de Inventarios</h1>
        <%-- muestra el nombre del usuario activo desde la sesion --%>
        <p>Bienvenido, <%= usuarioActivo.getNomUsuario() %>!</p>
    </div>

    <div class="contenido">
        <p class="label-menu">Seleccione un modulo:</p>

        <%-- botones de modulos, se iran agregando conforme avance el proyecto --%>
        <a href="#" class="btn-modulo">Gestión de Productos</a>
        <a href="#" class="btn-modulo">Movimientos de Inventario</a>
        <a href="#" class="btn-modulo">Reportes</a>
    </div>

    <div class="footer">
        <%-- cierra la sesion y redirige al login --%>
        <a href="cerrarSesion.jsp" class="btn-cerrar">Cerrar Sesión</a>
    </div>

</body>
</html>