<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%@page import="modelo.Usuario"%>
<%@page import="java.util.List"%>
<%
    // verifica que haya sesion activa, sino redirige al index
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioActivo == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // recupera los datos enviados por el servlet
    int totalProductos = (int) request.getAttribute("totalProductos");
    int totalCategorias = (int) request.getAttribute("totalCategorias");
    int stockBajo = (int) request.getAttribute("stockBajo");
    int movimientosHoy = (int) request.getAttribute("movimientosHoy");
    List<String[]> ultimosMovimientos = (List<String[]>) request.getAttribute("ultimosMovimientos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Sistema de Inventarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', sans-serif; background: #f0f0f0; }
        .header {
            background: #8B0033;
            color: white;
            padding: 14px 24px;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        .header h1 { font-size: 17px; font-weight: bold; }
        .header p { font-size: 11px; color: #f5b8c8; margin-top: 2px; }
        .btn-cerrar {
            background: rgba(255,255,255,0.15);
            color: white;
            border: 1px solid rgba(255,255,255,0.3);
            padding: 6px 14px;
            border-radius: 3px;
            font-size: 11px;
            text-decoration: none;
        }
        .btn-cerrar:hover { background: rgba(255,255,255,0.25); color: white; }
        .contenido { padding: 24px; }
        .nav-modulos { display: flex; gap: 8px; margin-bottom: 20px; flex-wrap: wrap; }
        .btn-modulo {
            background: white;
            border: 1px solid #ccc;
            border-radius: 3px;
            padding: 7px 16px;
            font-size: 12px;
            color: #333;
            text-decoration: none;
        }
        .btn-modulo:hover { background: #f5f5f5; color: #333; }
        .btn-modulo.activo { background: #8B0033; color: white; border-color: #8B0033; }
        .card-stat {
            background: white;
            border: 1px solid #ddd;
            border-radius: 6px;
            padding: 16px;
            height: 100%;
        }
        .card-stat .label { font-size: 12px; color: #888; margin-bottom: 4px; }
        .card-stat .numero { font-size: 28px; font-weight: bold; color: #8B0033; }
        .card-stat .sublabel { font-size: 11px; color: #aaa; margin-top: 2px; }
        .card-stat .icono { font-size: 22px; margin-bottom: 8px; }
        .tabla-wrap {
            background: white;
            border: 1px solid #ddd;
            border-radius: 6px;
            padding: 16px;
            margin-top: 20px;
        }
        .tabla-wrap h5 { font-size: 14px; font-weight: bold; color: #333; margin-bottom: 14px; }
        .badge-entrada { background: #e0ffe0; color: #006600; padding: 2px 10px; border-radius: 10px; font-size: 11px; }
        .badge-salida { background: #ffe0e0; color: #cc0000; padding: 2px 10px; border-radius: 10px; font-size: 11px; }
        .badge-ajuste { background: #fff3cd; color: #856404; padding: 2px 10px; border-radius: 10px; font-size: 11px; }
        .tabla-mov { font-size: 12px; width: 100%; }
        .tabla-mov th { color: #888; font-weight: normal; padding: 6px 8px; border-bottom: 1px solid #eee; }
        .tabla-mov td { padding: 7px 8px; border-bottom: 1px solid #f5f5f5; color: #333; }
        .sin-datos { text-align: center; color: #aaa; font-size: 12px; padding: 20px; }
    </style>
</head>
<body>
    <div class="header">
        <div>
            <h1>Sistema de Gestión de Inventarios</h1>
            <p>Bienvenido, <%= usuarioActivo.getNomUsuario() %></p>
        </div>
        <a href="cerrarSesion.jsp" class="btn-cerrar">Cerrar sesión</a>
    </div>

    <div class="contenido">
        <%-- navegacion de modulos --%>
        <div class="nav-modulos">
            <a href="dashboard" class="btn-modulo activo">Dashboard</a>
            <a href="productos" class="btn-modulo">Gestión de productos</a>
            <a href="movimientos" class="btn-modulo">Movimientos</a>
        </div>

        <%-- cards de estadisticas --%>
        <div class="row g-3">
            <div class="col-6 col-md-3">
                <div class="card-stat">
                    <div class="icono">📦</div>
                    <div class="label">Total productos</div>
                    <div class="numero"><%= totalProductos %></div>
                    <div class="sublabel">activos en almacén</div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="card-stat">
                    <div class="icono">📂</div>
                    <div class="label">Categorías</div>
                    <div class="numero"><%= totalCategorias %></div>
                    <div class="sublabel">registradas</div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="card-stat">
                    <div class="icono">⚠️</div>
                    <div class="label">Stock bajo</div>
                    <div class="numero"><%= stockBajo %></div>
                    <div class="sublabel">productos críticos</div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="card-stat">
                    <div class="icono">🔄</div>
                    <div class="label">Movimientos hoy</div>
                    <div class="numero"><%= movimientosHoy %></div>
                    <div class="sublabel">entradas y salidas</div>
                </div>
            </div>
        </div>

        <%-- tabla de ultimos movimientos --%>
        <div class="tabla-wrap">
            <h5>Últimos movimientos</h5>
            <% if (ultimosMovimientos == null || ultimosMovimientos.isEmpty()) { %>
                <div class="sin-datos">No hay movimientos registrados aún.</div>
            <% } else { %>
                <table class="tabla-mov">
                    <thead>
                        <tr>
                            <th>Producto</th>
                            <th>Tipo</th>
                            <th>Cantidad</th>
                            <th>Fecha</th>
                            <th>Usuario</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (String[] fila : ultimosMovimientos) { %>
                        <tr>
                            <td><%= fila[0] %></td>
                            <td>
                                <% if ("Entrada".equals(fila[1])) { %>
                                    <span class="badge-entrada">Entrada</span>
                                <% } else if ("Salida".equals(fila[1])) { %>
                                    <span class="badge-salida">Salida</span>
                                <% } else { %>
                                    <span class="badge-ajuste"><%= fila[1] %></span>
                                <% } %>
                            </td>
                            <td><%= fila[2] %></td>
                            <td><%= fila[3] %></td>
                            <td><%= fila[4] %></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>