package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioProveedor extends JpaRepository<Proveedor, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Proveedor buscarPorEmail(@Param("email") String email);
    
//        @Query("SELECT u FROM Usuario u WHERE u.rol =:rol")
//    public List<Usuario> buscarPorNombre(@Param("rol") String rol);

}
