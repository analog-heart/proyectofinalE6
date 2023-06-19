package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.EnumCalificacion;
import eggporIzquierda.solucionesactivas.enumation.EnumEstadoContrato;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class ContratoProveedor {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @ManyToOne
    private Proveedor proveedor;
    @ManyToOne
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    private Date fechaContrato;
    @Temporal(TemporalType.DATE)
    private Date fechaFinalizacion;
    @Enumerated(EnumType.STRING)
    private EnumEstadoContrato estado;
    @Enumerated(EnumType.STRING)
    private EnumCalificacion calificacion;
    private String comentario;
    private BigDecimal precio;




// CONSTRUCTORES:
    
        public ContratoProveedor() {
    }

    public ContratoProveedor(String id, Proveedor proveedor, Usuario usuario, Date fechaContrato, Date fechaFinalizacion, EnumEstadoContrato estado, EnumCalificacion calificacion, String comentario, BigDecimal precio) {
        this.id = id;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.fechaContrato = fechaContrato;
        this.fechaFinalizacion = fechaFinalizacion;
        this.estado = estado;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.precio = precio;
    }

    
    //GETTERS AND SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaContrato() {
        return fechaContrato;
    }

    public void setFechaContrato(Date fechaContrato) {
        this.fechaContrato = fechaContrato;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public EnumEstadoContrato getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoContrato estado) {
        this.estado = estado;
    }

    public EnumCalificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(EnumCalificacion calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
     
}