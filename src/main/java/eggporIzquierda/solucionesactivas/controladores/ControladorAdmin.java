package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
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
    private ServicioUsuario usuarioservicio;
        
     

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "dashboard_admin.html";
    }
    
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list";
    }
    
    
    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        usuarioservicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }
    
    
    //----------creado 25/06 por Hernan -------------
    //---------usuarios
    @GetMapping("/listarusuarios_all")
    public String listarUsuarios(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }
    
    @GetMapping("/listarusuarios_activos")
    public String listarUsuariosActivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuariosActivos();
        modelo.addAttribute("usuarios", usuarios);
       return "usuario_list.html";
    }
    
    @GetMapping("/listarusuarios_inactivos")
    public String listarUsuariosInactivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuariosInactivos();
        modelo.addAttribute("usuarios", usuarios);
      return "usuario_list.html";
    }
    
    
    
    //-------------Proveedores
     @GetMapping("/admin/listarProvedores_all")
    public String listarProvedores(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }
    
    @GetMapping("/admin/listarProvedores_activos")
    public String listarProvedoresActivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }
    
    @GetMapping("/admin/listarProvedores_inactivos")
    public String listarProvedoresInactivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
       return "usuario_list.html";
    }
    
   
    
    
}
