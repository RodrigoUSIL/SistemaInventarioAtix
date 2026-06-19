package repository;

import modelo.Producto;
import java.util.ArrayList;
import java.util.List;

// subject que gestiona los observadores y notifica cambios de stock
public class StockSubject {

    // lista de observadores registrados
    private List<StockObserver> observadores = new ArrayList<>();

    // agrega un observador a la lista
    public void agregarObservador(StockObserver observador) {
        observadores.add(observador); // registra el observador en la lista
    }

    // elimina un observador de la lista
    public void eliminarObservador(StockObserver observador) {
        observadores.remove(observador); // elimina el observador de la lista
    }

    // notifica a todos los observadores registrados sobre el cambio de stock
    public void notificarObservadores(Producto producto) {
        // recorre la lista de observadores usando lambda y stream
        observadores.stream()
                .forEach(obs -> obs.notificar(producto)); // notifica a cada observador
    }
}
