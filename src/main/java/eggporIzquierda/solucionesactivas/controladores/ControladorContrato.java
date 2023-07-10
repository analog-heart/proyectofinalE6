/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Usuario
 */
@Controller
@RequestMapping("/contrato")
public class ControladorContrato {

    @Autowired
    private RepositorioContrato repositorioContrato;

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioProveedor proveedorServicio;

    @Autowired
    private ServicioContrato contratoServicio;

    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @PostMapping("/contratar")
    public String contratar(@RequestParam String idProveedor, @RequestParam String comentarioInicial, ModelMap modelo,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);

        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);

        try {
            contratoServicio.crearContrato(usuario.getId(), idProveedor, comentarioInicial);
            modelo.put("exito", "El contrato fue generado con exito");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "proveedor_contratar.html";
        }
        return "proveedor_list.html";
    }

    // este post viene de una vista del proveedor, el cual acepta o rechaza la solicitud de contratacion
    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @PostMapping("/aceptar_contrato")
    public String aceptarContrato(HttpSession session, @RequestParam String idContrato, @RequestParam String decision,
            ModelMap modelo) {

        String exito = "";
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        try {
            if (decision.equalsIgnoreCase("aceptar")) {
                exito = "El contrato fue aceptado con exito";
            }
            if (decision.equalsIgnoreCase("rechazar")) {
                exito = "El contrato fue rechazado con exito";
            }
            contratoServicio.actualizarContrato(idContrato, decision);

            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());
            // modelo.put("usuario", usuario);
            modelo.put("exito", exito);

            return "inicio.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());
            return "contratos_solicitados.html";
        }

    }

    // este post viene de una vista del proveedor, el cual finaliza el trabajo
    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @PostMapping("/finalizar_contrato_proveedor")
    public String finalizarContratoProveedor(HttpSession session, @RequestParam String idContrato,
            @RequestParam BigDecimal precio, ModelMap modelo) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        try {

            contratoServicio.finalizarContratoProveedor(idContrato, precio);

            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("exito", "El contrato fue finalizado con exito");
            return "mis_contratos_proveedor.html";

        } catch (MiException ex) {
            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("error", ex.getMessage());
            return "mis_contratos_proveedor.html";
        }
    }

    // este post viene de una vista del usuario, el cual cancela la solicitud en curso
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @PostMapping("/finalizar_contrato_usuario")
    public String finalizarContratoUsuario(HttpSession session, @RequestParam String idContrato,
            @RequestParam String comentarioFinal, ModelMap modelo) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        try {

            contratoServicio.finalizarContratoUsuario(idContrato, null, comentarioFinal);

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("exito", "El contrato fue cancelado con exito");
            return "mis_contratos_usuario.html";

        } catch (MiException ex) {
            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("error", ex.getMessage());
            return "mis_contratos_usuario.html";
        }
    }

    // este post viene de una vista del cliente, el cual califica la contratacion
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @PostMapping("/calificar_contrato")
    public String calificarContrato(HttpSession session, @RequestParam String idContrato, ModelMap modelo,
            @RequestParam String comentarioFinal, @RequestParam Integer calificacion) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        try {

            contratoServicio.calificarContrato(idContrato, comentarioFinal, calificacion);

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("exito", "El contrato fue calificado con exito");
            return "mis_contratos_usuario.html";

        } catch (MiException ex) {

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("error", ex.getMessage());
            return "mis_contratos_usuario.html";
        }
    }

    // este post viene de una vista del proveedor, el cual denuncia el comentario
    // del cliente
    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @PostMapping("/denunciar_comentario")
    public String denunciarComentario(HttpSession session, @RequestParam String idContrato, ModelMap modelo) {

        System.out.println("ENTRANDO A CONTROLADOR..");

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        try {

            System.out.println("ENTRANDO A CONTROLADOR tryyyyyyyyyyyyy..");
            contratoServicio.denunciarComentario(idContrato);

            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("exito", "El contrato fue denunciado con exito");
            return "mis_contratos_proveedor.html";

        } catch (MiException ex) {

            System.out.println("ENTRANDO A CONTROLADOR CAAAATHCH..");
            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                    .listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("error", ex.getMessage());
            return "mis_contratos_proveedor_calificado.html";
        }
    }

}
