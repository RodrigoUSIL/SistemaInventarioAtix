package repository;

import modelo.Usuario;
import singleton.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// implementacion de la interfaz con consultas reales a mysql
public class UsuarioRepository implements IUsuarioRepository {

    // metodo auxiliar para obtener la conexion a la bd
    private Connection getConn() {
        return ConexionDB.getInstancia().getConnection(); // obtiene la conexion del singleton
    }

    // metodo auxiliar para mapear un resultset a un objeto usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario(); // crea un nuevo objeto usuario
        u.setIdUsuario(rs.getInt("id_usuario")); // asigna el id del usuario
        u.setNomUsuario(rs.getString("nom_usuario")); // asigna el nombre del usuario
        u.setCorreoUsuario(rs.getString("correo_usuario")); // asigna el correo del usuario
        u.setEstado(rs.getString("estado")); // asigna el estado del usuario
        u.setIdRol(rs.getInt("id_rol")); // asigna el rol del usuario
        return u; // retorna el usuario mapeado
    }

    @Override
    // lista todos los usuarios activos usando streams
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>(); // lista donde se almacenaran los usuarios
        String sql = "SELECT * FROM Usuario WHERE estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            while (rs.next()) {
                lista.add(mapearUsuario(rs)); // agrega el usuario mapeado a la lista
            }
        } catch (SQLException e) {
            System.err.println("error en listar usuarios: " + e.getMessage());
        }
        // retorna la lista ordenada por nombre usando stream y lambda
        return lista.stream()
                .sorted((a, b) -> a.getNomUsuario().compareToIgnoreCase(b.getNomUsuario()))
                .collect(Collectors.toList()); // colecta el resultado en una lista
    }

    @Override
    // busca un usuario por su id retornando un optional
    public Optional<Usuario> buscarPorId(int id) {
        // obtiene todos los usuarios y busca el que tenga el id dado con stream
        return listar().stream()
                .filter(u -> u.getIdUsuario() == id) // filtra por id del usuario
                .findFirst(); // retorna el primero que encuentre
    }

    @Override
    // valida credenciales y retorna el usuario envuelto en optional
    public Optional<Usuario> login(String nomUsuario, String contrasena) {
        // consulta que busca un usuario activo con las credenciales dadas
        String sql = "SELECT * FROM Usuario WHERE nom_usuario = ? AND contrasena = ? AND estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nomUsuario); // asigna el nombre de usuario
            ps.setString(2, contrasena); // asigna la contrasena
            ResultSet rs = ps.executeQuery(); // ejecuta la consulta
            if (rs.next()) {
                return Optional.of(mapearUsuario(rs)); // retorna el usuario encontrado en optional
            }
        } catch (SQLException e) {
            System.err.println("error en login: " + e.getMessage());
        }
        return Optional.empty(); // retorna optional vacio si no encontro el usuario
    }

    @Override
    // registra un nuevo usuario en la bd
    public boolean agregar(Usuario usuario) {
        // consulta para insertar un nuevo usuario
        String sql = "INSERT INTO Usuario (nom_usuario, correo_usuario, contrasena, estado, usuario_creacion, fecha_creacion, id_rol) "
                + "VALUES (?, ?, ?, 'Activo', ?, CURDATE(), 2)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, usuario.getNomUsuario()); // asigna el nombre del usuario
            ps.setString(2, usuario.getCorreoUsuario()); // asigna el correo del usuario
            ps.setString(3, usuario.getContrasena()); // asigna la contrasena del usuario
            ps.setString(4, usuario.getNomUsuario()); // asigna el usuario de creacion
            return ps.executeUpdate() > 0; // retorna true si se inserto correctamente
        } catch (SQLException e) {
            System.err.println("error en agregar usuario: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // actualiza datos de un usuario, no implementado en este modulo
    public boolean editar(Usuario usuario) {
        // no implementado, se maneja por cambiarContrasena
        return false;
    }

    @Override
    // elimina logicamente un usuario cambiando su estado a inactivo
    public boolean eliminar(int id) {
        // consulta para cambiar el estado del usuario a inactivo
        String sql = "UPDATE Usuario SET estado = 'Inactivo' WHERE id_usuario = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id); // asigna el id del usuario a eliminar
            return ps.executeUpdate() > 0; // retorna true si se actualizo correctamente
        } catch (SQLException e) {
            System.err.println("error en eliminar usuario: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // actualiza la contrasena de un usuario existente
    public boolean cambiarContrasena(String nomUsuario, String contrasenaNueva) {
        // consulta para actualizar la contrasena de un usuario activo
        String sql = "UPDATE Usuario SET contrasena = ? WHERE nom_usuario = ? AND estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, contrasenaNueva); // asigna la nueva contrasena
            ps.setString(2, nomUsuario); // asigna el nombre del usuario
            return ps.executeUpdate() > 0; // retorna true si se actualizo correctamente
        } catch (SQLException e) {
            System.err.println("error en cambiarContrasena: " + e.getMessage());
            return false; // retorna false si hubo un error
        }
    }

    @Override
    // verifica si un nombre de usuario ya existe en la bd usando streams
    public boolean existeUsuario(String nomUsuario) {
        // obtiene todos los usuarios y verifica si alguno tiene el nombre dado con stream
        return listar().stream()
                .anyMatch(u -> u.getNomUsuario().equalsIgnoreCase(nomUsuario)); // retorna true si encuentra el usuario
    }
}
