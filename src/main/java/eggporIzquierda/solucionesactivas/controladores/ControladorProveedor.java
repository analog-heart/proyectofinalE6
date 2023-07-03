package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/proveedor")
public class ControladorProveedor {

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioProveedor proveedorServicio;

    @Autowired
    private ServicioContrato contratoServicio;

    @GetMapping("/proveedores")
    public String listar(ModelMap modelo) {
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_list.html";
    }

    @GetMapping("/buscar")
    public String buscarProveedores(String nombre, ModelMap modelo) {

        List<Proveedor> resultados = proveedorServicio.buscarProveedoresXnombre(nombre);
        modelo.addAttribute("resultados", resultados);
        return "resultado_busqueda.html";
    }

    @GetMapping("/{id}")
    public String contacto(@PathVariable String id, ModelMap modelo) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.addAttribute("proveedor", proveedor);
        return "proveedor_contratar.html";
    }

    ////////////////////////////////////////////////////////////////////
    //Agrego el controlador para probar la generación de los contratos
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/contrato")
    public String contrato() {
        return "contrato.html";

    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/aceptacion")
    public String aceptacion() {
        return "aceptar_contrato.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @PostMapping("/aceptar_contrato")
    public String aceptar_contrato(@RequestParam String idContrato, @RequestParam String decision, ModelMap modelo) {

        String exito = "";
        System.out.println("ID CONTRATO: " + idContrato);

        try {
            if (decision.equalsIgnoreCase("aceptar")) {
                exito = "El contrato fue aceptado con exito";
            }
            if (decision.equalsIgnoreCase("rechazar")) {
                exito = "El contrato fue rechazado con exito";
            }
//            contratoServicio.aceptarContrato(idContrato);
            contratoServicio.actualizarContrato(idContrato, decision);
            modelo.put("exito", exito);
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "aceptar_contrato.html";
        }
        return "inicio.html";
    }

    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @PostMapping("/contratar")
    public String contratar(@RequestParam String idProveedor, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);

        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);

        try {
            contratoServicio.crearContrato(usuario.getId(), idProveedor);
            modelo.put("exito", "El contrato fue generado con exito");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "proveedor_contratar.html";
        }
        return "proveedor_list.html";
    }

}
