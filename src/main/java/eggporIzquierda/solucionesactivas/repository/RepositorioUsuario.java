
package eggporIzquierda.solucionesactivas.repository;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RepositorioUsuario extends JpaRepository <Usuario , String> {
    
   
     @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param ("email") String email);
}

