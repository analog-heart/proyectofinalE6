package eggporIzquierda.solucionesactivas.entity;

import eggporIzquierda.solucionesactivas.enumation.EnumEstadoContrato;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

    private Boolean comentarioOfensivo;

    private Integer calificacion;

    private String comentarioFinal;

    private String comentarioInicial;

    private BigDecimal precio;

// CONSTRUCTORES:
    public ContratoProveedor() {
    }

    public ContratoProveedor(String id, Proveedor proveedor, Usuario usuario, Date fechaContrato, Date fechaFinalizacion, EnumEstadoContrato estado, Integer calificacion, String comentarioFinal, String comentarioInicial, BigDecimal precio, Boolean comentarioOfensivo) {
        this.id = id;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.fechaContrato = fechaContrato;
        this.fechaFinalizacion = fechaFinalizacion;
        this.estado = estado;
        this.calificacion = calificacion;
        this.comentarioFinal = comentarioFinal;
        this.comentarioInicial = comentarioInicial;
        this.precio = precio;
        this.comentarioOfensivo = comentarioOfensivo;

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

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentarioFinal() {
        return comentarioFinal;
    }

    public void setComentarioFinal(String comentario) {
        this.comentarioFinal = comentario;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getComentarioInicial() {
        return comentarioInicial;
    }

    public void setComentarioInicial(String comentarioInicial) {
        this.comentarioInicial = comentarioInicial;
    }

    public Boolean getComentarioOfensivo() {
        return comentarioOfensivo;
    }

    public void setComentarioOfensivo(Boolean comentarioOfensivo) {
        this.comentarioOfensivo = comentarioOfensivo;
    }

}
