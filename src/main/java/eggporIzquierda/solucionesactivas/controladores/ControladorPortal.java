package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
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
    private ServicioServicioOfrecido servOfrecidoServicio;

    @Autowired
    private ServicioUsuario usuarioServicio;

    @Autowired
    private ServicioProveedor proveedorServicio;

    @Autowired
    private ServicioContrato contratoServicio;

    @GetMapping("/")

    public String index() {

        return "index.html";
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
            String password2, ModelMap modelo, MultipartFile archivo, String nombreUsuario, String apellido, Date fechaNacimiento, String dni, String telefono) {

        try {
            usuarioServicio.registrar(archivo, nombreUsuario, nombre, apellido, fechaNacimiento, dni, telefono, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registrar.html";

        }

    }

    @GetMapping("/registrarproveedor")
    public String registrarProveedor(ModelMap modelo) {
        modelo.addAttribute("serviciosOfrecidos", servOfrecidoServicio.listarServicios());
        return "registrar_proveedor.html";
    }

    @PostMapping("/registroproveedor")
    public String registroProveedor(@RequestParam String serviciosID, MultipartFile archivo, String nombreUsuario, @RequestParam String nombre, @RequestParam String apellido, Date fechaNacimiento, String dni, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo) {

        try {

            proveedorServicio.registrar(serviciosID, archivo, nombreUsuario, nombre, apellido, fechaNacimiento, dni, email, password, password2);
            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registrar_proveedor.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN', 'ROLE_PROVEEDOR')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN', 'ROLE_PROVEEDOR')")

    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        if (usuario.getRol().toString().equals("PROVEEDOR")) {
            modelo.put("usuario", usuario);
            return "/proveedor/proveedor_modificar.html";

        } else {
            modelo.put("usuario", usuario);
            return "/usuario/usuario_modificar.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN', 'ROLE_PROVEEDOR')")
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

    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR')")
    @PostMapping("/perfilproveedor/{id}")
    public String actualizarProveedor(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo, String nombreUsuario, String apellido, Date fechaNacimiento, String dni) {

        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2, nombreUsuario, apellido, fechaNacimiento, dni);
            modelo.put("exito", "Proveedor actualizado correctamente!");
            return "inicio.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "/usuario/usuario_modificar.html";
        }

    }

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

    //Agrego el controlador para probar la generación de los contratos
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/contrato")
    public String contrato() {
        return "contrato.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/contratar")
    public String contratar(@RequestParam String idUsuario, @RequestParam String idProveedor, ModelMap modelo) {

        System.out.println("ID USUARIO: " + idUsuario);
        System.out.println("ID PROVEEDOR: " + idProveedor);

        try {

            contratoServicio.crearContrato(idUsuario, idProveedor);

            modelo.put("exito", "El contrato fue generado con exito");

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "contrato.html";
        }

        return "contrato.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/aceptacion")
    public String aceptacion() {
        return "aceptar_contrato.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/aceptar_contrato")
    public String aceptar_contrato(@RequestParam String idContrato, @RequestParam String decision, ModelMap modelo) {

        String exito = "";

        System.out.println("ID CONTRATO: " + idContrato);

        try {
            if (decision.equalsIgnoreCase("aceptar")) {
                exito = "El contrato fue aceptado con exito";
            }
            if (decision.equalsIgnoreCase("rechazar")) {
                exito = "El contrato fue rechazado con exito";
            }

//            contratoServicio.aceptarContrato(idContrato);
            contratoServicio.actualizarContrato(idContrato, decision);

            modelo.put("exito", exito);

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            return "aceptar_contrato.html";
        }

        return "inicio.html";
    }

}
