package repository;

import modelo.Producto;
import singleton.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// implementacion del repositorio de productos con lambdas y streams
public class ProductoRepository implements IProductoRepository {

    // metodo auxiliar para obtener la conexion a la bd
    private Connection getConn() {
        return ConexionDB.getInstancia().getConnection(); // obtiene la conexion del singleton
    }

    // metodo auxiliar para construir un producto desde un resultset
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto(); // crea un nuevo objeto producto
        p.setIdProducto(rs.getInt("id_producto")); // asigna el id del producto
        p.setNomProducto(rs.getString("nom_producto")); // asigna el nombre del producto
        p.setDescProducto(rs.getString("desc_producto")); // asigna la descripcion del producto
        p.setUnidadMedida(rs.getString("unidad_medida")); // asigna la unidad de medida
        p.setPrecioCompra(rs.getDouble("precio_compra")); // asigna el precio de compra
        p.setPrecioVenta(rs.getDouble("precio_venta")); // asigna el precio de venta
        p.setStock(rs.getInt("stock")); // asigna el stock del producto
        p.setEstado(rs.getString("estado")); // asigna el estado del producto
        p.setIdCategoria(rs.getInt("id_categoria")); // asigna el id de la categoria
        p.setIdMarca(rs.getInt("id_marca")); // asigna el id de la marca
        p.setIdProveedor(rs.getInt("id_proveedor")); // asigna el id del proveedor
        p.setUsuarioCreacion(rs.getString("usuario_creacion")); // asigna el usuario de creacion
        p.setNomCategoria(rs.getString("nom_categoria")); // asigna el nombre de la categoria
        p.setNomMarca(rs.getString("nom_marca")); // asigna el nombre de la marca
        p.setNomProveedor(rs.getString("nom_proveedor")); // asigna el nombre del proveedor
        // asigna la fecha de vencimiento si no es nula
        Date fechaVenc = rs.getDate("fecha_vencimiento");
        if (fechaVenc != null) {
            p.setFechaVencimiento(fechaVenc.toLocalDate()); // convierte a localdate
        }
        return p; // retorna el producto mapeado
    }

    @Override
    // lista todos los productos activos con sus joins a categoria, marca y proveedor
    public List<Producto> listar() {
        // lista donde se almacenaran los productos obtenidos de la bd
        List<Producto> lista = new ArrayList<>();
        // consulta con joins para obtener nombres de categoria, marca y proveedor
        String sql = "SELECT p.*, c.nom_categoria, m.nom_marca, pr.razon_social AS nom_proveedor "
                + "FROM Producto p "
                + "JOIN Categoria c ON p.id_categoria = c.id_categoria "
                + "JOIN Marca m ON p.id_marca = m.id_marca "
                + "JOIN Proveedor pr ON p.id_proveedor = pr.id_proveedor "
                + "WHERE p.estado = 'Activo' "
                + "ORDER BY p.nom_producto";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            // recorre los resultados y los mapea a objetos producto usando lambda
            while (rs.next()) {
                lista.add(mapearProducto(rs)); // agrega el producto mapeado a la lista
            }
        } catch (SQLException e) {
            System.err.println("error en listar productos: " + e.getMessage());
        }
        // retorna la lista usando stream para ordenar por nombre
        return lista.stream()
                .sorted((a, b) -> a.getNomProducto().compareToIgnoreCase(b.getNomProducto()))
                .collect(Collectors.toList()); // colecta el resultado en una lista
    }

    @Override
    // lista productos filtrados por categoria usando streams
    public List<Producto> listarPorCategoria(int idCategoria) {
        // obtiene todos los productos y filtra por categoria con stream y lambda
        return listar().stream()
                .filter(p -> p.getIdCategoria() == idCategoria) // filtra por id de categoria
                .collect(Collectors.toList()); // colecta el resultado en una lista
    }

    @Override
    // busca un producto por su id retornando un optional
    public Optional<Producto> buscarPorId(int id) {
        // obtiene todos los productos y busca el que tenga el id dado con stream
        return listar().stream()
                .filter(p -> p.getIdProducto() == id) // filtra por id del producto
                .findFirst(); // retorna el primero que encuentre
    }

    @Override
    // agrega un nuevo producto en la bd con campos de auditoria
    public boolean agregar(Producto p) {
        // consulta para insertar un nuevo producto con todos sus campos
        String sql = "INSERT INTO Producto (nom_producto, desc_producto, unidad_medida, precio_compra, precio_venta, stock, fecha_vencimiento, estado, usuario_creacion, fecha_creacion, id_categoria, id_marca, id_proveedor) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'Activo', ?, CURDATE(), ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getNomProducto()); // asigna el nombre del producto
            ps.setString(2, p.getDescProducto()); // asigna la descripcion del producto
            ps.setString(3, p.getUnidadMedida()); // asigna la unidad de medida
            ps.setDouble(4, p.getPrecioCompra()); // asigna el precio de compra
            ps.setDouble(5, p.getPrecioVenta()); // asigna el precio de venta
            ps.setInt(6, p.getStock()); // asigna el stock inicial
            // asigna la fecha de vencimiento si existe, sino null
            if (p.getFechaVencimiento() != null) {
                ps.setDate(7, Date.valueOf(p.getFechaVencimiento())); // convierte localdate a sql date
            } else {
                ps.setNull(7, Types.DATE); // asigna null si no hay fecha de vencimiento
            }
            ps.setString(8, p.getUsuarioCreacion()); // asigna el usuario que crea el producto
            ps.setInt(9, p.getIdCategoria()); // asigna el id de la categoria
            ps.setInt(10, p.getIdMarca()); // asigna el id de la marca
            ps.setInt(11, p.getIdProveedor()); // asigna el id del proveedor
            return ps.executeUpdate() > 0; // retorna true si se inserto correctamente
        } catch (SQLException e) {
            System.err.println("error en agregar producto: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // actualiza un producto existente en la bd con campos de auditoria
    public boolean editar(Producto p) {
        // consulta para actualizar los campos del producto incluyendo auditoria
        String sql = "UPDATE Producto SET nom_producto=?, desc_producto=?, unidad_medida=?, precio_compra=?, precio_venta=?, stock=?, fecha_vencimiento=?, usuario_modificacion=?, fecha_modificacion=CURDATE(), ultimo_acceso=NOW(), id_categoria=?, id_marca=?, id_proveedor=? "
                + "WHERE id_producto=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, p.getNomProducto()); // asigna el nombre del producto
            ps.setString(2, p.getDescProducto()); // asigna la descripcion del producto
            ps.setString(3, p.getUnidadMedida()); // asigna la unidad de medida
            ps.setDouble(4, p.getPrecioCompra()); // asigna el precio de compra
            ps.setDouble(5, p.getPrecioVenta()); // asigna el precio de venta
            ps.setInt(6, p.getStock()); // asigna el stock
            // asigna la fecha de vencimiento si existe, sino null
            if (p.getFechaVencimiento() != null) {
                ps.setDate(7, Date.valueOf(p.getFechaVencimiento())); // convierte localdate a sql date
            } else {
                ps.setNull(7, Types.DATE); // asigna null si no hay fecha de vencimiento
            }
            ps.setString(8, p.getUsuarioModificacion()); // asigna el usuario que modifica
            ps.setInt(9, p.getIdCategoria()); // asigna el id de la categoria
            ps.setInt(10, p.getIdMarca()); // asigna el id de la marca
            ps.setInt(11, p.getIdProveedor()); // asigna el id del proveedor
            ps.setInt(12, p.getIdProducto()); // asigna el id del producto a actualizar
            return ps.executeUpdate() > 0; // retorna true si se actualizo correctamente
        } catch (SQLException e) {
            System.err.println("error en editar producto: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // elimina logicamente un producto cambiando su estado a inactivo
    public boolean eliminar(int id) {
        // consulta para cambiar el estado del producto a inactivo
        String sql = "UPDATE Producto SET estado = 'Inactivo', ultimo_acceso = NOW() WHERE id_producto = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id); // asigna el id del producto a eliminar
            return ps.executeUpdate() > 0; // retorna true si se actualizo correctamente
        } catch (SQLException e) {
            System.err.println("error en eliminar producto: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // lista las categorias disponibles para el filtro y formulario
    public List<String[]> listarCategorias() {
        List<String[]> lista = new ArrayList<>(); // lista donde se almacenaran las categorias
        String sql = "SELECT id_categoria, nom_categoria FROM Categoria WHERE estado = 'Activo' ORDER BY nom_categoria";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            // recorre los resultados y los agrega a la lista como arreglos de strings
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id_categoria")), // id de la categoria
                    rs.getString("nom_categoria") // nombre de la categoria
                });
            }
        } catch (SQLException e) {
            System.err.println("error en listar categorias: " + e.getMessage());
        }
        return lista; // retorna la lista de categorias
    }

    @Override
    // lista las marcas disponibles para el formulario
    public List<String[]> listarMarcas() {
        List<String[]> lista = new ArrayList<>(); // lista donde se almacenaran las marcas
        String sql = "SELECT id_marca, nom_marca FROM Marca WHERE estado = 'Activo' ORDER BY nom_marca";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            // recorre los resultados y los agrega a la lista como arreglos de strings
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id_marca")), // id de la marca
                    rs.getString("nom_marca") // nombre de la marca
                });
            }
        } catch (SQLException e) {
            System.err.println("error en listar marcas: " + e.getMessage());
        }
        return lista; // retorna la lista de marcas
    }

    @Override
    // lista los proveedores disponibles para el formulario
    public List<String[]> listarProveedores() {
        List<String[]> lista = new ArrayList<>(); // lista donde se almacenaran los proveedores
        String sql = "SELECT id_proveedor, razon_social FROM Proveedor WHERE estado = 'Activo' ORDER BY razon_social";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            // recorre los resultados y los agrega a la lista como arreglos de strings
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt("id_proveedor")), // id del proveedor
                    rs.getString("razon_social") // razon social del proveedor
                });
            }
        } catch (SQLException e) {
            System.err.println("error en listar proveedores: " + e.getMessage());
        }
        return lista; // retorna la lista de proveedores
    }
}
