package repository;

import modelo.Movimiento;
import java.util.List;

// interfaz especifica para movimientos, extiende la interfaz generica base
public interface IMovimientoRepository extends IRepository<Movimiento> {

    // lista movimientos filtrados por id de producto
    List<Movimiento> listarPorProducto(int idProducto);

    // lista movimientos filtrados por tipo de movimiento
    List<Movimiento> listarPorTipo(int idTipoMovimiento);

    // lista los productos disponibles para el formulario de movimientos
    List<String[]> listarProductos();

    // lista los tipos de movimiento disponibles para el formulario
    List<String[]> listarTiposMovimiento();
}
