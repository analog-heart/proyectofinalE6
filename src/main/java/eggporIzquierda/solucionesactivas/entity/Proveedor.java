package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.EnumNivel;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

@Entity

public class Proveedor extends Usuario {

    @ManyToMany
    private List<ServicioOfrecido> servicios;

    private Boolean estadoProveedorActivo;
    private Double reputacion;
    @Enumerated(EnumType.STRING)
    private EnumNivel nivel;

    // CONSTRUCTORES (sin atributo reputación)
    public Proveedor() {
    }

    public Proveedor(List<ServicioOfrecido> servicios, String id, String nombreUsuario, String email, String password, String nombre, String apellido, Date fechaNacimiento, String dni, String telefono, Domicilio domicilio, boolean estado, Date fecha, Rol rol, Imagen fotoPerfil) {
        super(id, nombreUsuario, email, password, nombre, apellido, fechaNacimiento, dni, telefono, domicilio, estado, fecha, rol, fotoPerfil);
        this.servicios = servicios;
    }

//    MÉTODOS ESPECIALES PARA ACTIVAR O DESACTIVAR AL PROVEEDOR
    public void activarProveedor() {
        this.estadoProveedorActivo = true;
    }

    public void desActivarProveedor() {
        this.estadoProveedorActivo = false;
    }

    // GUETTERS AND SETTERS
    public List<ServicioOfrecido> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioOfrecido> servicios) {
        this.servicios = servicios;
    }

    public Boolean getEstadoProveedorActivo() {
        return estadoProveedorActivo;
    }

    public void setEstadoProveedorActivo(Boolean estadoProveedorActivo) {
        this.estadoProveedorActivo = estadoProveedorActivo;
    }

    public Double getReputacion() {
        return reputacion;
    }

    public void setReputacion(Double reputacion) {
        this.reputacion = reputacion;
    }

    public EnumNivel getNivel() {
        return nivel;
    }

    public void setNivel(EnumNivel nivel) {
        this.nivel = nivel;
    }

}
