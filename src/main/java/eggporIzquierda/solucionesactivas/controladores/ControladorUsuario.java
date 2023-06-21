package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")

public class ControladorUsuario {

    @Autowired
    private ServicioUsuario usuarioservicio;


    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioservicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

}


