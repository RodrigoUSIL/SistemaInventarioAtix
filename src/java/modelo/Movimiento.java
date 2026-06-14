package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

// clase modelo que representa un movimiento de inventario
public class Movimiento {

    // atributos que coinciden con las columnas de la tabla Movimiento_Inventario en bd
    private int idMovimiento;
    private int cantidad;
    private int stockAnterior;
    private int stockNuevo;
    private LocalDateTime fechaMovimiento;
    private String observacion;
    private String estado;
    // campos de auditoria
    private String usuarioCreacion;
    private LocalDate fechaCreacion;
    // llaves foraneas
    private int idProducto;
    private int idUsuario;
    private int idTipoMovimiento;
    // campos extra para mostrar en la vista
    private String nomProducto;
    private String nomTipoMovimiento;
    private String nomUsuario;

    // constructor vacio para uso general
    public Movimiento() {
    }

    // constructor rapido para listado
    public Movimiento(int idMovimiento, int cantidad, int stockAnterior, int stockNuevo,
            LocalDateTime fechaMovimiento, String nomProducto, String nomTipoMovimiento, String nomUsuario) {
        this.idMovimiento = idMovimiento; // asigna el id del movimiento
        this.cantidad = cantidad; // asigna la cantidad del movimiento
        this.stockAnterior = stockAnterior; // asigna el stock anterior
        this.stockNuevo = stockNuevo; // asigna el stock nuevo
        this.fechaMovimiento = fechaMovimiento; // asigna la fecha del movimiento
        this.nomProducto = nomProducto; // asigna el nombre del producto
        this.nomTipoMovimiento = nomTipoMovimiento; // asigna el tipo de movimiento
        this.nomUsuario = nomUsuario; // asigna el nombre del usuario
    }

    // getter del id del movimiento
    public int getIdMovimiento() {
        return idMovimiento;
    }

    // setter del id del movimiento
    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    // getter de la cantidad del movimiento
    public int getCantidad() {
        return cantidad;
    }

    // setter de la cantidad del movimiento
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // getter del stock anterior al movimiento
    public int getStockAnterior() {
        return stockAnterior;
    }

    // setter del stock anterior al movimiento
    public void setStockAnterior(int stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    // getter del stock nuevo despues del movimiento
    public int getStockNuevo() {
        return stockNuevo;
    }

    // setter del stock nuevo despues del movimiento
    public void setStockNuevo(int stockNuevo) {
        this.stockNuevo = stockNuevo;
    }

    // getter de la fecha del movimiento
    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    // setter de la fecha del movimiento
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    // getter de la observacion del movimiento
    public String getObservacion() {
        return observacion;
    }

    // setter de la observacion del movimiento
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    // getter del estado del movimiento
    public String getEstado() {
        return estado;
    }

    // setter del estado del movimiento
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // getter del usuario que creo el movimiento
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    // setter del usuario que creo el movimiento
    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    // getter de la fecha de creacion del movimiento
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    // setter de la fecha de creacion del movimiento
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // getter del id del producto del movimiento
    public int getIdProducto() {
        return idProducto;
    }

    // setter del id del producto del movimiento
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    // getter del id del usuario del movimiento
    public int getIdUsuario() {
        return idUsuario;
    }

    // setter del id del usuario del movimiento
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // getter del id del tipo de movimiento
    public int getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    // setter del id del tipo de movimiento
    public void setIdTipoMovimiento(int idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    // getter del nombre del producto para mostrar en vista
    public String getNomProducto() {
        return nomProducto;
    }

    // setter del nombre del producto para mostrar en vista
    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    // getter del nombre del tipo de movimiento para mostrar en vista
    public String getNomTipoMovimiento() {
        return nomTipoMovimiento;
    }

    // setter del nombre del tipo de movimiento para mostrar en vista
    public void setNomTipoMovimiento(String nomTipoMovimiento) {
        this.nomTipoMovimiento = nomTipoMovimiento;
    }

    // getter del nombre del usuario para mostrar en vista
    public String getNomUsuario() {
        return nomUsuario;
    }

    // setter del nombre del usuario para mostrar en vista
    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }
}
