package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Contrato;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.Utility;
import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List; 
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @Autowired
    private JavaMailSender mailSender;
    
    @GetMapping("/")
    public String index(ModelMap modelo) {
         List<ServicioOfrecido> listaServ = servOfrecidoServicio.listarServicios();
        modelo.put("servicios", listaServ);
     
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

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_ADMIN', 'ROLE_PROVEEDOR')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        //Agrego logica para probar notificaciones al proveedor
        modelo.put("usuario", usuario);

        List<Contrato> cantidadContratosSolicitados = repositorioContrato.listarPorEstadoSolicitado(usuario.getId());
        modelo.put("cantidadContratosSolicitados", cantidadContratosSolicitados.size());

        List<ServicioOfrecido> listaServ = servOfrecidoServicio.listarServicios();
        modelo.put("servicios", listaServ);
        // Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        // 
        return "inicio.html";
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
    
    @GetMapping("/forgot_password")
    public String recuperarContraseñaForm(Model modelo) {

        return "password_forgot_form.html";

    }

    @PostMapping("/forgot_password")
    public String forgot_password(@RequestParam String email, ModelMap model, HttpServletRequest request) throws Exception {

        String token = RandomString.make(45);

        try {
            usuarioServicio.updateResetPasswordToken(token, email);
            
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            
            sendEmail(email, resetPasswordLink);
            
            model.put("exito", "Ya te enviamos a tu mail las instrucciones para recuperar tu contraseña.");
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }

         return "password_forgot_form.html";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException,   jakarta.mail.MessagingException,    UnsupportedEncodingException {

        
        MimeMessage message = mailSender.createMimeMessage();
        

        MimeMessageHelper helper = new MimeMessageHelper(message);
        

        helper.setFrom("solucionesactivasegg@gmail.com", "Soluciones Activas");
        helper.setTo(email);
        

        String subject = "Aquí está el link para restablecer tu contraseña";
        String content = "<p>Hola,</p>"
                + "<p>Solicitaste restablecer tu contraseña.</p>"
                + "<p>Haz click en el link de abajo para cambiar tu contraseña:</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Restablecer mi contraseña</a><b></p>"
                + "<p>Ignora este email si recordaste tu contraseña o no solicitaste restablecerla.</p>";
        
        

        helper.setSubject(subject);
        
        helper.setText(content, true);
        

        mailSender.send(message);
        
    }

    @GetMapping("/reset_password")
    public String resetPasswordForm(@Param(value = "token") String token, Model modelo) {

        /* Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token); */
        
        modelo.addAttribute("token", token);
        
        return "password_reset_form.html";
        
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, ModelMap model) {
        
        String token = request.getParameter("token");
        System.out.println("Token recibido" + token);
        
        String password = request.getParameter("password");
       
        Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token);
        
        model.addAttribute("titulo", "Restablecer contraseña");
        
        if (usuario == null) {
            model.put("error", "Token inválido");
            
            return "password_mensaje.html";
        } else {
            try {
                usuarioServicio.updatePassword(usuario, password );
                model.put("exito", "¡Tu contraseña fue cambiada exitosamente!");
            } catch (MiException ex) {
                 model.put("error", ex.getMessage());
            }
           
            
        }
        return "password_mensaje.html";
    }

}
