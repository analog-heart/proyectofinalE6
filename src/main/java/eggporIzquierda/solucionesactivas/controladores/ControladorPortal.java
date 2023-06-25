package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/")
public class ControladorPortal {
    @Autowired
    private ServicioUsuario usuarioServicio;
    
    @Autowired
    private ServicioProveedor proveedorServicio;

    
     @GetMapping("/")
    public String index() {

        return "index2.html";
    }
//    @GetMapping("/")
//    public String index(ModelMap modelo) {
//         List<Proveedor> ListProveedores = proveedorServicio.findAllbyfechadesc();
//        modelo.addAttribute("proveedores", ListProveedores);
//        return "index.html";
//
//      
//    }

   
    
    
    
    @GetMapping("/registrar")
    public String registrar() {
        return "registrar.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo, MultipartFile archivo, String nombreUsuario, String apellido, Date fechaNacimiento, String dni) {

        try {
            usuarioServicio.registrar(archivo, nombreUsuario, nombre, apellido, fechaNacimiento, dni, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registrar.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "/usuario/usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo, String nombreUsuario, String apellido, Date fechaNacimiento, String dni) {

        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2, nombreUsuario, apellido, fechaNacimiento, dni);
            modelo.put("exito", "Usuario actualizado correctamente!");
            return "inicio.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "/usuario/usuario_modificar.html";
        }

    }
}
