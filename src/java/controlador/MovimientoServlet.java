package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import modelo.Movimiento;
import modelo.Usuario;
import repository.MovimientoRepository;

// servlet que maneja el registro y listado de movimientos de inventario
@WebServlet(name = "MovimientoServlet", urlPatterns = {"/movimientos"})
public class MovimientoServlet extends HttpServlet {

    // repositorio para operaciones de movimiento en la bd
    private final MovimientoRepository repo = new MovimientoRepository();

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

        // carga los datos necesarios para la vista
        cargarVista(request, response);
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
            accion = "";
        }

        // ejecuta la accion correspondiente
        switch (accion) {
            case "registrar":
                registrar(request, response, usuarioActivo); // registra un nuevo movimiento
                break;
            default:
                cargarVista(request, response); // accion no reconocida, carga la vista
        }
    }

    // metodo para cargar los datos de la vista de movimientos
    private void cargarVista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // obtiene la lista de movimientos del repositorio
        List<Movimiento> movimientos = repo.listar();
        // obtiene las listas para los selects del formulario
        List<String[]> productos = repo.listarProductos();
        List<String[]> tipos = repo.listarTiposMovimiento();
        // envia los datos al jsp
        request.setAttribute("movimientos", movimientos);
        request.setAttribute("productos", productos);
        request.setAttribute("tipos", tipos);
        request.getRequestDispatcher("movimientos.jsp").forward(request, response);
    }

    // metodo para registrar un nuevo movimiento
    private void registrar(HttpServletRequest request, HttpServletResponse response, Usuario usuarioActivo)
            throws ServletException, IOException {

        // valida que los campos obligatorios no esten vacios
        String idProductoStr = request.getParameter("idProducto");
        String idTipoStr = request.getParameter("idTipoMovimiento");
        String cantidadStr = request.getParameter("cantidad");

        if (idProductoStr == null || idProductoStr.trim().isEmpty()
                || idTipoStr == null || idTipoStr.trim().isEmpty()
                || cantidadStr == null || cantidadStr.trim().isEmpty()) {
            request.setAttribute("error", "complete todos los campos obligatorios.");
            cargarVista(request, response);
            return;
        }

        // convierte los valores a sus tipos correspondientes
        int idProducto = Integer.parseInt(idProductoStr); // id del producto
        int idTipo = Integer.parseInt(idTipoStr); // id del tipo de movimiento
        int cantidad = Integer.parseInt(cantidadStr); // cantidad del movimiento

        // valida que una salida no supere el stock actual del producto
        if (idTipo == 2) {
            // busca el stock actual del producto en la lista de productos
            int stockActual = repo.listarProductos().stream()
                    .filter(p -> p[0].equals(idProductoStr)) // filtra por id del producto
                    .mapToInt(p -> Integer.parseInt(p[2])) // obtiene el stock
                    .findFirst()
                    .orElse(0);
            // si la cantidad supera el stock actual muestra un error
            if (cantidad > stockActual) {
                request.setAttribute("error", "la cantidad de salida (" + cantidad + ") supera el stock actual (" + stockActual + ").");
                cargarVista(request, response);
                return;
            }
        }

        // construye el objeto movimiento con los datos del formulario
        Movimiento m = new Movimiento();
        m.setIdProducto(idProducto); // asigna el id del producto
        m.setIdTipoMovimiento(idTipo); // asigna el tipo de movimiento
        m.setCantidad(cantidad); // asigna la cantidad
        m.setIdUsuario(usuarioActivo.getIdUsuario()); // asigna el id del usuario activo
        m.setUsuarioCreacion(usuarioActivo.getNomUsuario()); // asigna el nombre del usuario activo
        // asigna la observacion si fue ingresada
        String observacion = request.getParameter("observacion");
        m.setObservacion(observacion != null ? observacion.trim() : ""); // asigna la observacion

        // intenta registrar el movimiento en la bd
        if (repo.agregar(m)) {
            request.setAttribute("exito", "movimiento registrado correctamente.");
        } else {
            request.setAttribute("error", "error al registrar el movimiento.");
        }
        cargarVista(request, response); // vuelve a cargar la vista
    }
}
