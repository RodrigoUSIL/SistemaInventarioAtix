package repository;

import modelo.Producto;

// interfaz observer que define el contrato para los observadores de stock
public interface StockObserver {

    // metodo que se ejecuta cuando el stock de un producto cambia
    void notificar(Producto producto);
}
