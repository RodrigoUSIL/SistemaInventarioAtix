package repository;

import java.util.List;
import java.util.Optional;

// interfaz generica base para todos los repositorios del sistema
public interface IRepository<T> {

    // lista todos los registros activos
    List<T> listar();

    // busca un registro por su id, retorna optional para manejar el caso de no encontrar
    Optional<T> buscarPorId(int id);

    // agrega un nuevo registro, retorna true si se inserto correctamente
    boolean agregar(T entidad);

    // actualiza un registro existente, retorna true si se actualizo correctamente
    boolean editar(T entidad);

    // elimina logicamente un registro cambiando su estado a inactivo, retorna true si se actualizo correctamente
    boolean eliminar(int id);
}
