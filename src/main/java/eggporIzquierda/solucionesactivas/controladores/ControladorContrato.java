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

    //vista de testeo para generar contratos ingresando los id de las partes via inputs (descontinuada en breve)
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/alta_manual")
    public String contrato() {
        return "contrato_crear_manual_testing.html";
    }

    //vista para que el proveedor acepte o rechace la solicitud de contratacion
//    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
//    @GetMapping("/aceptacion")
//    public String aceptacion(ModelMap modelo) {
//        List<ContratoProveedor> cotratatos = repositorioContrato.listarPorEstado("SOLICITADO");
//        modelo.addAttribute("cotratatos", cotratatos);
//        return "aceptar_contrato.html";
//    }
    //este post recibe los IDs de las partes para generar una solicitu de contratacion (crea contrato en estado SOLICITADO)
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @PostMapping("/contratar")
    public String contratar(@RequestParam String idProveedor,@RequestParam String comentarioInicial, ModelMap modelo, HttpSession session) {

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

    //este post viene de una vista del proveedor, el cual acepta o rechaza la solicitud de contratacion
    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @PostMapping("/aceptar_contrato")
    public String aceptar_contrato(HttpSession session, @RequestParam String idContrato, @RequestParam String decision, ModelMap modelo) {

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

            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());
            //modelo.put("usuario", usuario);
            modelo.put("exito", exito);

            return "inicio.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());
            return "contratos_solicitados.html";
        }

    }

//este post viene de una vista del proveedor, el cual acepta o rechaza la solicitud de contratacion
    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @PostMapping("/finalizar_contrato")
    public String finalizar_contrato(HttpSession session, @RequestParam String idContrato, ModelMap modelo) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        try {

            contratoServicio.finalizarContrato(idContrato);

            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("exito", "El contrato fue finalizado con exito");
            return "mi_perfil_proveedor.html";

        } catch (MiException ex) {
            List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
            modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

            List<ContratoProveedor> contratosSesion = new ArrayList();
            contratosSesion = contratoServicio.listarContratosSesion(usuario);
            modelo.put("contratosUsuario", contratosSesion);

            modelo.put("error", ex.getMessage());
            return "mi_perfil_proveedor.html";
        }
    }

}
