package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioContrato extends JpaRepository<ContratoProveedor, String> {

    // @Query("SELECT c FROM ContratoProveedor c WHERE c.estado.toString() = :estado
    // AND c.proveedor.id =:idProveedor")
    // public List<ContratoProveedor> listarPorEstado(@Param("estado") String
    // estado, @Param("idProveedor") String idProveedor);
    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'ENCURSO' AND c.proveedor.id =:idProveedor")
    public List<ContratoProveedor> listarPorEstadoEncurso(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'CALIFICADO' AND c.proveedor.id =:idProveedor")
    public List<ContratoProveedor> listarPorEstadoCalificado(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'SOLICITADO' AND c.proveedor.id =:idProveedor")
    public List<ContratoProveedor> listarPorEstadoSolicitado(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'TERMINADO' AND c.proveedor.id =:idProveedor")
    public List<ContratoProveedor> listarPorEstadoTerminado(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.comentarioOfensivo = true")
    public List<ContratoProveedor> listarPorEstadoDenunciado();

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'ENCURSO' OR c.estado = 'SOLICITADO' AND c.usuario.id =:idUsuario")
    public List<ContratoProveedor> listarPorEstadoEncursoCliente(@Param("idUsuario") String idUsuario);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'CALIFICADO' AND c.usuario.id =:idUsuario")
    public List<ContratoProveedor> listarPorEstadoCalificadoCliente(@Param("idUsuario") String idUsuario);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'SOLICITADO' AND c.usuario.id =:idUsuario")
    public List<ContratoProveedor> listarPorEstadoSolicitadoCliente(@Param("idUsuario") String idUsuario);

    @Query("SELECT c FROM ContratoProveedor c WHERE c.estado = 'TERMINADO' AND c.usuario.id =:idUsuario")
    public List<ContratoProveedor> listarPorEstadoTerminadoCliente(@Param("idUsuario") String idUsuario);

}
