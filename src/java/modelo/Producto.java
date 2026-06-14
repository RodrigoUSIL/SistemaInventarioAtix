package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

// clase modelo que representa un producto del inventario
public class Producto {

    // atributos que coinciden con las columnas de la tabla Producto en bd
    private int idProducto;
    private String nomProducto;
    private String descProducto;
    private String unidadMedida;
    private double precioCompra;
    private double precioVenta;
    private int stock;
    private LocalDate fechaVencimiento;
    private String estado;
    // campos de auditoria
    private String usuarioCreacion;
    private LocalDate fechaCreacion;
    private String usuarioModificacion;
    private LocalDate fechaModificacion;
    private LocalDateTime ultimoAcceso;
    // llaves foraneas
    private int idCategoria;
    private int idMarca;
    private int idProveedor;
    // campos extra para mostrar en la vista
    private String nomCategoria;
    private String nomMarca;
    private String nomProveedor;

    // constructor vacio para uso general
    public Producto() {
    }

    // constructor rapido para listado
    public Producto(int idProducto, String nomProducto, double precioCompra, double precioVenta, int stock, int idCategoria, String nomCategoria, String nomMarca) {
        this.idProducto = idProducto;
        this.nomProducto = nomProducto;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.nomCategoria = nomCategoria;
        this.nomMarca = nomMarca;
    }

    // getter del id del producto
    public int getIdProducto() {
        return idProducto;
    }

    // setter del id del producto
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    // getter del nombre del producto
    public String getNomProducto() {
        return nomProducto;
    }

    // setter del nombre del producto
    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    // getter de la descripcion del producto
    public String getDescProducto() {
        return descProducto;
    }

    // setter de la descripcion del producto
    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    // getter de la unidad de medida del producto
    public String getUnidadMedida() {
        return unidadMedida;
    }

    // setter de la unidad de medida del producto
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    // getter del precio de compra del producto
    public double getPrecioCompra() {
        return precioCompra;
    }

    // setter del precio de compra del producto
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    // getter del precio de venta del producto
    public double getPrecioVenta() {
        return precioVenta;
    }

    // setter del precio de venta del producto
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    // getter del stock del producto
    public int getStock() {
        return stock;
    }

    // setter del stock del producto
    public void setStock(int stock) {
        this.stock = stock;
    }

    // getter de la fecha de vencimiento del producto
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    // setter de la fecha de vencimiento del producto
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    // getter del estado del producto
    public String getEstado() {
        return estado;
    }

    // setter del estado del producto
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // getter del usuario que creo el producto
    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    // setter del usuario que creo el producto
    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    // getter de la fecha de creacion del producto
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    // setter de la fecha de creacion del producto
    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // getter del usuario que modifico el producto
    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    // setter del usuario que modifico el producto
    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    // getter de la fecha de modificacion del producto
    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    // setter de la fecha de modificacion del producto
    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    // getter del ultimo acceso al producto
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    // setter del ultimo acceso al producto
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    // getter del id de la categoria del producto
    public int getIdCategoria() {
        return idCategoria;
    }

    // setter del id de la categoria del producto
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    // getter del id de la marca del producto
    public int getIdMarca() {
        return idMarca;
    }

    // setter del id de la marca del producto
    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    // getter del id del proveedor del producto
    public int getIdProveedor() {
        return idProveedor;
    }

    // setter del id del proveedor del producto
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    // getter del nombre de la categoria para mostrar en vista
    public String getNomCategoria() {
        return nomCategoria;
    }

    // setter del nombre de la categoria para mostrar en vista
    public void setNomCategoria(String nomCategoria) {
        this.nomCategoria = nomCategoria;
    }

    // getter del nombre de la marca para mostrar en vista
    public String getNomMarca() {
        return nomMarca;
    }

    // setter del nombre de la marca para mostrar en vista
    public void setNomMarca(String nomMarca) {
        this.nomMarca = nomMarca;
    }

    // getter del nombre del proveedor para mostrar en vista
    public String getNomProveedor() {
        return nomProveedor;
    }

    // setter del nombre del proveedor para mostrar en vista
    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }
}
