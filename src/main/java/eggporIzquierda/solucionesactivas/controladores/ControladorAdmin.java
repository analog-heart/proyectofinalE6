package eggporIzquierda.solucionesactivas.controladores;


import eggporIzquierda.solucionesactivas.entity.Contrato;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/admin")
public class ControladorAdmin {

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioProveedor proveedorServicio;

    @Autowired
    private RepositorioProveedor proveedorRepositorio;

    @Autowired
    private RepositorioContrato contratoRepositorio;

    @Autowired
    private ServicioContrato contratoServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {
        List<Proveedor> proveedoresinactivos = proveedorRepositorio.listarSolicitudesProvedoresNuevos();
        modelo.addAttribute("cantSolict", proveedoresinactivos.size());
        return "dashboardadmin.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }

    // ----------creado 25/06 por Hernan -------------
    // ---------usuarios
    @GetMapping("/listarusuarios_all")
    public String listarUsuarios(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @GetMapping("/listarusuarios_activos")
    public String listarUsuariosActivos(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuariosActivos();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @GetMapping("/listarusuarios_inactivos")
    public String listarUsuariosInactivos(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuariosInactivos();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    // -------------Proveedores

    @GetMapping("/listarProvedores_activos")
    public String listarProvedoresActivos(ModelMap modelo) {
        List<Proveedor> proveedoresinactivos = proveedorRepositorio.listarProveedoresActivos();
        modelo.addAttribute("proveedores", proveedoresinactivos);
        return "proveedor_list.html";
    }

    @GetMapping("/listarProvedores_inactivos")
    public String listarProvedoresInactivos(ModelMap modelo) {
        List<Proveedor> proveedoresinactivos = proveedorRepositorio.listarProveedoresInactivos();
        modelo.addAttribute("proveedores", proveedoresinactivos);
        return "proveedor_list.html";
    }

    @GetMapping("/dashboard/contratos_denunciados")
    public String contratosComentariosDenunciados(ModelMap modelo) {

        List<Contrato> contratosComentariosDenunciados = contratoRepositorio.listarPorComentarioDenunciado();
        modelo.addAttribute("contratosComentariosDenunciados", contratosComentariosDenunciados);

        return "dashboard_contratos_denunciados.html";
    }

    @PostMapping("/dashboard/comentario_eliminado")
    public String eliminarComentarioDenunciado(ModelMap modelo, String idContrato) throws MiException {

        contratoServicio.eliminarComentarioDenunciado(idContrato);
        List<Contrato> contratosComentariosDenunciados = contratoRepositorio.listarPorComentarioDenunciado();
        modelo.addAttribute("contratosComentariosDenunciados", contratosComentariosDenunciados);

        return "dashboard_contratos_denunciados.html";
    }

    // ----------------------

    @GetMapping("/autorizar_solicitudes_nuevo")
    public String autorizarSolicitudesNuevosProveedores(ModelMap modelo) {

        List<Proveedor> proveedoresinactivos = proveedorRepositorio.listarSolicitudesProvedoresNuevos();
        modelo.addAttribute("proveedoresInactivos", proveedoresinactivos);
        return "autorizar_nuevoproveedor.html";
    }

    @PostMapping("/autorizar_proveedor/{id}")
    public String autorizarnuevoproveedor(ModelMap modelo, @PathVariable String id) {

        proveedorServicio.autorizarnuevoproveedor(id);

        return "redirect:../autorizar_solicitudes_nuevo";

    }

    
    @GetMapping("/modificar_usuario/{id}")
     public String modificarUsuario_admin(ModelMap modelo, @PathVariable String id) {

         Usuario usuario = usuarioServicio.getOne(id);
        modelo.addAttribute("usuario",usuario );
        
        return "usuario_modificar_admin.html";
    }
    
     @PostMapping("/modificado_usuario/{id}")
   public String modificadoUsuario_admin(MultipartFile archivo, @RequestParam String id, @RequestParam String nombre,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelo, String nombreUsuario,
            String apellido, String fechaNacimiento, String dni, String telefono) {
        
       
        try {
            usuarioServicio.modifcar_por_Admin(archivo, id, nombre, email, password, password2, nombreUsuario, apellido,
                    fechaNacimiento, dni, telefono);
            System.out.println("entro al try");
            modelo.put("exito", "Usuario actualizado correctamente!");

           return "redirect:/admin/dashboard";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            

              return "usuario_modificar_admin.html";
        }

    }
      
        
        
    
    

}
