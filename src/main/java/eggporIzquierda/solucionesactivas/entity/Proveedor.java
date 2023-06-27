package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.EnumNivel;
import eggporIzquierda.solucionesactivas.enumation.EnumServiciosOfrecidos;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;



@Entity
public class Proveedor extends Usuario {
    
    @Enumerated(EnumType.STRING)
    private EnumServiciosOfrecidos servicios;
    private Boolean estadoProveedorActivo;
    private Double reputacion;
    @Enumerated(EnumType.STRING)
    private EnumNivel nivel;

    
    
    
    // CONSTRUCTORES (sin atributo reputación)
    public Proveedor() {
    }

    public Proveedor(EnumServiciosOfrecidos servicios, Boolean estadoProveedorActivo, EnumNivel nivel) {
        this.servicios = servicios;
        this.estadoProveedorActivo = estadoProveedorActivo;
        this.nivel = nivel;
    }

    public Proveedor(EnumServiciosOfrecidos servicios, String id, String nombreUsuario, String email, String password, String nombre, String apellido, Date fechaNacimiento, String dni, String telefono, Domicilio domicilio, boolean estado, Date fecha, Rol rol, Imagen fotoPerfil) {
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

    public EnumServiciosOfrecidos getServicios() {
        return servicios;
    }

    public void setServicios(EnumServiciosOfrecidos servicios) {
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
