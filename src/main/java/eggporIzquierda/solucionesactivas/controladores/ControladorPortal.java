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
import org.springframework.data.repository.query.Param;
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
    private ServicioServicioOfrecido servOfrecidoServicio;

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioProveedor proveedorServicio;


    @Autowired
    private RepositorioContrato repositorioContrato;

    @GetMapping("/")
    public String index(ModelMap modelo) {
     
     return "index.html";

    }

    //-------------------------BUSCADOR--------------------
    @GetMapping("/buscar")
    public String buscar(ModelMap modelo, String palabraClave) {
        
        List<Proveedor> proveedores = proveedorServicio.buscarProveedoresxPalabraClave(palabraClave);

        modelo.addAttribute("proveedores", proveedores);

        return "proveedor_list.html";

    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar.html";

    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            String password2, ModelMap modelo, MultipartFile archivo, String nombreUsuario, String apellido, String fechaNacimiento, String dni, String telefono) {

        try {
            usuarioServicio.registrar(archivo, nombreUsuario, nombre, apellido, fechaNacimiento, dni, telefono, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            modelo.put("fechaNacimiento", fechaNacimiento);
            modelo.put("archivo", archivo);
            modelo.put("email", email);

            return "registrar.html";

        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo, HttpSession session) {

        if (error != null) {

            modelo.put("error", "Usuario o Contraseña invalidos!");
            session.removeAttribute("usuariosession");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        //Agrego logica para probar notificaciones al proveedor
        modelo.put("usuario", usuario);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        List<ServicioOfrecido> listaServ = servOfrecidoServicio.listarServicios();
        modelo.put("servicios", listaServ);
        // Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        // 
        return "inicio.html";
    }
    
     @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR')")
    @GetMapping("/inicio_proveedor")
    public String inicio_proveedor(ModelMap modelo, HttpSession session) {

        //Agrego logia para probar notificaciones al proveedor
        Proveedor proveedor = (Proveedor) session.getAttribute("proveedorsession");
        modelo.put("proveedor", proveedor);

        List<ContratoProveedor> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(proveedor.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        
         List<ServicioOfrecido>  listaServ = servOfrecidoServicio.listarServicios();
         modelo.put("servicios", listaServ);
        // Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        // if (logueado.getRol().toString().equals("ADMIN")) {
        //     return "redirect:/admin/dashboard";
        // }
        return "inicio_proveedor.html";
    }

    @GetMapping("/registrarproveedor")
    public String registrarProveedor(ModelMap modelo) {
        modelo.addAttribute("serviciosOfrecidos", servOfrecidoServicio.listarServicios());
        return "registrar_proveedor.html";
    }

    @PostMapping("/registroproveedor")
    public String registroProveedor(String serviciosID2, @RequestParam String serviciosID, MultipartFile archivo, String nombreUsuario, @RequestParam String nombre, @RequestParam String apellido, String fechaNacimiento, String dni, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo, String telefono) {

        try {

            proveedorServicio.registrar(serviciosID2, serviciosID, archivo, nombreUsuario, nombre, apellido, fechaNacimiento, dni, email, password, password2, telefono);
            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            List<ServicioOfrecido> serviciosOfrecidos = servOfrecidoServicio.listarServicios();
            modelo.put("serviciosOfrecidos", serviciosOfrecidos);

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("dni", dni);
            modelo.put("telefono", telefono);
            modelo.put("fechaNacimiento", fechaNacimiento);
            modelo.put("archivo", archivo);
            modelo.put("email", email);

            return "registrar_proveedor.html";
        }

    }

}
