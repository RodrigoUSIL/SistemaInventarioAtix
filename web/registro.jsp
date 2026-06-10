<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%
    // si ya hay sesion activa redirige al menu principal
    if (session.getAttribute("usuarioActivo") != null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }

    // recupera mensaje de error si existe
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro - Sistema de Inventarios</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', sans-serif;
                background: #f0f0f0;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }

            .card {
                background: white;
                border: 2px solid #8B0033;
                border-radius: 4px;
                padding: 30px 40px;
                width: 460px;
            }

            .titulo {
                text-align: center;
                color: #8B0033;
                font-size: 20px;
                font-weight: bold;
                margin-bottom: 4px;
            }

            .subtitulo {
                text-align: center;
                color: #646464;
                font-size: 12px;
                margin-bottom: 10px;
            }

            .separador {
                border: none;
                border-top: 1.5px solid #8B0033;
                margin-bottom: 20px;
            }

            label {
                display: block;
                font-size: 14px;
                color: #000;
                margin-bottom: 6px;
            }

            input[type="text"],
            input[type="password"],
            input[type="email"] {
                width: 100%;
                padding: 8px 10px;
                background: white;
                color: #333;
                border: 1px solid #8B0033;
                border-radius: 3px;
                font-size: 13px;
                margin-bottom: 14px;
            }


            .btn-principal {
                width: 100%;
                padding: 10px;
                background: #8B0033;
                color: white;
                border: none;
                border-radius: 3px;
                font-size: 13px;
                font-weight: bold;
                cursor: pointer;
                margin-bottom: 10px;
            }

            .btn-principal:hover {
                background: #6d0028;
            }

            .link-centro {
                text-align: center;
            }

            .link-centro a {
                color: #8B0033;
                font-size: 12px;
                font-weight: bold;
                text-decoration: none;
            }

            .mensaje-error {
                background: #ffe0e0;
                color: #cc0000;
                padding: 8px 12px;
                border-radius: 3px;
                font-size: 12px;
                margin-bottom: 12px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="card">
            <p class="titulo">Crear nueva cuenta</p>
            <p class="subtitulo">Complete los campos para registrarse</p>
            <hr class="separador">

            <%-- muestra error si existe --%>
            <% if (error != null && !error.isEmpty()) {%>
            <div class="mensaje-error"><%= error%></div>
            <% }%>

            <form method="post" action="registro">
                <label>Usuario:</label>
                <input type="text" name="nomUsuario" />

                <label>Correo (opcional):</label>
                <input type="email" name="correo" />

                <label>Contraseña:</label>
                <input type="password" name="contrasena" />

                <label>Confirmar contraseña:</label>
                <input type="password" name="confirmar" />

                <button type="submit" class="btn-principal">Registrar Usuario</button>
            </form>

            <div class="link-centro">
                <a href="login.jsp">Volver al inicio de sesión</a>
            </div>
        </div>
    </body>
</html>