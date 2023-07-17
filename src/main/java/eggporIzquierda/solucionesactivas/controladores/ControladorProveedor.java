package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Contrato;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/proveedor")
public class ControladorProveedor {

    @Autowired
    private ServicioServicioOfrecido servOfrecidoServicio;

    @Autowired
    private RepositorioContrato repositorioContrato;

    @Autowired
    private ServicioProveedor proveedorServicio;

    @Autowired
    private ServicioContrato contratoServicio;

    @Autowired
    private ServicioUsuario usuarioServicio;

    @GetMapping("/proveedores")
    public String listar(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        List<Proveedor> proveedores = proveedorServicio.listarProveedoresActivos();
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_list.html";
    }

    @GetMapping("/buscar")
    public String buscarProveedores(String nombre, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        // Agrego logia para probar notificaciones al proveedor
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        List<Proveedor> resultados = proveedorServicio.buscarProveedoresXnombre(nombre);
        modelo.addAttribute("resultados", resultados);
        return "resultado_busqueda.html";
    }

    @GetMapping("/{id}")
    public String contacto(@PathVariable String id, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.addAttribute("proveedor", proveedor);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        // Agrego logia para probar notificaciones al proveedor
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        List<Contrato> contratosCalificados = repositorioContrato.listarPorEstadoCalificado(id);
        modelo.addAttribute("contratosCalificados", contratosCalificados);

        return "proveedor_contratar.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/solicitudes")
    public String solicitudesPendientes(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "contratos_solicitados.html";
    }

    //-----VER MI PERFIL
    @GetMapping("/mi_perfil_proveedor")
    public String miPerfilProveedor(ModelMap modelo, HttpSession session) {

        Proveedor usuario = (Proveedor) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<Contrato> contratosSesion = new ArrayList();
        contratosSesion = contratoServicio.listarContratosSesion(usuario);

        modelo.put("contratosUsuario", contratosSesion);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mi_perfil_proveedor.html";

    }

    //------------MODIFICAR PERFIL PROVEEDOR GET
    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @GetMapping("/modificar_perfil_proveedor")
    public String perfil(ModelMap modelo, HttpSession session) {

        Proveedor usuario = (Proveedor) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));
        modelo.addAttribute("serviciosOfrecidos", servOfrecidoServicio.listarServicios());
        modelo.put("usuario", usuario);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        // modelo.addAttribute("contratos", cantidadContratosSolicitados);
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "proveedor_modificar.html";

    }

    //------------MODIFICAR PERFIL PROVEEDOR POST
    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil_proveedor/{id}")
    public String actualizarProveedor(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,
            @RequestParam String email, ModelMap modelo, String apellido, String dni,
            String telefono) {

        // Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // List<ContratoProveedor> cantidadContratosSolicitados =
        // repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        // modelo.put("cantidadContratosSolicitados",
        // cantidadContratosSolicitados.size());
        try {
            proveedorServicio.actualizar(archivo, id, nombre, email, "proveedor_userNick",
                    apellido, dni, telefono);
            modelo.put("exito", "Proveedor actualizado correctamente!");
            return "redirect:../../inicio";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "proveedor_modificar.html";
        }

    }

    @GetMapping("/proveedor_servicio/{serv_descripcion}")
    public String listarProveedoresXServicio(ModelMap modelo, @PathVariable String serv_descripcion) {

        List<Proveedor> proveedores = proveedorServicio.listarProveedoresconfiltro(serv_descripcion);
        modelo.addAttribute("servicio", serv_descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_list.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/mis_contratos_proveedor")
    public String misContratosProveedor(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<Contrato> contratosSesion = new ArrayList();
        contratosSesion = contratoServicio.listarContratosSesion(usuario);

        modelo.addAttribute("contratosUsuario", contratosSesion);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mis_contratos_proveedor.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/mis_contratos_proveedor_encurso")
    public String misContratosProveedorEncurso(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<Contrato> contratosEncurso = repositorioContrato.listarPorEstadoEncurso(usuario.getId());
        modelo.put("contratosEncurso", contratosEncurso);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mis_contratos_proveedor_encurso.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/mis_contratos_proveedor_calificado")
    public String misContratosProveedorCalificado(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<Contrato> contratosCalificados = repositorioContrato.listarPorEstadoCalificado(usuario.getId());
        modelo.put("contratosCalificados", contratosCalificados);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mis_contratos_proveedor_calificado.html";
    }

    //---------------ACTUALIZAR CLAVE---------------
    @PostMapping("/actualizarclave/{id}")
    public String actualizarClave(@RequestParam String passwordold, @RequestParam String passwordnew,
            @RequestParam String passwordconf, @PathVariable String id, ModelMap modelo) {

        try {
            usuarioServicio.modificarClave(passwordold, passwordnew, passwordconf, id);
            return "redirect:../mi_perfil_proveedor";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "inicio.html";
        }
    }

    //--------------SUSPENDER PERFIL PROVEEDOR------------
    @PostMapping("/suspender_mi_cuenta/{id}")
    public String suspenderCuenta(@PathVariable String id, ModelMap modelo) {

        proveedorServicio.suspenderMiCuenta(id);

        return "redirect:../mi_perfil_proveedor";
    }

    @PostMapping("/reactivar_mi_cuenta/{id}")
    public String reactivarCuenta(@PathVariable String id, ModelMap modelo) {

        proveedorServicio.reactivarMiCuenta(id);

        return "redirect:../mi_perfil_proveedor";
    }

    @GetMapping("/guest/{id}")
    public String contacto(@PathVariable String id, ModelMap modelo) {

        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.addAttribute("proveedor", proveedor);
        
        List<Contrato> contratosCalificados = repositorioContrato.listarPorEstadoCalificado(id);
        modelo.addAttribute("contratosCalificados", contratosCalificados);
        return "proveedor_perfil_guest.html";

    }

}
