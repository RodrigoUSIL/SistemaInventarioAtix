<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%
    // si ya hay sesion activa redirige al dashboard
    if (session.getAttribute("usuarioActivo") != null) {
        response.sendRedirect("dashboard.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Distribuidora Atix S.A.C. - Sistema de Inventarios</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f0f0f0;
            min-height: 100vh;
        }
        .header {
            background: #8B0033;
            padding: 14px 32px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .header .brand {
            color: white;
            font-size: 16px;
            font-weight: bold;
        }
        .header .brand span {
            font-size: 11px;
            color: #f5b8c8;
            display: block;
            font-weight: normal;
        }
        .hero {
            padding: 60px 32px 40px;
            text-align: center;
        }
        .hero h1 {
            font-size: 26px;
            font-weight: bold;
            color: #8B0033;
            margin-bottom: 10px;
        }
        .hero p {
            font-size: 14px;
            color: #555;
            max-width: 420px;
            margin: 0 auto 28px;
            line-height: 1.6;
        }
        .btn-guinda {
            background: #8B0033;
            color: white;
            border: none;
            padding: 11px 32px;
            border-radius: 3px;
            font-size: 13px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-guinda:hover { background: #6d0028; }
        .btn-outline {
            background: white;
            color: #8B0033;
            border: 1px solid #8B0033;
            padding: 10px 32px;
            border-radius: 3px;
            font-size: 13px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin-left: 10px;
        }
        .btn-outline:hover { background: #f5f5f5; }
        .cards {
            display: flex;
            gap: 16px;
            justify-content: center;
            padding: 0 32px 40px;
            flex-wrap: wrap;
        }
        .card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 20px;
            width: 160px;
            text-align: center;
        }
        .card .icono {
            font-size: 28px;
            color: #8B0033;
            margin-bottom: 8px;
        }
        .card p {
            font-size: 12px;
            color: #555;
            line-height: 1.5;
        }
        .footer {
            background: #8B0033;
            color: #f5b8c8;
            text-align: center;
            padding: 12px;
            font-size: 11px;
            position: fixed;
            bottom: 0;
            width: 100%;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="brand">
            Distribuidora Atix S.A.C.
            <span>Sistema de Gestión de Inventarios</span>
        </div>
    </div>

    <div class="hero">
        <h1>Bienvenido al sistema de inventarios</h1>
        <p>Plataforma interna para la administración de productos, control de stock y movimientos del almacén de Distribuidora Atix S.A.C., Ayacucho.</p>
        <a href="login.jsp" class="btn-guinda">Iniciar sesión</a>
        <a href="registro.jsp" class="btn-outline">Crear cuenta</a>
    </div>

    <div class="cards">
        <div class="card">
            <div class="icono">📦</div>
            <p>Control de productos y stock en tiempo real</p>
        </div>
        <div class="card">
            <div class="icono">🔄</div>
            <p>Registro de entradas y salidas del almacén</p>
        </div>
        <div class="card">
            <div class="icono">🔍</div>
            <p>Filtrado rápido por categoría de producto</p>
        </div>
        <div class="card">
            <div class="icono">🔒</div>
            <p>Acceso seguro con roles de usuario</p>
        </div>
    </div>

    <div class="footer">
        © 2025 Distribuidora Atix S.A.C. — Ayacucho, Perú
    </div>
</body>
</html>