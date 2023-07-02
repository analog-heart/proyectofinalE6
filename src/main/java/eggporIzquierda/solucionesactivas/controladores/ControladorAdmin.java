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
        
        @Autowired
     private ServicioServicioOfrecido servOfrecidoServicio;

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
    @GetMapping("/admin/listarusuarios_all")
    public String listarUsuarios(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }
    
    @GetMapping("/admin/listarusuarios_activos")
    public String listarUsuariosActivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
       return "usuario_list.html";
    }
    
    @GetMapping("/admin/listarusuarios_inactivos")
    public String listarUsuariosInactivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
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
    
    //-----------------------------Servicios Ofrecidos---------------------
    @GetMapping("/altaservicio_ofrecido")
    public String altaServicio() {
        return "servicio_ofrecido_alta.html";
    }
    @PostMapping("/altaservicio_ofrecido_ok")
    public String guardarServicio(@RequestParam String serv_descripcion, ModelMap modelo) throws MiException {

        try {
            servOfrecidoServicio.registrarServicio(serv_descripcion);
            return "redirect:/registrarproveedor";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
        }

        return "servicio_ofrecido_alta.html";
    }
    //-----------listar
    @GetMapping("/listarServicio_ofrecido")
    public String listarservicio_ofrecido(ModelMap modelo) {
        List<ServicioOfrecido> serviciosList = servOfrecidoServicio.listarServicios();
        modelo.addAttribute("serviciosList", serviciosList);
        return "servicio_ofrecido_list.html";
    }
    //-----------modificar ---
    @GetMapping("/modificar_servicio_ofrecido/{serv_id}")
    public String modificar_servicio_ofrecido(ModelMap modelo ,@PathVariable String serv_id) {
        
           modelo.addAttribute("servicio", servOfrecidoServicio.findById(serv_id));
        
        return "servicio_ofrecido_modificar.html";
        
    }
     
      
    @PostMapping("/modificar_servicio_ofrecido/{serv_id}")
    public String modificar_servicio_ofrecido(@PathVariable @RequestParam String serv_id , @RequestParam String serv_descripcion, MultipartFile serv_imagen, ModelMap modelo) {
        
        try {
            servOfrecidoServicio.modificarServicio( serv_id, serv_descripcion, serv_imagen);
             return "redirect:/";

        } catch (MiException ex) {
          modelo.put("error", ex.getMessage());
           return "servicio_ofrecido_modificar.html";
        }

    }
    
    
}
