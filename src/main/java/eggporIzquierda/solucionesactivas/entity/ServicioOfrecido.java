package eggporIzquierda.solucionesactivas.entity;




import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "servicios_ofrecidos")
public class ServicioOfrecido {
    
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String serv_id;

    private String serv_descripcion;
    
    @OneToOne (cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    private Imagen serv_imagen;
    
    


    
    public ServicioOfrecido() {
    }

    public ServicioOfrecido(String serv_descripcion) {
        this.serv_descripcion = serv_descripcion;
    }

    
    public ServicioOfrecido(String serv_descripcion, Imagen serv_imagen) {
        this.serv_descripcion = serv_descripcion;
        this.serv_imagen = serv_imagen;
    }

    
    
    
    
    
    
    public String getServ_id() {
        return serv_id;
    }

    public void setServ_id(String serv_id) {
        this.serv_id = serv_id;
    }

    public String getServ_descripcion() {
        return serv_descripcion;
    }

    public void setServ_descripcion(String serv_descripcion) {
        this.serv_descripcion = serv_descripcion;
    }

    public Imagen getServ_imagen() {
        return serv_imagen;
    }

    public void setServ_imagen(Imagen serv_imagen) {
        this.serv_imagen = serv_imagen;
    }

    

    
  
    

    
    
        

    
    
}
