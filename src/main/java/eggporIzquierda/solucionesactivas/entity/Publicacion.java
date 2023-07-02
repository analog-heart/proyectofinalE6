//package eggporIzquierda.solucionesactivas.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.OneToOne;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import org.hibernate.annotations.GenericGenerator;
//
//@Entity
//public class Publicacion {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    private String id;
//    @OneToOne
//    private Proveedor proveedor;
//    private String titulo;
//    private String descripcion;
//    @OneToMany
//    private ArrayList <Imagen> imagenes;
//    private BigDecimal precio;
//
//    public Publicacion() {
//    }
//
//    public Publicacion(String id, Proveedor proveedor, String titulo, String descripcion, ArrayList<Imagen> imagenes, BigDecimal precio) {
//        this.id = id;
//        this.proveedor = proveedor;
//        this.titulo = titulo;
//        this.descripcion = descripcion;
//        this.imagenes = imagenes;
//        this.precio = precio;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Proveedor getProveedor() {
//        return proveedor;
//    }
//
//    public void setProveedor(Proveedor proveedor) {
//        this.proveedor = proveedor;
//    }
//
//    public String getTitulo() {
//        return titulo;
//    }
//
//    public void setTitulo(String titulo) {
//        this.titulo = titulo;
//    }
//
//    public String getDescripcion() {
//        return descripcion;
//    }
//
//    public void setDescripcion(String descripcion) {
//        this.descripcion = descripcion;
//    }
//
//    public ArrayList<Imagen> getImagenes() {
//        return imagenes;
//    }
//
//    public void setImagenes(ArrayList<Imagen> imagenes) {
//        this.imagenes = imagenes;
//    }
//
//    public BigDecimal getPrecio() {
//        return precio;
//    }
//
//    public void setPrecio(BigDecimal precio) {
//        this.precio = precio;
//    }
//
//    
//    
//}
