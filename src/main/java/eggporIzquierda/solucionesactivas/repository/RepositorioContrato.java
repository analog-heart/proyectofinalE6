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

//    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = :estado")
//    public List<ContratoProveedor> listarPorEstado(@Param("estado") String estado);
    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'SOLICITADO' AND c.proveedor.id =:idProveedor")
    public List<ContratoProveedor> listarPorEstadoSolicitado(@Param("idProveedor") String idProveedor);

}
