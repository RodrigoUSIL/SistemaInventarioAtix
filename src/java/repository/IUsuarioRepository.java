package repository;

import modelo.Usuario;

// interfaz que define las operaciones de usuario en la bd
public interface IUsuarioRepository {

    // valida credenciales y retorna el usuario si existe
    Usuario login(String nomUsuario, String contrasena);

    // registra un nuevo usuario en la bd
    boolean registrar(Usuario usuario);

    // actualiza la contrasena de un usuario existente
    boolean cambiarContrasena(String nomUsuario, String contrasenaNueva);

    // verifica si un nombre de usuario ya existe en la bd
    boolean existeUsuario(String nomUsuario);
}
