package repository;

import modelo.Producto;

// observador que reacciona cuando el stock de un producto es critico
public class AlertaStockBajo implements StockObserver {

    // limite de stock para considerar un producto en nivel critico
    private static final int LIMITE_STOCK = 10;

    @Override
    // metodo que se ejecuta al recibir la notificacion de cambio de stock
    public void notificar(Producto producto) {
        // verifica si el stock del producto es menor al limite critico
        if (producto.getStock() < LIMITE_STOCK) {
            // muestra la alerta en consola con el nombre del producto y su stock actual
            System.out.println("alerta: stock critico detectado en producto '"
                    + producto.getNomProducto()
                    + "' con stock actual de "
                    + producto.getStock()
                    + " unidades.");
        }
    }
}
