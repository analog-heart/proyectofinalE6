package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
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
        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_list.html";
    }

    @GetMapping("/buscar")
    public String buscarProveedores(String nombre, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
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

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        // Agrego logia para probar notificaciones al proveedor
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "proveedor_contratar.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/solicitudes")
    public String solicitudesPendientes(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.addAttribute("contratos", cantidadContratosSolicitados);
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "contratos_solicitados.html";
    }

    @GetMapping("/mi_perfil_proveedor")
    public String miPerfilProveedor(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<ContratoProveedor> contratosSesion = new ArrayList();
        contratosSesion = contratoServicio.listarContratosSesion(usuario);

        modelo.put("contratosUsuario", contratosSesion);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mi_perfil_proveedor.html";

    }
    
    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @PostMapping("/perfilproveedor/{id}")
    public String actualizarProveedor(HttpSession session, ServicioOfrecido servicios, MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, String password2, String telefono, String nombreUsuario, ModelMap modelo, String apellido, Date fechaNacimiento, String dni) {
        
//Falta validar por separdo las claves pssw
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2, nombreUsuario, apellido, fechaNacimiento, dni, telefono);
            modelo.put("exito", "Usuario actualizado correctamente!");

            return "inicio.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "proveedor_modificar.html";
        }

    
    }

//        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
//        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
//        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());
//
//        try {
//            proveedorServicio.actualizar(servicios, archivo, id, nombre, email, password, password, "", apellido, fechaNacimiento, dni);
//            modelo.put("exito", "Proveedor actualizado correctamente!");
//            return "redirect:../inicio";
//
//        } catch (MiException ex) {
//            modelo.put("error", ex.getMessage());
//            modelo.put("nombre", nombre);
//            modelo.put("email", email);
//            return "proveedor_modificar.html";
//        }
//
//    }
    
    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @GetMapping("/modificar_perfil_proveedor")
    public String modificarPerfilProveedor (ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));
        modelo.put("usuario", usuario);
        
         List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "proveedor_modificar.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @PostMapping("/perfil_proveedor/{id}")
    public String actualizarProveedor(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo, String apellido, String dni,
            String telefono) {

        // Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        // List<ContratoProveedor> cantidadContratosSolicitados =
        // repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        // modelo.put("cantidadContratosSolicitados",
        // cantidadContratosSolicitados.size());
        try {
            proveedorServicio.actualizar(archivo, id, nombre, email, password, password2, "proveedor_userNick",
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

    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN')")
    @GetMapping("/modificar_perfil_proveedor")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));
        modelo.addAttribute("serviciosOfrecidos", servOfrecidoServicio.listarServicios());
        modelo.put("usuario", usuario);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        // modelo.addAttribute("contratos", cantidadContratosSolicitados);
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "proveedor_modificar.html";

    }


    @GetMapping("/proveedor_servicio/{serv_descripcion}")
    public String listarProveedoresXServicio(ModelMap modelo, @PathVariable String serv_descripcion) {

        List<Proveedor> proveedores = proveedorServicio.listarProveedoresconfiltro(serv_descripcion);
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_list.html";
    }


    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/mis_contratos_proveedor")
    public String misContratosProveedor(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<ContratoProveedor> contratosSesion = new ArrayList();
        contratosSesion = contratoServicio.listarContratosSesion(usuario);

        modelo.put("contratosUsuario", contratosSesion);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mis_contratos_proveedor.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/mis_contratos_proveedor_encurso")
    public String misContratosProveedorEncurso(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<ContratoProveedor> contratosEncurso = repositorioContrato.listarPorEstadoEncurso(usuario.getId());
        modelo.put("contratosEncurso", contratosEncurso);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mis_contratos_proveedor_encurso.html";
    }

    @PreAuthorize("hasRole('ROLE_PROVEEDOR')")
    @GetMapping("/mis_contratos_proveedor_calificado")
    public String misContratosProveedorCalificado(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", proveedorServicio.getOne(usuario.getId()));

        List<ContratoProveedor> contratosCalificados = repositorioContrato.listarPorEstadoCalificado(usuario.getId());
        modelo.put("contratosCalificados", contratosCalificados);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato
                .listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mis_contratos_proveedor_calificado.html";
    }
    
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


}

