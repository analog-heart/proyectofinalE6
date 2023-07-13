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
    @Autowired
    private ServicioProveedor servicioProveedor;

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
        CP.setComentarioOfensivo(false);

        repositoriocontrato.save(CP);
    }

    @Transactional
    public void actualizarContrato(String idContrato, String decision) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();

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

        // System.out.println("CONTRATOS: " + contratos);
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
    public void finalizarContratoProveedor(String idContrato, BigDecimal precio) throws MiException {

        System.out.println("log 3");

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        System.out.println(respuestaCP);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            System.out.println("LOG IS PRESENT");
            newCP = respuestaCP.get();
            
            newCP.setEstado(EnumEstadoContrato.TERMINADO);
            newCP.setPrecio(precio);

            System.out.println(newCP.getPrecio());
   
            newCP.setFechaFinalizacion(new Date());
 
            System.out.println(newCP);
            repositoriocontrato.save(newCP);

        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            System.out.println("LOG IS EMPTY");
            throw new MiException("El contrato no existe");
        }

    }

    @Transactional
    public void finalizarContratoUsuario(String idContrato, BigDecimal precio, String comentarioFinal)
            throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            System.out.println("RESPUESTA PRESENTE");
            newCP = respuestaCP.get();

            newCP.setEstado(EnumEstadoContrato.CANCELADO);
            newCP.setPrecio(precio);
            newCP.setFechaFinalizacion(new Date());
            newCP.setComentarioFinal("CANCELADO POR EL CLIENTE: " + comentarioFinal);

            repositoriocontrato.save(newCP);
        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            System.out.println("RESPUESTA EMPTY");
            throw new MiException("El contrato no existe");

        }

    }

    @Transactional
    public void calificarContrato(String idContrato, String comentarioFinal, Integer calificacion) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (comentarioFinal.equalsIgnoreCase("")) {
            comentarioFinal = "Sin comentarios del cliente";
        }
        
        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();
            newCP.setComentarioFinal(comentarioFinal);
            newCP.setEstado(EnumEstadoContrato.CALIFICADO);
            newCP.setCalificacion(calificacion);

            // grabando la reputacion sobre el proveedor de este contrato calificado:
            servicioProveedor.grabarReputacion(newCP.getProveedor().getId());

            repositoriocontrato.save(newCP);
        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            throw new MiException("El contrato no existe");
        }

    }

    @Transactional
    public void denunciarComentario(String idContrato) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();
            newCP.setComentarioOfensivo(true);
            repositoriocontrato.save(newCP);
        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            throw new MiException("El contrato no existe");
        }

    }

    public void eliminarComentarioDenunciado(String idContrato) throws MiException {

        Optional<ContratoProveedor> respuestaCP = repositoriocontrato.findById(idContrato);

        ContratoProveedor newCP = new ContratoProveedor();

        if (respuestaCP.isPresent()) {

            newCP = respuestaCP.get();
            newCP.setComentarioFinal(null);
            repositoriocontrato.save(newCP);
        }

        if (respuestaCP.isEmpty() || respuestaCP == null) {

            throw new MiException("El contrato no existe");
        }

    }

    public List<ContratoProveedor> listarContratos() {

        List<ContratoProveedor> contratos = new ArrayList();
        contratos = repositoriocontrato.findAll();
        return contratos;
    }

}
