<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Movimiento"%>
<%@page import="java.util.List"%>
<%
    // verifica que haya sesion activa, sino redirige al index
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioActivo == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // recupera los datos enviados por el servlet
    List<Movimiento> movimientos = (List<Movimiento>) request.getAttribute("movimientos");
    List<String[]> productos = (List<String[]>) request.getAttribute("productos");
    List<String[]> tipos = (List<String[]>) request.getAttribute("tipos");
    String error = (String) request.getAttribute("error");
    String exito = (String) request.getAttribute("exito");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Movimientos - Sistema de Inventarios</title>
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
        .card-wrap {
            background: white;
            border: 1px solid #ddd;
            border-radius: 6px;
            padding: 16px;
            margin-bottom: 16px;
        }
        .card-wrap h5 { font-size: 14px; font-weight: bold; color: #333; margin-bottom: 14px; }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
        .form-group { margin-bottom: 10px; }
        .form-group label { display: block; font-size: 12px; color: #333; margin-bottom: 4px; }
        .form-group .req { color: #cc0000; margin-left: 2px; }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%; padding: 6px 10px; border: 1px solid #8B0033;
            border-radius: 3px; font-size: 12px; background: white; color: #333;
        }
        .form-group textarea { height: 52px; resize: none; }
        .form-group .info-stock {
            padding: 6px 10px; border: 1px solid #ddd;
            border-radius: 3px; font-size: 12px; background: #f9f9f9; color: #555;
        }
        .leyenda { font-size: 10px; color: #cc0000; margin-top: 8px; margin-bottom: 10px; }
        .btn-guinda {
            background: #8B0033; color: white; border: none;
            padding: 8px 24px; border-radius: 3px; font-size: 12px;
            font-weight: bold; cursor: pointer;
        }
        .btn-guinda:hover { background: #6d0028; }
        .alerta-salida {
            background: #fff3cd; color: #856404;
            padding: 8px 12px; border-radius: 3px;
            font-size: 11px; margin-bottom: 10px;
            display: none;
        }
        .mov-table { width: 100%; font-size: 12px; border-collapse: collapse; }
        .mov-table th { text-align: left; color: #888; font-weight: normal; padding: 6px 8px; border-bottom: 1px solid #eee; }
        .mov-table td { padding: 7px 8px; border-bottom: 1px solid #f5f5f5; color: #333; }
        .badge-entrada { background: #e0ffe0; color: #006600; padding: 2px 8px; border-radius: 10px; font-size: 10px; }
        .badge-salida { background: #ffe0e0; color: #cc0000; padding: 2px 8px; border-radius: 10px; font-size: 10px; }
        .badge-ajuste { background: #fff3cd; color: #856404; padding: 2px 8px; border-radius: 10px; font-size: 10px; }
        .sin-datos { text-align: center; color: #aaa; font-size: 12px; padding: 20px; }
        .mensaje-error { background: #ffe0e0; color: #cc0000; padding: 8px 12px; border-radius: 3px; font-size: 12px; margin-bottom: 12px; }
        .mensaje-exito { background: #e0ffe0; color: #006600; padding: 8px 12px; border-radius: 3px; font-size: 12px; margin-bottom: 12px; }
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
            <a href="dashboard" class="btn-modulo">Dashboard</a>
            <a href="productos" class="btn-modulo">Gestión de productos</a>
            <a href="movimientos" class="btn-modulo activo">Movimientos</a>
        </div>

        <%-- mensajes de error o exito --%>
        <% if (error != null && !error.isEmpty()) { %>
            <div class="mensaje-error"><%= error %></div>
        <% } %>
        <% if (exito != null && !exito.isEmpty()) { %>
            <div class="mensaje-exito"><%= exito %></div>
        <% } %>

        <%-- formulario para registrar movimiento --%>
        <div class="card-wrap">
            <h5>Registrar movimiento</h5>
            <form method="post" action="movimientos">
                <input type="hidden" name="accion" value="registrar"/>
                <div class="form-row">
                    <div class="form-group">
                        <label>Producto <span class="req">*</span></label>
                        <select name="idProducto" id="selectProducto" onchange="actualizarStock()">
                            <option value="">Seleccione un producto</option>
                            <% if (productos != null) {
                                for (String[] p : productos) { %>
                                    <option value="<%= p[0] %>" data-stock="<%= p[2] %>">
                                        <%= p[1] %> (stock: <%= p[2] %>)
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Tipo de movimiento <span class="req">*</span></label>
                        <select name="idTipoMovimiento" id="selectTipo" onchange="verificarTipo()">
                            <% if (tipos != null) {
                                for (String[] t : tipos) { %>
                                    <option value="<%= t[0] %>"><%= t[1] %></option>
                            <%  }
                            } %>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Cantidad <span class="req">*</span></label>
                        <input type="number" name="cantidad" id="cantidad" placeholder="0" min="1"/>
                    </div>
                    <div class="form-group">
                        <label>Stock actual</label>
                        <div class="info-stock" id="infoStock">Seleccione un producto</div>
                    </div>
                </div>
                <div class="form-group">
                    <label>Observación</label>
                    <textarea name="observacion" placeholder="Motivo del movimiento..."></textarea>
                </div>
                <div class="alerta-salida" id="alertaSalida">
                    ⚠️ Verificar que la cantidad de salida no supere el stock actual.
                </div>
                <p class="leyenda">* Campo obligatorio</p>
                <button type="submit" class="btn-guinda">Registrar movimiento</button>
            </form>
        </div>

        <%-- historial de movimientos --%>
        <div class="card-wrap">
            <h5>Historial de movimientos</h5>
            <% if (movimientos == null || movimientos.isEmpty()) { %>
                <div class="sin-datos">No hay movimientos registrados aún.</div>
            <% } else { %>
                <table class="mov-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Producto</th>
                            <th>Tipo</th>
                            <th>Cantidad</th>
                            <th>Stock anterior</th>
                            <th>Stock nuevo</th>
                            <th>Fecha</th>
                            <th>Usuario</th>
                            <th>Observación</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% int i = 1;
                        for (Movimiento m : movimientos) { %>
                        <tr>
                            <td><%= i++ %></td>
                            <td><%= m.getNomProducto() %></td>
                            <td>
                                <% if ("Entrada".equals(m.getNomTipoMovimiento())) { %>
                                    <span class="badge-entrada">Entrada</span>
                                <% } else if ("Salida".equals(m.getNomTipoMovimiento())) { %>
                                    <span class="badge-salida">Salida</span>
                                <% } else { %>
                                    <span class="badge-ajuste"><%= m.getNomTipoMovimiento() %></span>
                                <% } %>
                            </td>
                            <td><%= m.getCantidad() %></td>
                            <td><%= m.getStockAnterior() %></td>
                            <td><%= m.getStockNuevo() %></td>
                            <td><%= m.getFechaMovimiento() != null ? m.getFechaMovimiento().toString().replace("T", " ") : "" %></td>
                            <td><%= m.getNomUsuario() %></td>
                            <td><%= m.getObservacion() != null ? m.getObservacion() : "" %></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </div>

    <script>
        // actualiza el stock mostrado cuando se selecciona un producto
        function actualizarStock() {
            var select = document.getElementById('selectProducto');
            var option = select.options[select.selectedIndex];
            var stock = option.getAttribute('data-stock');
            var infoStock = document.getElementById('infoStock');
            if (stock) {
                infoStock.textContent = stock + ' unidades'; // muestra el stock actual
            } else {
                infoStock.textContent = 'Seleccione un producto'; // mensaje por defecto
            }
        }

        // muestra alerta si el tipo de movimiento es salida
        function verificarTipo() {
            var select = document.getElementById('selectTipo');
            var option = select.options[select.selectedIndex];
            var alerta = document.getElementById('alertaSalida');
            // muestra la alerta solo si es una salida
            if (option.textContent.trim() === 'Salida') {
                alerta.style.display = 'block'; // muestra la alerta
            } else {
                alerta.style.display = 'none'; // oculta la alerta
            }
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>