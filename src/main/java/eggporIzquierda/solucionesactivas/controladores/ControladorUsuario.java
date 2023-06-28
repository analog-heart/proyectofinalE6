package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/usuario")

public class ControladorUsuario {

    @Autowired
    private ServicioUsuario usuarioservicio;

    
    @GetMapping("/usuarios")
    public String listarUsuariosActivos(ModelMap modelo){
        List<Usuario> usuarios = usuarioservicio.listarUsuariosActivos();
        modelo.addAttribute("usuarios", usuarios);
       return "usuario_list.html";
    }
    
    

    @GetMapping("/buscar")
    public String buscarUsuarios(String nombre,ModelMap modelo) {
         
        List<Usuario> resultados = usuarioservicio.buscarUsuariosXnombre(nombre);
        modelo.addAttribute("resultados", resultados);
        return "resultado_busqueda.html";
    }

    


}


