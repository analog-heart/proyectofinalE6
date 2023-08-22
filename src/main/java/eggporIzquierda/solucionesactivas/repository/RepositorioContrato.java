package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.Contrato;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioContrato extends JpaRepository<Contrato, String> {

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'ENCURSO' AND c.proveedor.id =:idProveedor")
    public List<Contrato> listarPorEstadoEncurso(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM Contrato c WHERE c.calificado = true AND c.proveedor.id =:idProveedor")
    public List<Contrato> listarPorEstadoCalificado(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'SOLICITADO' AND c.proveedor.id =:idProveedor")
    public List<Contrato> listarPorEstadoSolicitado(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'TERMINADO' AND c.proveedor.id =:idProveedor")
    public List<Contrato> listarPorEstadoTerminado(@Param("idProveedor") String idProveedor);

    @Query("SELECT c FROM Contrato c WHERE c.comentarioDenunciado = true")
    public List<Contrato> listarPorComentarioDenunciado();

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'ENCURSO' OR c.estado = 'SOLICITADO' AND c.usuario.id =:idUsuario")
    public List<Contrato> listarPorEstadoEncursoCliente(@Param("idUsuario") String idUsuario);

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'PRESUPUESTADO' AND c.usuario.id =:idUsuario")
    public List<Contrato> listarPorEstadoPresupuestadoCliente(@Param("idUsuario") String idUsuario);

    // @Query("SELECT c FROM Contrato c WHERE c.estado = 'CALIFICADO' AND c.usuario.id =:idUsuario")
    // public List<Contrato> listarPorEstadoCalificadoCliente(@Param("idUsuario") String idUsuario);

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'SOLICITADO' AND c.usuario.id =:idUsuario")
    public List<Contrato> listarPorEstadoSolicitadoCliente(@Param("idUsuario") String idUsuario);

    @Query("SELECT c FROM Contrato c WHERE c.estado = 'TERMINADO' AND c.usuario.id =:idUsuario")
    public List<Contrato> listarPorEstadoTerminadoCliente(@Param("idUsuario") String idUsuario);

}
