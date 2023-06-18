package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.EnumServiciosOfrecidos;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Proveedor extends Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private Usuario idUsuario;
    private List<EnumServiciosOfrecidos> servicios;
    private boolean estadoProveedorActivo = false;

    // CONSTRUCTORES
    public Proveedor() {
    }

    public Proveedor(String id, String nombreUsuario, String email, String password, String nombre, String apellido, Date fechaNacimiento, String dni, String telefono, Domicilio domicilio, Boolean estado, Date fecha, Rol rol, Imagen fotoPerfil) {
        super(id, nombreUsuario, email, password, nombre, apellido, fechaNacimiento, dni, telefono, domicilio, estado, fecha, rol, fotoPerfil);
    }

    //MÉTODOS ESPECIALES PARA ACTIVAR O DESACTIVAR AL PROVEEDOR
    public void activarProveedor() {
        this.estadoProveedorActivo = true;
    }

    public void desActivarProveedor() {
        this.estadoProveedorActivo = false;
    }

    // GUETTERS AND SETTERS
    public boolean isEstadoProveedorActivo() {
        return estadoProveedorActivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<EnumServiciosOfrecidos> getServicios() {
        return servicios;
    }

    public void setServicios(List<EnumServiciosOfrecidos> servicios) {
        this.servicios = servicios;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Imagen getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Imagen fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

}
