package repository;

import modelo.Producto;
import java.util.List;

// interfaz especifica para productos, extiende la interfaz generica base
public interface IProductoRepository extends IRepository<Producto> {

    // lista productos filtrados por id de categoria
    List<Producto> listarPorCategoria(int idCategoria);

    // lista las categorias disponibles para el filtro en la vista
    List<String[]> listarCategorias();

    // lista las marcas disponibles para el formulario
    List<String[]> listarMarcas();

    // lista los proveedores disponibles para el formulario
    List<String[]> listarProveedores();
}
