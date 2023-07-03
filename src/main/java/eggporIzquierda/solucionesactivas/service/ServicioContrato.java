package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.enumation.EnumEstadoContrato;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.repository.RepositorioUsuario;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioContrato {

    @Autowired
    private RepositorioContrato repositoriocontrato;
    @Autowired
    private RepositorioProveedor repositorioproveedor;
    @Autowired
    private RepositorioUsuario repositoriousuario;


@Transactional
public void crearContrato(String idUsuario, String idProveedor, boolean aceptar, boolean rechazar) throws MiException {
    Optional<Usuario> respuestaUsuario = repositoriousuario.findById(idUsuario);
    Optional<Proveedor> respuestaProveedor = repositorioproveedor.findById(idProveedor);

    if (respuestaUsuario.isPresent() && respuestaProveedor.isPresent()) {
        Usuario usuario = respuestaUsuario.get();
        Proveedor proveedor = respuestaProveedor.get();

        ContratoProveedor contrato = new ContratoProveedor();
        contrato.setUsuario(usuario);
        contrato.setProveedor(proveedor);
        contrato.setFechaContrato(new Date());

        if (aceptar && rechazar) {
            throw new MiException("No se puede aceptar y rechazar el contrato al mismo tiempo");
        } else if (aceptar) {
            contrato.setEstado(EnumEstadoContrato.ENCURSO);
        } else if (rechazar) {
            contrato.setEstado(EnumEstadoContrato.RECHAZADO);
        } else {
            contrato.setEstado(EnumEstadoContrato.SOLICITADO);
        }

        repositoriocontrato.save(contrato);
    } else {
        throw new MiException("No se encontró el usuario o proveedor correspondiente");
    }
}
}

