package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioUsuario;
import jakarta.servlet.http.HttpSession;
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
    public void registrar(MultipartFile archivo, String nombreUsuario, String nombre, String apellido, Date fechaNacimiento, String dni, String telefono, String email, String password, String password2) throws MiException {

        validar(email, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);

        //se guarda la fecha de alta (no modificable)
        Date fechatemp = new Date();
        usuario.setFecha(fechatemp);

        //se guarda la fecha de nacimiento que llega por formulario
        fechatemp = fechaNacimiento;
        usuario.setFechaNacimiento(fechatemp);

        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USUARIO);

        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setFotoPerfil(imagen);
        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombre, String email, String password, String password2, String nombreUsuario, String apellido, Date fechaNacimiento, String dni) throws MiException {

        validar(email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setApellido(apellido);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setDni(dni);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USUARIO);

            String idImagen = null;

            if (usuario.getFotoPerfil() != null) {
                idImagen = usuario.getFotoPerfil().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setFotoPerfil(imagen);

            usuarioRepositorio.save(usuario);
        }

    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosActivos() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.listarUsuariosActivos();
        return usuarios;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosInactivos() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.listarUsuariosInactivos();
        return usuarios;
    }

    public List<Usuario> buscarUsuariosXnombre(String nombre) {

        List<Usuario> usuariosXnombre = new ArrayList();

        usuariosXnombre = usuarioRepositorio.buscarPorNombre(nombre);

        return usuariosXnombre;
    }

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

    private void validar(String email, String password, String password2) throws MiException {

        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = (HttpSession) attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

}
