package repository;

import modelo.Usuario;
import singleton.ConexionDB;
import java.sql.*;
import java.time.LocalDate;

// implementacion de la interfaz con consultas reales a sql server
public class UsuarioRepository implements IUsuarioRepository {

    // metodo auxiliar para obtener la conexion a la bd
    private Connection getConn() {
        return ConexionDB.getInstancia().getConnection(); // obtiene la conexion del singleton
    }

    @Override
    // metodo para validar credenciales de login, retorna el usuario si las credenciales son correctas y el usuario esta activo
    public Usuario login(String nomUsuario, String contrasena) {
        // consulta que busca un usuario activo con las credenciales dadas
        String sql = "SELECT * FROM Usuario WHERE nom_usuario = ? AND contrasena = ? AND estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            // asigna los parametros de busqueda
            ps.setString(1, nomUsuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            // si encuentra un resultado construye el objeto usuario
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNomUsuario(rs.getString("nom_usuario"));
                u.setCorreoUsuario(rs.getString("correo_usuario"));
                u.setEstado(rs.getString("estado"));
                u.setIdRol(rs.getInt("id_rol"));
                return u;
            }
        } catch (SQLException e) { // en caso de error muestra el mensaje en consola
            System.err.println("error en login: " + e.getMessage());
        }
        // retorna null si no encontro coincidencia
        return null;
    }

    @Override
    // metodo para registrar un nuevo usuario en la bd, retorna true si se inserta correctamente
    public boolean registrar(Usuario usuario) {
        // consulta para insertar un nuevo usuario, el estado se setea como 'Activo' por defecto y la fecha de creacion es la fecha actual
        String sql = "INSERT INTO Usuario (nom_usuario, correo_usuario, contrasena, estado, usuario_creacion, fecha_creacion, id_rol) "
                + "VALUES (?, ?, ?, 'Activo', ?, CURDATE(), 2)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            // asigna los parametros de insercion
            ps.setString(1, usuario.getNomUsuario());
            ps.setString(2, usuario.getCorreoUsuario());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getNomUsuario());
            // executeUpdate retorna 1 si inserto correctamente
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { // en caso de error muestra el mensaje en consola
            System.err.println("error en registrar: " + e.getMessage());
            return false; // retorna false si hubo un error al insertar
        }
    }

    @Override
    // metodo para cambiar la contrasena de un usuario existente, retorna true si se actualiza correctamente
    public boolean cambiarContrasena(String nomUsuario, String contrasenaNueva) {
        // consulta para actualizar la contrasena de un usuario activo con el nombre dado
        String sql = "UPDATE Usuario SET contrasena = ? WHERE nom_usuario = ? AND estado = 'Activo'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            // nueva contrasena y nombre del usuario a actualizar
            ps.setString(1, contrasenaNueva);
            ps.setString(2, nomUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { // en caso de error muestra el mensaje en consola
            System.err.println("error en cambiarContrasena: " + e.getMessage());
            return false; // retorna false si hubo un error al actualizar
        }
    }

    @Override
    // metodo para verificar si un nombre de usuario ya existe en la bd, retorna true si el usuario existe
    public boolean existeUsuario(String nomUsuario) {
        // consulta que cuenta cuantas filas tienen el nombre de usuario dado, si el count es mayor a 0 el usuario ya existe
        String sql = "SELECT COUNT(*) FROM Usuario WHERE nom_usuario = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, nomUsuario); // asigna el nombre de usuario a buscar
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { // si encuentra el resultado, obtiene el count y verifica si es mayor a 0
                // si count es mayor a 0 el usuario ya existe
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { // en caso de error muestra el mensaje en consola
            System.err.println("error en existeUsuario: " + e.getMessage());
        }
        return false; // retorna false si no encontro el usuario o hubo un error
    }
}
