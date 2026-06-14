package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import modelo.Producto;
import modelo.Usuario;
import repository.ProductoRepository;

// servlet que maneja el crud completo de productos
@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    // repositorio para operaciones de producto en la bd
    private final ProductoRepository repo = new ProductoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // verifica que haya sesion activa, sino redirige al index
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
        if (usuarioActivo == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // obtiene la accion solicitada, por defecto lista todos los productos
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; // accion por defecto
        }

        // ejecuta la accion correspondiente
        switch (accion) {
            case "listar":
                listar(request, response); // lista todos los productos
                break;
            case "filtrar":
                filtrar(request, response); // filtra por categoria
                break;
            case "eliminar":
                eliminar(request, response, usuarioActivo); // elimina un producto
                break;
            default:
                listar(request, response); // accion no reconocida, lista todos
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // verifica que haya sesion activa, sino redirige al index
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
        if (usuarioActivo == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // obtiene la accion solicitada desde el formulario
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = ""; // accion vacia por defecto
        }

        // ejecuta la accion correspondiente
        switch (accion) {
            case "agregar":
                agregar(request, response, usuarioActivo); // agrega un nuevo producto
                break;
            case "editar":
                editar(request, response, usuarioActivo); // edita un producto existente
                break;
            default:
                listar(request, response); // accion no reconocida, lista todos
        }
    }

    // metodo para listar todos los productos activos
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // obtiene la lista de productos del repositorio
        List<Producto> productos = repo.listar();
        // obtiene las listas para los selects del formulario
        List<String[]> categorias = repo.listarCategorias();
        List<String[]> marcas = repo.listarMarcas();
        List<String[]> proveedores = repo.listarProveedores();
        // envia los datos al jsp
        request.setAttribute("productos", productos);
        request.setAttribute("categorias", categorias);
        request.setAttribute("marcas", marcas);
        request.setAttribute("proveedores", proveedores);
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }

    // metodo para filtrar productos por categoria
    private void filtrar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // obtiene el id de categoria del parametro
        String idCatStr = request.getParameter("idCategoria");
        // si no hay categoria seleccionada lista todos los productos
        if (idCatStr == null || idCatStr.trim().isEmpty()) {
            listar(request, response);
            return;
        }
        // convierte el id de categoria a entero y filtra
        int idCategoria = Integer.parseInt(idCatStr);
        List<Producto> productos = repo.listarPorCategoria(idCategoria); // filtra por categoria
        List<String[]> categorias = repo.listarCategorias(); // obtiene categorias para el filtro
        List<String[]> marcas = repo.listarMarcas(); // obtiene marcas para el formulario
        List<String[]> proveedores = repo.listarProveedores(); // obtiene proveedores para el formulario
        // envia los datos al jsp
        request.setAttribute("productos", productos);
        request.setAttribute("categorias", categorias);
        request.setAttribute("marcas", marcas);
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("idCategoriaSeleccionada", idCategoria); // para mantener el filtro activo
        request.getRequestDispatcher("productos.jsp").forward(request, response);
    }

    // metodo para agregar un nuevo producto
    private void agregar(HttpServletRequest request, HttpServletResponse response, Usuario usuarioActivo)
            throws ServletException, IOException {
        // valida que los campos obligatorios no esten vacios
        String nomProducto = request.getParameter("nomProducto");
        if (nomProducto == null || nomProducto.trim().isEmpty()) {
            request.setAttribute("error", "el nombre del producto es obligatorio.");
            listar(request, response);
            return;
        }
        // construye el objeto producto con los datos del formulario
        Producto p = new Producto();
        p.setNomProducto(nomProducto.trim()); // asigna el nombre del producto
        p.setDescProducto(request.getParameter("descProducto")); // asigna la descripcion
        p.setUnidadMedida(request.getParameter("unidadMedida")); // asigna la unidad de medida
        p.setPrecioCompra(Double.parseDouble(request.getParameter("precioCompra"))); // asigna el precio de compra
        p.setPrecioVenta(Double.parseDouble(request.getParameter("precioVenta"))); // asigna el precio de venta
        p.setStock(Integer.parseInt(request.getParameter("stock"))); // asigna el stock inicial
        p.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria"))); // asigna la categoria
        p.setIdMarca(Integer.parseInt(request.getParameter("idMarca"))); // asigna la marca
        p.setIdProveedor(Integer.parseInt(request.getParameter("idProveedor"))); // asigna el proveedor
        p.setUsuarioCreacion(usuarioActivo.getNomUsuario()); // asigna el usuario activo como creador
        // asigna la fecha de vencimiento si fue ingresada
        String fechaVenc = request.getParameter("fechaVencimiento");
        if (fechaVenc != null && !fechaVenc.trim().isEmpty()) {
            p.setFechaVencimiento(LocalDate.parse(fechaVenc)); // convierte el string a localdate
        }
        // intenta insertar el producto en la bd
        if (repo.agregar(p)) {
            request.setAttribute("exito", "producto agregado correctamente.");
        } else {
            request.setAttribute("error", "error al agregar el producto.");
        }
        listar(request, response); // vuelve a listar los productos
    }

    // metodo para editar un producto existente
    private void editar(HttpServletRequest request, HttpServletResponse response, Usuario usuarioActivo)
            throws ServletException, IOException {
        // valida que los campos obligatorios no esten vacios
        String nomProducto = request.getParameter("nomProducto");
        if (nomProducto == null || nomProducto.trim().isEmpty()) {
            request.setAttribute("error", "el nombre del producto es obligatorio.");
            listar(request, response);
            return;
        }
        // construye el objeto producto con los datos del formulario
        Producto p = new Producto();
        p.setIdProducto(Integer.parseInt(request.getParameter("idProducto"))); // asigna el id del producto
        p.setNomProducto(nomProducto.trim()); // asigna el nombre del producto
        p.setDescProducto(request.getParameter("descProducto")); // asigna la descripcion
        p.setUnidadMedida(request.getParameter("unidadMedida")); // asigna la unidad de medida
        p.setPrecioCompra(Double.parseDouble(request.getParameter("precioCompra"))); // asigna el precio de compra
        p.setPrecioVenta(Double.parseDouble(request.getParameter("precioVenta"))); // asigna el precio de venta
        p.setStock(Integer.parseInt(request.getParameter("stock"))); // asigna el stock
        p.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria"))); // asigna la categoria
        p.setIdMarca(Integer.parseInt(request.getParameter("idMarca"))); // asigna la marca
        p.setIdProveedor(Integer.parseInt(request.getParameter("idProveedor"))); // asigna el proveedor
        p.setUsuarioModificacion(usuarioActivo.getNomUsuario()); // asigna el usuario activo como modificador
        // asigna la fecha de vencimiento si fue ingresada
        String fechaVenc = request.getParameter("fechaVencimiento");
        if (fechaVenc != null && !fechaVenc.trim().isEmpty()) {
            p.setFechaVencimiento(LocalDate.parse(fechaVenc)); // convierte el string a localdate
        }
        // intenta actualizar el producto en la bd
        if (repo.editar(p)) {
            request.setAttribute("exito", "producto actualizado correctamente.");
        } else {
            request.setAttribute("error", "error al actualizar el producto.");
        }
        listar(request, response); // vuelve a listar los productos
    }

    // metodo para eliminar logicamente un producto
    private void eliminar(HttpServletRequest request, HttpServletResponse response, Usuario usuarioActivo)
            throws ServletException, IOException {
        // obtiene el id del producto a eliminar
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("error", "id de producto invalido.");
            listar(request, response);
            return;
        }
        // intenta eliminar logicamente el producto en la bd
        int id = Integer.parseInt(idStr);
        if (repo.eliminar(id)) {
            request.setAttribute("exito", "producto eliminado correctamente.");
        } else {
            request.setAttribute("error", "error al eliminar el producto.");
        }
        listar(request, response); // vuelve a listar los productos
    }
}
