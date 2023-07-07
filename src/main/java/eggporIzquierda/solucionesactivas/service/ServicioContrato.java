package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.enumation.EnumCalificacion;
import eggporIzquierda.solucionesactivas.enumation.EnumEstadoContrato;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.repository.RepositorioUsuario;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public void crearContrato(String idUsuario, String idProveedor, String comentarioInicial) throws MiException {

        Optional<Usuario> respuestaUsuario = repositoriousuario.findById(idUsuario);
        Optional<Proveedor> respuestaProveedor = repositorioproveedor.findById(idProveedor);

        Proveedor p = new Proveedor();
        Usuario u = new Usuario();

        if (respuestaUsuario.isPresent()) {
            u = respuestaUsuario.get();
        }

        if (respuestaProveedor.isPresent()) {
            p = respuestaProveedor.get();
        }

        ContratoProveedor CP = new ContratoProveedor();

        CP.setEstado(EnumEstadoContrato.SOLICITADO);
        CP.setProveedor(p);
        CP.setUsuario(u);
        CP.setFechaContrato(new Date());
        CP.setComentarioInicial(comentarioInicial);

        repositoriocontrato.save(CP);
    }

    @Transactional
    public void actualizarContrato(String idContrato, String decision) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();
            System.out.println("CONTRATO: " + newCP);

            if (decision.equalsIgnoreCase("aceptar")) {

                newCP.setEstado(EnumEstadoContrato.ENCURSO);

                repositoriocontrato.save(newCP);
            }

            if (decision.equalsIgnoreCase("rechazar")) {

                newCP.setEstado(EnumEstadoContrato.RECHAZADO);

                repositoriocontrato.save(newCP);
            }

        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            throw new MiException("El contrato no existe");

        }

    }

    public List<ContratoProveedor> listarContratosSesion(Usuario usuario) {

        List<ContratoProveedor> contratos = listarContratos();
        List<ContratoProveedor> contratosSesion = new ArrayList();
        // List<Usuario> usuariosRol =
        // repositoriocontrato.listarPorRol(usuario.getRol().toString());

//        System.out.println("CONTRATOS: " + contratos);
        if (usuario.getRol().toString().equalsIgnoreCase("USUARIO")) {
            for (int i = 0; i < contratos.size(); i++) {

                if (contratos.get(i).getUsuario().getId().equalsIgnoreCase(usuario.getId())) {

                    contratosSesion.add(contratos.get(i));

                }
            }
        }
        if (usuario.getRol().toString().equalsIgnoreCase("PROVEEDOR")) {
            for (int i = 0; i < contratos.size(); i++) {

                if (contratos.get(i).getProveedor().getId().equalsIgnoreCase(usuario.getId())) {

                    contratosSesion.add(contratos.get(i));

                }
            }
        }

        return contratosSesion;

    }

    @Transactional
    public void finalizarContrato(String idContrato) throws MiException {

        System.out.println("ENTRANDO AL SERVICIO");
        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            System.out.println("RESPUESTA PRESENTE");
            newCP = respuestaCP.get();

            newCP.setEstado(EnumEstadoContrato.TERMINADO);

            repositoriocontrato.save(newCP);
        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            System.out.println("RESPUESTA EMPTY");
            throw new MiException("El contrato no existe");

        }

    }

    @Transactional
    public void calificarContrato(String idContrato, String comentarioFinal, String calificacion) throws MiException {

        System.out.println("ENTRANDO AL SERVICIO");
        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            System.out.println("RESPUESTA PRESENTE");
            newCP = respuestaCP.get();

            // newCP.setEstado(EnumEstadoContrato.TERMINADO);
            newCP.setComentarioFinal(comentarioFinal);
            newCP.setEstado(EnumEstadoContrato.CALIFICADO);

            switch (calificacion) {
                case "1":
                    newCP.setCalificacion(EnumCalificacion.MALO);
                    break;
                case "2":
                    newCP.setCalificacion(EnumCalificacion.REGULAR);
                    break;
                case "3":
                    newCP.setCalificacion(EnumCalificacion.BUENO);
                    break;
                case "4":
                    newCP.setCalificacion(EnumCalificacion.MUYBUENO);
                    break;
                case "5":
                    newCP.setCalificacion(EnumCalificacion.EXCELENTE);
                    break;

                default:
                    break;
            }

            repositoriocontrato.save(newCP);
        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            System.out.println("RESPUESTA EMPTY");
            throw new MiException("El contrato no existe");

        }

    }

    public List<ContratoProveedor> listarContratos() {

        List<ContratoProveedor> contratos = new ArrayList();

        contratos = repositoriocontrato.findAll();

        return contratos;

    }

}
