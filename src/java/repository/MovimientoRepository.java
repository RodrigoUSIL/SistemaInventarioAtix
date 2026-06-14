package repository;

import modelo.Movimiento;
import singleton.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// implementacion del repositorio de movimientos con lambdas y streams
public class MovimientoRepository implements IMovimientoRepository {

    // metodo auxiliar para obtener la conexion a la bd
    private Connection getConn() {
        return ConexionDB.getInstancia().getConnection(); // obtiene la conexion del singleton
    }

    // metodo auxiliar para construir un movimiento desde un resultset
    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        Movimiento m = new Movimiento(); // crea un nuevo objeto movimiento
        m.setIdMovimiento(rs.getInt("id_movimiento")); // asigna el id del movimiento
        m.setCantidad(rs.getInt("cantidad")); // asigna la cantidad del movimiento
        m.setStockAnterior(rs.getInt("stock_anterior")); // asigna el stock anterior
        m.setStockNuevo(rs.getInt("stock_nuevo")); // asigna el stock nuevo
        m.setObservacion(rs.getString("observacion")); // asigna la observacion
        m.setEstado(rs.getString("estado")); // asigna el estado del movimiento
        m.setUsuarioCreacion(rs.getString("usuario_creacion")); // asigna el usuario de creacion
        m.setIdProducto(rs.getInt("id_producto")); // asigna el id del producto
        m.setIdUsuario(rs.getInt("id_usuario")); // asigna el id del usuario
        m.setIdTipoMovimiento(rs.getInt("id_tipo_movimiento")); // asigna el id del tipo de movimiento
        m.setNomProducto(rs.getString("nom_producto")); // asigna el nombre del producto
        m.setNomTipoMovimiento(rs.getString("nom_tipo_movimiento")); // asigna el tipo de movimiento
        m.setNomUsuario(rs.getString("nom_usuario")); // asigna el nombre del usuario
        // asigna la fecha del movimiento si no es nula
        Timestamp fechaMov = rs.getTimestamp("fecha_movimiento");
        if (fechaMov != null) {
            m.setFechaMovimiento(fechaMov.toLocalDateTime()); // convierte a localdatetime
        }
        return m; // retorna el movimiento mapeado
    }

    @Override
    // lista todos los movimientos activos con sus joins
    public List<Movimiento> listar() {
        List<Movimiento> lista = new ArrayList<>(); // lista donde se almacenaran los movimientos
        // consulta con joins para obtener nombres de producto, tipo y usuario
        String sql = "SELECT mi.*, p.nom_producto, tm.nom_tipo_movimiento, u.nom_usuario "
                + "FROM Movimiento_Inventario mi "
                + "JOIN Producto p ON mi.id_producto = p.id_producto "
                + "JOIN Tipo_Movimiento tm ON mi.id_tipo_movimiento = tm.id_tipo_movimiento "
                + "JOIN Usuario u ON mi.id_usuario = u.id_usuario "
                + "WHERE mi.estado = 'Activo' "
                + "ORDER BY mi.fecha_movimiento DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            while (rs.next()) {
                lista.add(mapearMovimiento(rs)); // agrega el movimiento mapeado a la lista
            }
        } catch (SQLException e) {
            System.err.println("error en listar movimientos: " + e.getMessage());
        }
        // retorna la lista usando stream para ordenar por fecha descendente
        return lista.stream()
                .sorted((a, b) -> b.getFechaMovimiento().compareTo(a.getFechaMovimiento()))
                .collect(Collectors.toList()); // colecta el resultado en una lista
    }

    @Override
    // busca un movimiento por su id retornando un optional
    public Optional<Movimiento> buscarPorId(int id) {
        // obtiene todos los movimientos y busca el que tenga el id dado con stream
        return listar().stream()
                .filter(m -> m.getIdMovimiento() == id) // filtra por id del movimiento
                .findFirst(); // retorna el primero que encuentre
    }

    @Override
    // lista movimientos filtrados por id de producto usando streams
    public List<Movimiento> listarPorProducto(int idProducto) {
        // obtiene todos los movimientos y filtra por producto con stream y lambda
        return listar().stream()
                .filter(m -> m.getIdProducto() == idProducto) // filtra por id del producto
                .collect(Collectors.toList()); // colecta el resultado en una lista
    }

    @Override
    // lista movimientos filtrados por tipo usando streams
    public List<Movimiento> listarPorTipo(int idTipoMovimiento) {
        // obtiene todos los movimientos y filtra por tipo con stream y lambda
        return listar().stream()
                .filter(m -> m.getIdTipoMovimiento() == idTipoMovimiento) // filtra por tipo
                .collect(Collectors.toList()); // colecta el resultado en una lista
    }

    @Override
    // registra un nuevo movimiento y actualiza el stock del producto
    public boolean agregar(Movimiento m) {
        Connection conn = getConn(); // obtiene la conexion a la bd
        try {
            conn.setAutoCommit(false); // inicia la transaccion para asegurar consistencia

            // primero obtiene el stock actual del producto
            int stockActual = 0;
            String sqlStock = "SELECT stock FROM Producto WHERE id_producto = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                ps.setInt(1, m.getIdProducto()); // asigna el id del producto
                ResultSet rs = ps.executeQuery(); // ejecuta la consulta
                if (rs.next()) {
                    stockActual = rs.getInt("stock"); // obtiene el stock actual
                }
            }

            // calcula el stock nuevo segun el tipo de movimiento
            int stockNuevo;
            if (m.getIdTipoMovimiento() == 1) {
                stockNuevo = stockActual + m.getCantidad(); // entrada suma al stock
            } else if (m.getIdTipoMovimiento() == 2) {
                stockNuevo = stockActual - m.getCantidad(); // salida resta al stock
            } else {
                stockNuevo = stockActual + m.getCantidad(); // ajuste suma o resta segun cantidad
            }

            // asigna el stock anterior y nuevo al movimiento
            m.setStockAnterior(stockActual); // guarda el stock antes del movimiento
            m.setStockNuevo(stockNuevo); // guarda el stock despues del movimiento

            // inserta el movimiento en la bd
            String sqlMovimiento = "INSERT INTO Movimiento_Inventario (cantidad, stock_anterior, stock_nuevo, fecha_movimiento, observacion, estado, usuario_creacion, fecha_creacion, id_producto, id_usuario, id_tipo_movimiento) "
                    + "VALUES (?, ?, ?, NOW(), ?, 'Activo', ?, CURDATE(), ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlMovimiento)) {
                ps.setInt(1, m.getCantidad()); // asigna la cantidad del movimiento
                ps.setInt(2, m.getStockAnterior()); // asigna el stock anterior
                ps.setInt(3, m.getStockNuevo()); // asigna el stock nuevo
                ps.setString(4, m.getObservacion()); // asigna la observacion
                ps.setString(5, m.getUsuarioCreacion()); // asigna el usuario de creacion
                ps.setInt(6, m.getIdProducto()); // asigna el id del producto
                ps.setInt(7, m.getIdUsuario()); // asigna el id del usuario
                ps.setInt(8, m.getIdTipoMovimiento()); // asigna el id del tipo de movimiento
                ps.executeUpdate(); // ejecuta la insercion
            }

            // actualiza el stock del producto en la tabla producto
            String sqlActualizar = "UPDATE Producto SET stock = ?, ultimo_acceso = NOW() WHERE id_producto = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlActualizar)) {
                ps.setInt(1, stockNuevo); // asigna el nuevo stock
                ps.setInt(2, m.getIdProducto()); // asigna el id del producto
                ps.executeUpdate(); // ejecuta la actualizacion
            }

            conn.commit(); // confirma la transaccion si todo salio bien
            return true; // retorna true si se registro correctamente
        } catch (SQLException e) {
            System.err.println("error en agregar movimiento: " + e.getMessage());
            try {
                conn.rollback(); // revierte la transaccion si hubo un error
            } catch (SQLException ex) {
                System.err.println("error en rollback: " + ex.getMessage());
            }
            return false; // retorna false si hubo un error
        } finally {
            try {
                conn.setAutoCommit(true); // restaura el autocommit
            } catch (SQLException e) {
                System.err.println("error restaurando autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    // editar no aplica para movimientos, son registros permanentes
    public boolean editar(Movimiento m) {
        return false; // los movimientos no se editan
    }

    @Override
    // elimina logicamente un movimiento cambiando su estado a inactivo
    public boolean eliminar(int id) {
        String sql = "UPDATE Movimiento_Inventario SET estado = 'Inactivo' WHERE id_movimiento = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id); // asigna el id del movimiento a eliminar
            return ps.executeUpdate() > 0; // retorna true si se actualizo correctamente
        } catch (SQLException e) {
            System.err.println("error en eliminar movimiento: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // lista los productos disponibles para el formulario de movimientos
    public List<String[]> listarProductos() {
        List<String[]> lista = new ArrayList<>(); // lista donde se almacenaran los productos
        String sql = "SELECT id_producto, nom_producto, stock FROM Producto WHERE estado = 'Activo' ORDER BY nom_producto";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id_producto")), // id del producto
                    rs.getString("nom_producto"), // nombre del producto
                    String.valueOf(rs.getInt("stock")) // stock actual del producto
                });
            }
        } catch (SQLException e) {
            System.err.println("error en listar productos para movimientos: " + e.getMessage());
        }
        return lista; // retorna la lista de productos
    }

    @Override
    // lista los tipos de movimiento disponibles para el formulario
    public List<String[]> listarTiposMovimiento() {
        List<String[]> lista = new ArrayList<>(); // lista donde se almacenaran los tipos
        String sql = "SELECT id_tipo_movimiento, nom_tipo_movimiento FROM Tipo_Movimiento WHERE estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id_tipo_movimiento")), // id del tipo
                    rs.getString("nom_tipo_movimiento") // nombre del tipo
                });
            }
        } catch (SQLException e) {
            System.err.println("error en listar tipos de movimiento: " + e.getMessage());
        }
        return lista; // retorna la lista de tipos de movimiento
    }
}
