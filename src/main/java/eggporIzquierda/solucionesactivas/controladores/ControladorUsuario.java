package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
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
@RequestMapping("/usuario")
public class ControladorUsuario {

    @Autowired
    private RepositorioContrato repositorioContrato;

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioContrato contratoServicio;

    @GetMapping("/usuarios")
    public String listarUsuariosActivos(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuariosActivos();
        modelo.addAttribute("usuarios", usuarios);
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
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo, String nombreUsuario, String apellido, String fechaNacimiento, String dni, String telefono) {
//Falta validar por separdo las claves pssw
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2, nombreUsuario, apellido, fechaNacimiento, dni, telefono);
            modelo.put("exito", "Usuario actualizado correctamente!");

            return "inicio.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "usuario_modificar.html";
        }

    }

    @GetMapping("/mi_perfil_usuario")
    public String mi_perfil_usuario(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.addAttribute("usuario", usuarioServicio.getOne(usuario.getId()));

        List<ContratoProveedor> contratosSesion = new ArrayList();

        contratosSesion = contratoServicio.listarContratosSesion(usuario);
        modelo.put("contratosUsuario", contratosSesion);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        return "mi_perfil_usuario.html";

    }

    @GetMapping("/dar_baja_usuario")
    public String dar_baja_usuario(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        usuarioServicio.dar_baja_usuario(usuario.getId());

        return "mi_perfil_usuario.html";

    }
}
