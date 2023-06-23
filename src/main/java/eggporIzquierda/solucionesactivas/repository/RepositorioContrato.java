package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioContrato extends JpaRepository <ContratoProveedor, String>{
    
//    Dejamos comentada la query para futuras busquedas de contrato
//    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
//    public Usuario buscarPorEmail(@Param("email") String email);
//    
}
