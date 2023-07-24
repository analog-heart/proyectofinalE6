package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioUsuario;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service

public class ServicioUsuario implements UserDetailsService {

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @Autowired
    private ServicioImagen imagenServicio;
    
    

    @Transactional
    public void registrar(MultipartFile archivo, String nombreUsuario, String nombre, String apellido,
            String fechaNacimiento, String dni, String telefono, String email, String password, String password2)
            throws MiException {

        validar(nombre, apellido,  email,  password,  password2, dni,  telefono);
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEstado(true);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USUARIO);
        // se guarda la fecha de alta (no modificable)
        Date fechatemp = new Date();
        usuario.setFecha(fechatemp);
        
        // se guarda la fecha de nacimiento que llega por formulario
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNac = formato.parse(fechaNacimiento);
            usuario.setFechaNacimiento(fechaNac);
        } catch (ParseException e) {

            e.printStackTrace();
        }

         if (!archivo.isEmpty()) {
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setFotoPerfil(imagen);
         }
        
        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombre, String email, String password,
            String password2, String nombreUsuario, String apellido, Date fechaNacimiento, String dni, String telefono)
            throws MiException {

        //validar(email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setTelefono(telefono);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            
            if (usuario.getFotoPerfil() != null && !archivo.isEmpty()) {
                String idImagen = usuario.getFotoPerfil().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                usuario.setFotoPerfil(imagen);

            } else if (usuario.getFotoPerfil() == null && !archivo.isEmpty()) {

                Imagen imagen = imagenServicio.guardar(archivo);
                usuario.setFotoPerfil(imagen);
            }
            
            usuario.setEstado(true);


            usuarioRepositorio.save(usuario);
        }

    }

    
    
    
    
    
    
    //-----------metodos de busqueda de usuarios
    
    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos() {
        List<Usuario> usuarios = usuarioRepositorio.listarUsuariosActivos();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosInactivos() {
        List<Usuario> usuarios = usuarioRepositorio.listarUsuariosInactivos();
        return usuarios;
    }

    public List<Usuario> buscarUsuariosXnombre(String nombre) {

        List<Usuario> usuariosXnombre = new ArrayList();

        usuariosXnombre = usuarioRepositorio.buscarPorNombre(nombre);

        return usuariosXnombre;
    }

    
    
    
    
    //-------------Dar Permisos de Administrador ---------
    @Transactional
    public void cambiarRol(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.USUARIO)) {

                usuario.setRol(Rol.ADMIN);

            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USUARIO);
            }
        }
    }

 
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            System.out.println("ingresa a loaduser");

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = (HttpSession) attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    public void dar_baja_usuario(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getEstado() == true) {
                usuario.setEstado(false);
            }
        }
    }

 
   
    //-----Autogestion desde Mi_perfil
    public void modificarClave(String passwordold, String passwordnew, String passwordconf, String id) throws MiException {

        validarClaves(passwordold, passwordnew, passwordconf, id);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setPassword(new BCryptPasswordEncoder().encode(passwordnew));

            usuarioRepositorio.save(usuario);
        }

    }

    
      private void validar(String nombre,String apellido, String email, String password, String password2,
                         String dni, String telefono) throws MiException {

        if (nombre.isEmpty()) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        
        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("el apellido no puede ser nulo o estar vacío");
        }
        if (dni.isEmpty() || dni == null || dni.length() != 8) {
            throw new MiException("DNI no valido");
        }
        
        if (telefono.isEmpty() || telefono == null || telefono.length() < 9 || telefono.length() > 20 ) {
            throw new MiException("Telefono no valido");
        }         
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }    
        Usuario respuesta = usuarioRepositorio.buscarPorEmail(email);
        if (respuesta != null) {
             throw new MiException("email ya registrado.");
        }            
        
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }
    
    private void validarClaves(String passwordold, String passwordnew, String passwordconf, String id) throws MiException {

        if (passwordnew.isEmpty() || passwordnew == null) {
            throw new MiException("La contraseña no puede estar vacía");
        }

        if (passwordnew.length() < 5) {
            throw new MiException("La contraseña debe tener al menos 6 caracteres");

        }

        if (!passwordnew.equals(passwordconf)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            String poe = new BCryptPasswordEncoder().encode(passwordold);
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            bc.matches(passwordold, usuario.getPassword());
            if (!bc.matches(passwordold, usuario.getPassword())) {
                System.out.println(usuario.getPassword() + " nueva " + poe);
                throw new MiException("La contraseña anterior, no es válida");
            }

        }

    }

// ---------si encuentra una base de datos vacia creara el admin-----
 @Transactional
    @PostConstruct
    public void crearUsuarios_script() throws MiException {
        List<Usuario> respuesta = usuarioRepositorio.findAll();
        if (respuesta.isEmpty()) {
            // Crear usuario ADMIN
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setEmail("admin@admin.com");
            admin.setApellido("Admin");
            admin.setRol(Rol.ADMIN);
            admin.setTelefono("123456789");
            admin.setPassword(new BCryptPasswordEncoder().encode("123456"));
            usuarioRepositorio.save(admin);

          /*   // Crear 2 usuarios con rol USUARIO
            for (int i = 0; i < 2; i++) {
                Usuario usuario = new Usuario();
                usuario.setNombre("Usuario" + (i + 1));
                usuario.setEmail("usuario" + (i + 1) + "@example.com");
                usuario.setApellido("Apellido" + (i + 1));
                usuario.setRol(Rol.USUARIO);
                usuario.setTelefono("123456789");
                usuario.setPassword(new BCryptPasswordEncoder().encode("123456"));
                usuarioRepositorio.save(usuario);
            }

            // Crear 10 usuarios con rol PROVEEDOR
            for (int i = 0; i < 10; i++) {
                Usuario proveedor = new Usuario();
                proveedor.setNombre("Proveedor" + (i + 1));
                proveedor.setEmail("proveedor" + (i + 1) + "@example.com");
                proveedor.setApellido("Apellido" + (i + 1));
                proveedor.setRol(Rol.PROVEEDOR);
                proveedor.setTelefono("123456789");
                proveedor.setPassword(new BCryptPasswordEncoder().encode("123456"));
                usuarioRepositorio.save(proveedor);
            } */
        }
    }
   
        public void suspenderMiCuenta(String id) {
       
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setEstado(false);
            usuarioRepositorio.save(usuario);
        }
    }

    public void reactivarMiCuenta(String id) {
         Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setEstado(true);
            usuarioRepositorio.save(usuario);
        }

     }
    
     public void updateResetPasswordToken(String token, String email) throws Exception {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            usuario.setResetPasswordToken(token);
            usuarioRepositorio.save(usuario);
        } else {

            throw new Exception("No se encuentra ningun usuario con el email: " + email);
        }
    }

    public Usuario obtenrUsuarioPorToken(String resetPasswordToken) {
        return usuarioRepositorio.findByResetPasswordToken(resetPasswordToken);
    }

    
    public void updatePassword(Usuario usuario, String newPassword){
    
        usuario.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        usuario.setResetPasswordToken(null);
        usuarioRepositorio.save(usuario);
    
    }

}
