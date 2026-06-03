package modelo;

import java.time.LocalDate;

// clase modelo que representa un usuario del sistema
public class Usuario {

    // atributos que coinciden con las columnas de la tabla usuario en bd
    private int idUsuario;
    private String nomUsuario;
    private String correoUsuario;
    private String contrasena;
    private String estado;
    private String usuarioCreacion;
    private LocalDate fechaCreacion;
    private int idRol;

    // constructor vacio para uso general
    public Usuario() {
    }

    // constructor rapido para login
    public Usuario(String nomUsuario, String contrasena) {
        this.nomUsuario = nomUsuario;
        this.contrasena = contrasena;
    }

    // getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}
