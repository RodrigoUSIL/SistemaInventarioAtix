<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Producto"%>
<%@page import="java.util.List"%>
<%
    // verifica que haya sesion activa, sino redirige al index
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioActivo == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // recupera los datos enviados por el servlet
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    List<String[]> categorias = (List<String[]>) request.getAttribute("categorias");
    List<String[]> marcas = (List<String[]>) request.getAttribute("marcas");
    List<String[]> proveedores = (List<String[]>) request.getAttribute("proveedores");
    String error = (String) request.getAttribute("error");
    String exito = (String) request.getAttribute("exito");
    Integer idCategoriaSeleccionada = (Integer) request.getAttribute("idCategoriaSeleccionada");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Productos - Sistema de Inventarios</title>
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
        .tabla-wrap {
            background: white;
            border: 1px solid #ddd;
            border-radius: 6px;
            padding: 16px;
        }
        .toolbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 14px;
            flex-wrap: wrap;
            gap: 10px;
        }
        .toolbar h5 { font-size: 14px; font-weight: bold; color: #333; margin: 0; }
        .filtros { display: flex; gap: 8px; align-items: center; flex-wrap: wrap; }
        .filtros select {
            padding: 5px 10px;
            border: 1px solid #8B0033;
            border-radius: 3px;
            font-size: 12px;
            color: #333;
            background: white;
        }
        .btn-guinda {
            background: #8B0033;
            color: white;
            border: none;
            padding: 6px 14px;
            border-radius: 3px;
            font-size: 12px;
            cursor: pointer;
            text-decoration: none;
        }
        .btn-guinda:hover { background: #6d0028; color: white; }
        .prod-table { width: 100%; font-size: 12px; border-collapse: collapse; }
        .prod-table th { text-align: left; color: #888; font-weight: normal; padding: 6px 8px; border-bottom: 1px solid #eee; }
        .prod-table td { padding: 7px 8px; border-bottom: 1px solid #f5f5f5; color: #333; }
        .badge-ok { background: #e0ffe0; color: #006600; padding: 2px 8px; border-radius: 10px; font-size: 10px; }
        .badge-bajo { background: #ffe0e0; color: #cc0000; padding: 2px 8px; border-radius: 10px; font-size: 10px; }
        .badge-activo { background: #e0ffe0; color: #006600; padding: 2px 8px; border-radius: 10px; font-size: 10px; }
        .btn-edit { background: #f0f0f0; border: none; padding: 3px 8px; border-radius: 3px; font-size: 11px; cursor: pointer; margin-right: 4px; }
        .btn-del { background: #ffe0e0; color: #cc0000; border: none; padding: 3px 8px; border-radius: 3px; font-size: 11px; cursor: pointer; }
        .sin-datos { text-align: center; color: #aaa; font-size: 12px; padding: 20px; }
        .mensaje-error { background: #ffe0e0; color: #cc0000; padding: 8px 12px; border-radius: 3px; font-size: 12px; margin-bottom: 12px; }
        .mensaje-exito { background: #e0ffe0; color: #006600; padding: 8px 12px; border-radius: 3px; font-size: 12px; margin-bottom: 12px; }
        .modal-overlay { display: none; position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.4); z-index: 1000; align-items: flex-start; justify-content: center; padding-top: 30px; }
        .modal-overlay.activo { display: flex; }
        .modal-card { background: white; border: 2px solid #8B0033; border-radius: 4px; padding: 22px 26px; width: 500px; max-height: 85vh; overflow-y: auto; }
        .modal-card .titulo { color: #8B0033; font-size: 15px; font-weight: bold; margin-bottom: 3px; }
        .modal-card .subtitulo { color: #888; font-size: 11px; margin-bottom: 12px; }
        .sep { border: none; border-top: 1.5px solid #8B0033; margin-bottom: 14px; }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
        .form-group { margin-bottom: 10px; }
        .form-group label { display: block; font-size: 12px; color: #333; margin-bottom: 4px; }
        .form-group .req { color: #cc0000; margin-left: 2px; }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%; padding: 6px 10px; border: 1px solid #8B0033;
            border-radius: 3px; font-size: 12px; background: white; color: #333;
        }
        .form-group textarea { height: 52px; resize: none; }
        .leyenda { font-size: 10px; color: #cc0000; margin-top: 8px; }
        .modal-btns { display: flex; gap: 8px; margin-top: 12px; }
        .btn-guardar { flex: 1; background: #8B0033; color: white; border: none; padding: 8px; border-radius: 3px; font-size: 12px; font-weight: bold; cursor: pointer; }
        .btn-guardar:hover { background: #6d0028; }
        .btn-cancelar { flex: 1; background: white; color: #8B0033; border: 1px solid #8B0033; padding: 8px; border-radius: 3px; font-size: 12px; cursor: pointer; }
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
            <a href="productos" class="btn-modulo activo">Gestión de productos</a>
            <a href="#" class="btn-modulo">Movimientos</a>
        </div>

        <%-- mensajes de error o exito --%>
        <% if (error != null && !error.isEmpty()) { %>
            <div class="mensaje-error"><%= error %></div>
        <% } %>
        <% if (exito != null && !exito.isEmpty()) { %>
            <div class="mensaje-exito"><%= exito %></div>
        <% } %>

        <div class="tabla-wrap">
            <div class="toolbar">
                <h5>Productos</h5>
                <div class="filtros">
                    <%-- filtro por categoria --%>
                    <form method="get" action="productos" style="display:inline;">
                        <input type="hidden" name="accion" value="filtrar"/>
                        <select name="idCategoria" onchange="this.form.submit()">
                            <option value="">Todas las categorías</option>
                            <% if (categorias != null) {
                                for (String[] cat : categorias) { %>
                                    <option value="<%= cat[0] %>" <%= (idCategoriaSeleccionada != null && idCategoriaSeleccionada == Integer.parseInt(cat[0])) ? "selected" : "" %>>
                                        <%= cat[1] %>
                                    </option>
                            <%  }
                            } %>
                        </select>
                    </form>
                    <button class="btn-guinda" onclick="abrirModal('agregar', null)">+ Agregar producto</button>
                </div>
            </div>

            <%-- tabla de productos --%>
            <% if (productos == null || productos.isEmpty()) { %>
                <div class="sin-datos">No hay productos registrados.</div>
            <% } else { %>
                <table class="prod-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Marca</th>
                            <th>P. Compra</th>
                            <th>P. Venta</th>
                            <th>Stock</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% int i = 1;
                        for (Producto p : productos) { %>
                        <tr>
                            <td><%= i++ %></td>
                            <td><%= p.getNomProducto() %></td>
                            <td><%= p.getNomCategoria() %></td>
                            <td><%= p.getNomMarca() %></td>
                            <td>S/ <%= String.format("%.2f", p.getPrecioCompra()) %></td>
                            <td>S/ <%= String.format("%.2f", p.getPrecioVenta()) %></td>
                            <td>
                                <% if (p.getStock() < 10) { %>
                                    <span class="badge-bajo">Bajo (<%= p.getStock() %>)</span>
                                <% } else { %>
                                    <span class="badge-ok">OK (<%= p.getStock() %>)</span>
                                <% } %>
                            </td>
                            <td><span class="badge-activo">Activo</span></td>
                            <td>
                                <button class="btn-edit" onclick="abrirModal('editar', {
                                    id: '<%= p.getIdProducto() %>',
                                    nombre: '<%= p.getNomProducto().replace("'", "\\'") %>',
                                    desc: '<%= p.getDescProducto() != null ? p.getDescProducto().replace("'", "\\'") : "" %>',
                                    unidad: '<%= p.getUnidadMedida() %>',
                                    pcompra: '<%= p.getPrecioCompra() %>',
                                    pventa: '<%= p.getPrecioVenta() %>',
                                    stock: '<%= p.getStock() %>',
                                    fechaVenc: '<%= p.getFechaVencimiento() != null ? p.getFechaVencimiento().toString() : "" %>',
                                    idCategoria: '<%= p.getIdCategoria() %>',
                                    idMarca: '<%= p.getIdMarca() %>',
                                    idProveedor: '<%= p.getIdProveedor() %>'
                                })">Editar</button>
                                <a href="productos?accion=eliminar&id=<%= p.getIdProducto() %>"
                                   class="btn-del"
                                   onclick="return confirm('¿Está seguro de eliminar este producto?')">Eliminar</a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </div>

    <%-- modal para agregar y editar productos --%>
    <div id="modalOverlay" class="modal-overlay">
        <div class="modal-card">
            <p class="titulo" id="modalTitulo">Agregar producto</p>
            <p class="subtitulo" id="modalSubtitulo">Complete los campos para registrar el producto</p>
            <hr class="sep">
            <form method="post" action="productos" id="formProducto">
                <input type="hidden" name="accion" id="modalAccion" value="agregar"/>
                <input type="hidden" name="idProducto" id="campoId" value=""/>
                <div class="form-row">
                    <div class="form-group">
                        <label>Nombre <span class="req">*</span></label>
                        <input type="text" name="nomProducto" id="campoNombre" placeholder="Ej: Inca Kola 1.5L"/>
                    </div>
                    <div class="form-group">
                        <label>Unidad de medida <span class="req">*</span></label>
                        <select name="unidadMedida" id="campoUnidad">
                            <option value="unidad">unidad</option>
                            <option value="bolsa">bolsa</option>
                            <option value="caja">caja</option>
                            <option value="paquete">paquete</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>Descripción</label>
                    <textarea name="descProducto" id="campoDesc" placeholder="Descripción del producto..."></textarea>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Precio compra S/ <span class="req">*</span></label>
                        <input type="number" step="0.01" name="precioCompra" id="campoPCompra" placeholder="0.00"/>
                    </div>
                    <div class="form-group">
                        <label>Precio venta S/ <span class="req">*</span></label>
                        <input type="number" step="0.01" name="precioVenta" id="campoPVenta" placeholder="0.00"/>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Stock <span class="req">*</span></label>
                        <input type="number" name="stock" id="campoStock" placeholder="0"/>
                    </div>
                    <div class="form-group">
                        <label>Fecha vencimiento</label>
                        <input type="date" name="fechaVencimiento" id="campoFechaVenc"/>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Categoría <span class="req">*</span></label>
                        <select name="idCategoria" id="campoCategoria">
                            <% if (categorias != null) {
                                for (String[] cat : categorias) { %>
                                    <option value="<%= cat[0] %>"><%= cat[1] %></option>
                            <%  }
                            } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Marca <span class="req">*</span></label>
                        <select name="idMarca" id="campoMarca">
                            <% if (marcas != null) {
                                for (String[] m : marcas) { %>
                                    <option value="<%= m[0] %>"><%= m[1] %></option>
                            <%  }
                            } %>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>Proveedor <span class="req">*</span></label>
                    <select name="idProveedor" id="campoProveedor">
                        <% if (proveedores != null) {
                            for (String[] prov : proveedores) { %>
                                <option value="<%= prov[0] %>"><%= prov[1] %></option>
                        <%  }
                        } %>
                    </select>
                </div>
                <p class="leyenda">* Campo obligatorio</p>
                <div class="modal-btns">
                    <button type="submit" class="btn-guardar">Guardar</button>
                    <button type="button" class="btn-cancelar" onclick="cerrarModal()">Cancelar</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // abre el modal para agregar o editar un producto
        function abrirModal(modo, datos) {
            var overlay = document.getElementById('modalOverlay');
            var titulo = document.getElementById('modalTitulo');
            var subtitulo = document.getElementById('modalSubtitulo');
            var accion = document.getElementById('modalAccion');

            if (modo === 'agregar') {
                // configura el modal para agregar
                titulo.textContent = 'Agregar producto';
                subtitulo.textContent = 'Complete los campos para registrar el producto';
                accion.value = 'agregar';
                // limpia los campos del formulario
                document.getElementById('campoId').value = '';
                document.getElementById('campoNombre').value = '';
                document.getElementById('campoDesc').value = '';
                document.getElementById('campoPCompra').value = '';
                document.getElementById('campoPVenta').value = '';
                document.getElementById('campoStock').value = '';
                document.getElementById('campoFechaVenc').value = '';
            } else {
                // configura el modal para editar con los datos del producto
                titulo.textContent = 'Editar producto';
                subtitulo.textContent = 'Modifique los campos que desea actualizar';
                accion.value = 'editar';
                // carga los datos del producto en el formulario
                document.getElementById('campoId').value = datos.id;
                document.getElementById('campoNombre').value = datos.nombre;
                document.getElementById('campoDesc').value = datos.desc;
                document.getElementById('campoUnidad').value = datos.unidad;
                document.getElementById('campoPCompra').value = datos.pcompra;
                document.getElementById('campoPVenta').value = datos.pventa;
                document.getElementById('campoStock').value = datos.stock;
                document.getElementById('campoFechaVenc').value = datos.fechaVenc;
                document.getElementById('campoCategoria').value = datos.idCategoria;
                document.getElementById('campoMarca').value = datos.idMarca;
                document.getElementById('campoProveedor').value = datos.idProveedor;
            }
            overlay.classList.add('activo'); // muestra el modal
        }

        // cierra el modal
        function cerrarModal() {
            document.getElementById('modalOverlay').classList.remove('activo');
        }

        // cierra el modal al hacer clic fuera de el
        document.getElementById('modalOverlay').addEventListener('click', function(e) {
            if (e.target === this) cerrarModal();
        });
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>