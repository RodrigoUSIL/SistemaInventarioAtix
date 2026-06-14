package repository;

import modelo.Usuario;
import java.util.Optional;

// interfaz especifica para usuarios, extiende la interfaz generica base
public interface IUsuarioRepository extends IRepository<Usuario> {

    // valida credenciales y retorna el usuario envuelto en optional si existe
    Optional<Usuario> login(String nomUsuario, String contrasena);

    // actualiza la contrasena de un usuario existente
    boolean cambiarContrasena(String nomUsuario, String contrasenaNueva);

    // verifica si un nombre de usuario ya existe en la bd
    boolean existeUsuario(String nomUsuario);
}
