package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.EnumServiciosOfrecidos;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import jakarta.persistence.Entity;
import java.util.Date;
import java.util.List;


@Entity
public class Proveedor extends Usuario {

    private List<EnumServiciosOfrecidos> servicios;
    private boolean estadoProveedorActivo = false;

    // CONSTRUCTORES
    public Proveedor() {
    }

    public Proveedor(List<EnumServiciosOfrecidos> servicios) {
        this.servicios = servicios;
    }

    public Proveedor(List<EnumServiciosOfrecidos> servicios, String id, String nombreUsuario, String email, String password, String nombre, String apellido, Date fechaNacimiento, String dni, String telefono, Domicilio domicilio, boolean estado, Date fecha, Rol rol, Imagen fotoPerfil) {
        super(id, nombreUsuario, email, password, nombre, apellido, fechaNacimiento, dni, telefono, domicilio, estado, fecha, rol, fotoPerfil);
        this.servicios = servicios;
    }

   

    //MÉTODOS ESPECIALES PARA ACTIVAR O DESACTIVAR AL PROVEEDOR
    public void activarProveedor() {
        this.estadoProveedorActivo = true;
    }

    public void desActivarProveedor() {
        this.estadoProveedorActivo = false;
    }

    // GUETTERS AND SETTERS

    public List<EnumServiciosOfrecidos> getServicios() {
        return servicios;
    }

    public void setServicios(List<EnumServiciosOfrecidos> servicios) {
        this.servicios = servicios;
    }

    public boolean isEstadoProveedorActivo() {
        return estadoProveedorActivo;
    }

    public void setEstadoProveedorActivo(boolean estadoProveedorActivo) {
        this.estadoProveedorActivo = estadoProveedorActivo;
    }
    

}
