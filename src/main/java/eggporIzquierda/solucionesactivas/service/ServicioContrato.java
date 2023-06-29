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
    public void crearContrato(String idUsuario, String idProveedor) throws MiException {

        Optional<Usuario> respuestaUsuario = repositoriousuario.findById(idUsuario);
        Optional<Proveedor> respuestaProveedor = repositorioproveedor.findById(idProveedor);

        Proveedor p = new Proveedor();
        System.out.println("PROVEEDOR P: " + respuestaProveedor);
        Usuario u = new Usuario();

        System.out.println("DESDE EL SERVICIO:");
        System.out.println("ID USUARIO: " + idUsuario);
        System.out.println("ID PROVEEDOR: " + idProveedor);

        if (respuestaUsuario.isPresent()) {

            u = respuestaUsuario.get();
            System.out.println("USUARIO: " + u);
        }

        if (respuestaProveedor.isPresent()) {

            p = respuestaProveedor.get();
            System.out.println("PROVEEDOR: " + p);
        }

        ContratoProveedor CP = new ContratoProveedor();

        CP.setEstado(EnumEstadoContrato.SOLICITADO);
        CP.setProveedor(p);
        CP.setUsuario(u);
        CP.setFechaContrato(new Date());

        repositoriocontrato.save(CP);
    }

    @Transactional
    public void aceptarContrato(String idContrato) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();
            System.out.println("CONTRATO: " + newCP);

            newCP.setEstado(EnumEstadoContrato.ENCURSO);

            System.out.println("CONTRATO: " + newCP);

            repositoriocontrato.save(newCP);
        }

    }

    public void actualizarContrato(String idContrato, String decision) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();
            System.out.println("CONTRATO: " + newCP);

            if (decision.equalsIgnoreCase("aceptar")) {

                newCP.setEstado(EnumEstadoContrato.ENCURSO);

                System.out.println("CONTRATO: " + newCP);

                repositoriocontrato.save(newCP);
            }

            if (decision.equalsIgnoreCase("rechazar")) {

                newCP.setEstado(EnumEstadoContrato.RECHAZADO);

                System.out.println("CONTRATO: " + newCP);

                repositoriocontrato.save(newCP);
            }

        }

    }

}
