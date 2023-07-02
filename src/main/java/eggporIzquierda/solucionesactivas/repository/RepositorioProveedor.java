package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioProveedor extends JpaRepository<Proveedor, String> {

    @Query("SELECT u FROM Proveedor u WHERE u.email = :email")
    public Proveedor buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Proveedor u WHERE u.nombre =:nombre")
    public List<Proveedor> buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT u FROM Proveedor u WHERE u.rol =:rol")
    public List<Proveedor> buscarPorRol(@Param("rol") String rol);
    
   @Query("SELECT p FROM Proveedor p WHERE p.estado ='true'")
    public List<Proveedor> listarProveedoresActivos();
    
    //OR p.servicios LIKE %:filtro%
     @Query("SELECT p FROM Proveedor p WHERE p.nombre LIKE %:filtro% OR p.apellido LIKE %:filtro% ORDER BY p.reputacion DESC")
    public List<Proveedor> listarXpalabraClave(@Param("filtro") String filtro);


}
