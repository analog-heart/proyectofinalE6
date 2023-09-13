package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Contrato;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    private RepositorioContrato repositorioContrato;

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioContrato contratoServicio;

    @Autowired
    private ServicioProveedor proveedorServicio;

    @GetMapping("/usuarios")
    public String listarUsuariosActivos(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuariosActivos();
        modelo.addAttribute("usuarios", usuarios);

        // ESTA VISTA VA A SER SOLO PARA LOS LOGEADOS? EN ESE CASO AGREGAR PREAUTH
        // Y SI ESTA VISTA LA TIENE EL PROVEEDOR, HAY QUE INYECTAR LOS CONTRATOS
        // SOLICITADOS (BRIAN)
        return "usuario_list.html";
    }

    @GetMapping("/buscar")
    public String buscarUsuarios(String nombre, ModelMap modelo) {

        List<Usuario> resultados = usuarioServicio.buscarUsuariosXnombre(nombre);
        modelo.addAttribute("resultados", resultados);
        return "resultado_busqueda.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
    @GetMapping("/modificar_perfil_usuario")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));
        modelo.put("usuario", usuario);

        return "usuario_modificar_v2.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
    @PostMapping("/usuario_modificar/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,
            @RequestParam String email, String nombreUsuario,
            String apellido, Date fechaNacimiento, String dni, String telefono, ModelMap modelo) {
        // Falta validar por separdo las claves pssw
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, nombreUsuario, apellido,
                    fechaNacimiento, dni, telefono);
            modelo.put("exito", "Usuario actualizado correctamente!");

            return "inicio.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "usuario_modificar_v2.html";
        }

    }

    @GetMapping("/mi_perfil_usuario")
    public String miPerfilUsuario(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        List<Contrato> contratosSesion = contratoServicio.listarContratosSesion(usuario);

        modelo.put("contratosUsuario", contratosSesion);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mi_perfil_usuario.html";

    }

    @GetMapping("/dar_baja_usuario")
    public String darBajaUsuario(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        usuarioServicio.dar_baja_usuario(usuario.getId());

        return "mi_perfil_usuario.html";

    }

    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @GetMapping("/mis_contratos_usuario")
    public String misContratosUsuario(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        List<Contrato> contratosSesion = contratoServicio.listarContratosSesion(usuario);

        modelo.put("contratosUsuario", contratosSesion);
        // agregado por lucho y juan
        List<Proveedor> proveedores = proveedorServicio.listarProveedoresActivos();
        modelo.addAttribute("proveedores", proveedores);
        modelo.put("proveedores", proveedores);

        return "mis_contratos_usuario.html";
    }

    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @GetMapping("/mis_contratos_usuario_presupuestado")
    public String misContratosUsuarioPresupuestado(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        List<Contrato> contratosPresupuestados = repositorioContrato
                .listarPorEstadoPresupuestadoCliente(usuario.getId());
        modelo.put("contratosPresupuestados", contratosPresupuestados);

        return "mis_contratos_usuario_presupuestado.html";
    }

    //esta vista mostrara contratos SOLICITADOS y/o ENCURSO
    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @GetMapping("/mis_contratos_usuario_encurso")
    public String misContratosUsuarioEncurso(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        List<Contrato> contratosEncurso = repositorioContrato.listarPorEstadoEncursoCliente(usuario.getId());
        modelo.put("contratosEncurso", contratosEncurso);

        return "mis_contratos_usuario_encurso.html";
    }

    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @GetMapping("/mis_contratos_usuario_terminado")
    public String misContratosUsuarioTerminado(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        List<Contrato> contratosTerminados = repositorioContrato
                .listarPorEstadoTerminadoCliente(usuario.getId());
        modelo.put("contratosTerminados", contratosTerminados);

        return "mis_contratos_usuario_terminado.html";
    }

    @PreAuthorize("hasRole('ROLE_USUARIO')")
    @GetMapping("/usuario_calificar_contrato/{id}")
    public String usuarioCalificarContrato(ModelMap modelo, HttpSession session, @PathVariable String id) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        Contrato contrato = repositorioContrato.getReferenceById(id);
        
        modelo.put("contrato", contrato);

        return "usuario_calificar_contrato.html";
    }

    @PostMapping("/actualizarclave/{id}")
    public String actualizarClave(@RequestParam String passwordold, @RequestParam String passwordnew,
            @RequestParam String passwordconf, @PathVariable String id, ModelMap modelo) {

        try {
            usuarioServicio.modificarClave(passwordold, passwordnew, passwordconf, id);
            return "redirect:../mi_perfil_usuario";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "inicio.html";

        }

    }

    // --------------SUSPENDER PERFIL PROVEEDOR------------
    @PostMapping("/suspender_mi_cuenta/{id}")
    public String suspenderCuenta(@PathVariable String id, ModelMap modelo) {
        usuarioServicio.suspenderMiCuenta(id);
        return "redirect:../mi_perfil_usuario";
    }

    @PostMapping("/reactivar_mi_cuenta/{id}")
    public String reactivarCuenta(@PathVariable String id, ModelMap modelo) {

        usuarioServicio.reactivarMiCuenta(id);

        return "redirect:../mi_perfil_usuario";
    }
}
