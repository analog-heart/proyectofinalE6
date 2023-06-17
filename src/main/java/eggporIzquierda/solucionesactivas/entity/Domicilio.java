
package eggporIzquierda.solucionesactivas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Domicilio {
     @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name= "uuid" , strategy = "uuid2")
    private String id;
    
    private String calle;
    private String numero;
    private String barrio;
    private String lote;
  
    private String codigoPostal;
    private String localidad;

    public Domicilio() {
    }

    public Domicilio(String calle, String numero, String barrio, String lote, String codigoPostal, String localidad) {
        this.calle = calle;
        this.numero = numero;
        this.barrio = barrio;
        this.lote = lote;
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

   

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
  
    
    
}
