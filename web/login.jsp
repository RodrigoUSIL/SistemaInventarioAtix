<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%
    // si ya hay sesion activa redirige al menu principal
    if (session.getAttribute("usuarioActivo") != null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }

    // recupera mensajes de error o exito si existen
    String error = (String) request.getAttribute("error");
    String exito = request.getParameter("exito");
    Integer intentosRestantes = (Integer) request.getAttribute("intentosRestantes");
    Boolean bloqueado = (Boolean) request.getAttribute("bloqueado");
    if (bloqueado == null)
        bloqueado = false;
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Login - Sistema de Inventarios</title>
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
            input[type="password"] {
                width: 100%;
                padding: 8px 10px;
                background: white;
                color: #333;
                border: 1px solid #8B0033;
                border-radius: 3px;
                font-size: 13px;
                margin-bottom: 14px;
            }

            .link-derecha {
                text-align: right;
                margin-top: -10px;
                margin-bottom: 14px;
            }

            .link-derecha a {
                color: #8B0033;
                font-size: 12px;
                text-decoration: none;
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

            .btn-principal:disabled {
                background: #aaa;
                cursor: not-allowed;
            }

            .intentos {
                text-align: center;
                font-size: 12px;
                color: #646464;
                margin-bottom: 4px;
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

            .mensaje-exito {
                background: #e0ffe0;
                color: #006600;
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
            <p class="titulo">Sistema de Gestión de Inventarios</p>
            <p class="subtitulo">Ingrese sus credenciales para continuar</p>
            <hr class="separador">

            <%-- muestra mensaje de exito si viene de registro o cambio de contrasena --%>
            <% if (exito != null && !exito.isEmpty()) {%>
            <div class="mensaje-exito"><%= exito%></div>
            <% } %>

            <%-- muestra mensaje de error si existe --%>
            <% if (error != null && !error.isEmpty()) {%>
            <div class="mensaje-error"><%= error%></div>
            <% }%>

            <form method="post" action="login">
                <label>Usuario:</label>
                <input type="text" name="nomUsuario" placeholder="" <%= bloqueado ? "disabled" : ""%> />

                <label>Contraseña</label>
                <input type="password" name="contrasena" placeholder="" <%= bloqueado ? "disabled" : ""%> />

                <div class="link-derecha">
                    <a href="cambiarContrasena.jsp">Olvidaste tu contraseña?</a>
                </div>

                <button type="submit" class="btn-principal" <%= bloqueado ? "disabled" : ""%>>
                    Ingresar
                </button>
            </form>

            <%-- muestra intentos restantes si aplica --%>
            <% if (intentosRestantes != null) {%>
            <p class="intentos">Intentos restantes: <%= intentosRestantes%></p>
            <% }%>

            <div class="link-centro">
                <a href="registro.jsp">Crea una cuenta ahora!</a>
            </div>
        </div>
    </body>
</html>