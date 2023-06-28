package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre =:nombre")
    public List<Usuario> buscarPorNombre(@Param("nombre") String nombre);
    
//    @Query("SELECT u FROM Usuario u WHERE u.estado = true")
//    public List<Usuario> listarUsuariosActivos();
    //debe listar cuando el esado es activo y el dtype es Usuario
     
    @Query("SELECT u FROM Usuario u ")
    public List<Usuario> listarUsuariosActivos();
    
    @Query("SELECT u FROM Usuario u WHERE u.estado = false")
    public List<Usuario> listarUsuariosInactivos();
    

}
