package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioContrato extends JpaRepository<ContratoProveedor, String> {
//
//    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
//    public List<Usuario> listarPorRol(@Param("rol") String rol);

}
