package eggporIzquierda.solucionesactivas.entity;

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
public class Contrato {

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

    private Boolean comentarioDenunciado = false;

    private Boolean comentarioEliminado = false;

    private Boolean calificado = false;

    private Integer calificacion;

    private String comentarioFinal;

    private String comentarioInicial;

    private String comentarioOfensivoEliminado;

    private BigDecimal precio;

// CONSTRUCTORES:
    public Contrato() {
    }

    public Contrato(String id, Proveedor proveedor, Usuario usuario, Date fechaContrato, Date fechaFinalizacion, EnumEstadoContrato estado, Integer calificacion, String comentarioFinal, String comentarioInicial, String comentarioOfensivoEliminado, BigDecimal precio) {
        this.id = id;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.fechaContrato = fechaContrato;
        this.fechaFinalizacion = fechaFinalizacion;
        this.estado = estado;
        this.calificacion = calificacion;
        this.comentarioFinal = comentarioFinal;
        this.comentarioInicial = comentarioInicial;
        this.comentarioOfensivoEliminado = comentarioOfensivoEliminado;
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

    public Boolean getComentarioDenunciado() {
        return comentarioDenunciado;
    }

    public void setComentarioDenunciado(Boolean comentarioDenunciado) {
        this.comentarioDenunciado = comentarioDenunciado;
    }

    public Boolean getComentarioEliminado() {
        return comentarioEliminado;
    }

    public void setComentarioEliminado(Boolean comentarioEliminado) {
        this.comentarioEliminado = comentarioEliminado;
    }

    public String getComentarioOfensivoEliminado() {
        return comentarioOfensivoEliminado;
    }

    public void setComentarioOfensivoEliminado(String comentarioOfensivoEliminado) {
        this.comentarioOfensivoEliminado = comentarioOfensivoEliminado;
    }

    public Boolean getCalificado() {
        return calificado;
    }

    public void setCalificado(Boolean calificado) {
        this.calificado = calificado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contrato{");
        sb.append("id=").append(id);
        sb.append(", proveedor=").append(proveedor);
        sb.append(", usuario=").append(usuario);
        sb.append(", fechaContrato=").append(fechaContrato);
        sb.append(", fechaFinalizacion=").append(fechaFinalizacion);
        sb.append(", estado=").append(estado);
        sb.append(", comentarioDenunciado=").append(comentarioDenunciado);
        sb.append(", comentarioEliminado=").append(comentarioEliminado);
        sb.append(", calificado=").append(calificado);
        sb.append(", calificacion=").append(calificacion);
        sb.append(", comentarioFinal=").append(comentarioFinal);
        sb.append(", comentarioInicial=").append(comentarioInicial);
        sb.append(", comentarioOfensivoEliminado=").append(comentarioOfensivoEliminado);
        sb.append(", precio=").append(precio);
        sb.append('}');
        return sb.toString();
    }

}
