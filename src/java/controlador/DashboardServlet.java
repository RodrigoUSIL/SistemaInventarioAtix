package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import singleton.ConexionDB;

// servlet que carga los datos del dashboard principal
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    // metodo auxiliar para obtener la conexion a la bd
    private Connection getConn() {
        return ConexionDB.getInstancia().getConnection(); // obtiene la conexion del singleton
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // verifica que haya sesion activa, sino redirige al login
        HttpSession session = request.getSession();
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
        if (usuarioActivo == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // consulta el total de productos activos
        int totalProductos = 0;
        String sqlProductos = "SELECT COUNT(*) FROM Producto WHERE estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sqlProductos)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalProductos = rs.getInt(1); // obtiene el total de productos
            }
        } catch (SQLException e) {
            System.err.println("error contando productos: " + e.getMessage());
        }

        // consulta el total de categorias activas
        int totalCategorias = 0;
        String sqlCategorias = "SELECT COUNT(*) FROM Categoria WHERE estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sqlCategorias)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalCategorias = rs.getInt(1); // obtiene el total de categorias
            }
        } catch (SQLException e) {
            System.err.println("error contando categorias: " + e.getMessage());
        }

        // consulta productos con stock bajo, menos de 10 unidades
        int stockBajo = 0;
        String sqlStockBajo = "SELECT COUNT(*) FROM Producto WHERE stock < 10 AND estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sqlStockBajo)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stockBajo = rs.getInt(1); // obtiene el total de productos con stock bajo
            }
        } catch (SQLException e) {
            System.err.println("error contando stock bajo: " + e.getMessage());
        }

        // consulta el total de movimientos de hoy
        int movimientosHoy = 0;
        String sqlMovimientos = "SELECT COUNT(*) FROM Movimiento_Inventario WHERE DATE(fecha_movimiento) = CURDATE()";
        try (PreparedStatement ps = getConn().prepareStatement(sqlMovimientos)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                movimientosHoy = rs.getInt(1); // obtiene el total de movimientos de hoy
            }
        } catch (SQLException e) {
            System.err.println("error contando movimientos: " + e.getMessage());
        }

        // consulta los ultimos 5 movimientos registrados
        List<String[]> ultimosMovimientos = new ArrayList<>();
        String sqlUltimos = "SELECT p.nom_producto, tm.nom_tipo_movimiento, mi.cantidad, mi.fecha_movimiento, mi.usuario_creacion "
                + "FROM Movimiento_Inventario mi "
                + "JOIN Producto p ON mi.id_producto = p.id_producto "
                + "JOIN Tipo_Movimiento tm ON mi.id_tipo_movimiento = tm.id_tipo_movimiento "
                + "ORDER BY mi.fecha_movimiento DESC LIMIT 5";
        try (PreparedStatement ps = getConn().prepareStatement(sqlUltimos)) {
            ResultSet rs = ps.executeQuery();
            // recorre los resultados y los agrega a la lista
            while (rs.next()) {
                String[] fila = new String[5];
                fila[0] = rs.getString("nom_producto");       // nombre del producto
                fila[1] = rs.getString("nom_tipo_movimiento"); // tipo de movimiento
                fila[2] = String.valueOf(rs.getInt("cantidad")); // cantidad
                fila[3] = rs.getString("fecha_movimiento");   // fecha del movimiento
                fila[4] = rs.getString("usuario_creacion");   // usuario que registro
                ultimosMovimientos.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("error cargando ultimos movimientos: " + e.getMessage());
        }

        // envia los datos al jsp del dashboard
        request.setAttribute("totalProductos", totalProductos);
        request.setAttribute("totalCategorias", totalCategorias);
        request.setAttribute("stockBajo", stockBajo);
        request.setAttribute("movimientosHoy", movimientosHoy);
        request.setAttribute("ultimosMovimientos", ultimosMovimientos);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // redirige el post al get
        doGet(request, response);
    }
}
