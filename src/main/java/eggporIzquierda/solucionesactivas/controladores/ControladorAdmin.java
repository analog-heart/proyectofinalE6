package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
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
    private ServicioProveedor proveedorservicio;
        
      @Autowired
    private RepositorioProveedor  proveedorRepo;
      
         @Autowired
        private RepositorioContrato contratoRepositorio;
     

    @Autowired
    private ServicioContrato contratoServicio;
         
         
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {
         List<Proveedor> proveedoresinactivos = proveedorRepo.listarSolicitudesProvedoresNuevos();
         modelo.addAttribute("cantSolict", proveedoresinactivos.size());
        return "dashboardadmin.html";
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
  
   
    @GetMapping("/listarProvedores_activos")
    public String listarProvedoresActivos(ModelMap modelo){
        List<Proveedor> proveedoresinactivos = proveedorRepo.listarProveedoresActivos();
        modelo.addAttribute("proveedores", proveedoresinactivos);
        return "proveedor_list.html";
    }
    
    @GetMapping("/listarProvedores_inactivos")
    public String listarProvedoresInactivos(ModelMap modelo){
        List<Proveedor> proveedoresinactivos = proveedorRepo.listarProveedoresInactivos();
        modelo.addAttribute("proveedores", proveedoresinactivos);
        return "proveedor_list.html";
    }
    
   
     @GetMapping("/dashboard/contratos_denunciados")
    public String listarContratosDenunciados(ModelMap modelo) {

        System.out.println("ENTRANDO AL CONTROLADOR");

        //List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        List<ContratoProveedor> contratosDenunciados = contratoRepositorio.listarPorEstadoDenunciado();

        System.out.println("CONTRATOS DENUNCIADOS: " + contratosDenunciados);
        //modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("contratosDenunciados", contratosDenunciados);
        //modelo.put("contratosDenunciados", contratosDenunciados);

        System.out.println("ENTRANDO AL CONTROLADOR 2");

        return "contratos_denunciados.html";
    }

    @PostMapping("/dashboard/comentario_eliminado")
    public String eliminarComentarioDenunciado(ModelMap modelo, String idContrato) throws MiException {
        //List<Usuario> usuarios = usuarioservicio.listarUsuarios();

        contratoServicio.eliminarComentarioDenunciado(idContrato);
        
        List<ContratoProveedor> contratosDenunciados = contratoRepositorio.listarPorEstadoDenunciado();

        //modelo.addAttribute("usuarios", usuarios);
        modelo.addAttribute("contratosDenunciados", contratosDenunciados);

        return "contratos_denunciados.html";
    }

    
    
    
    
    //----------------------
    
    @GetMapping("/autorizar_solicitudes_nuevo")
    public String autorizarSolicitudesNuevosProveedores(ModelMap modelo) {

        List<Proveedor> proveedoresinactivos = proveedorRepo.listarSolicitudesProvedoresNuevos();
        modelo.addAttribute("proveedoresInactivos", proveedoresinactivos);
        return "autorizar_nuevoproveedor.html";
    }
    
    @PostMapping("/autorizar_proveedor/{id}")
    public String autorizarnuevoproveedor(ModelMap modelo , @PathVariable String id  ) {

       proveedorservicio.autorizarnuevoproveedor(id);
       
       return "redirect:../autorizar_solicitudes_nuevo";
       
    }
    
}
