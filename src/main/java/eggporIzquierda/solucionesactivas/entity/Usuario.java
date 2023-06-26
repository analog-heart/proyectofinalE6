package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;

    protected String nombreUsuario;
    protected String email;
    protected String password;

    protected String nombre;
    protected String apellido;
    @Temporal(TemporalType.DATE)
    protected Date fechaNacimiento;
    protected String dni;
    protected String telefono;

    @OneToOne
    protected Domicilio domicilio;

    protected boolean estado;

    @Temporal(TemporalType.DATE)
    protected Date fecha;

    @Enumerated(EnumType.STRING)
    protected Rol rol;

//    @OneToOne
//    protected Imagen fotoPerfil;

    //-----------Constructores 
    public Usuario() {
    }

    public Usuario(String id, String nombreUsuario, String email, String password, String nombre, String apellido, Date fechaNacimiento, String dni, String telefono, Domicilio domicilio, boolean estado, Date fecha, Rol rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.estado = estado;
        this.fecha = fecha;
        this.rol = rol;
//        this.fotoPerfil = fotoPerfil;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

//    public Imagen getFotoPerfil() {
//        return fotoPerfil;
//    }
//
//    public void setFotoPerfil(Imagen fotoPerfil) {
//        this.fotoPerfil = fotoPerfil;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario{");
        sb.append("id=").append(id);
        sb.append(", nombreUsuario=").append(nombreUsuario);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", nombre=").append(nombre);
        sb.append(", apellido=").append(apellido);
        sb.append(", fechaNacimiento=").append(fechaNacimiento);
        sb.append(", dni=").append(dni);
        sb.append(", telefono=").append(telefono);
        sb.append(", domicilio=").append(domicilio);
        sb.append(", estado=").append(estado);
        sb.append(", fecha=").append(fecha);
        sb.append(", rol=").append(rol);
//        sb.append(", fotoPerfil=").append(fotoPerfil);
        sb.append('}');
        return sb.toString();
    }

}
