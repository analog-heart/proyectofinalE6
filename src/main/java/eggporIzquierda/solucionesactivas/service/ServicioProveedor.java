package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.entity.Proveedor;

import eggporIzquierda.solucionesactivas.enumation.EnumNivel;
import eggporIzquierda.solucionesactivas.enumation.EnumServiciosOfrecidos;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
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
public class ServicioProveedor implements UserDetailsService {

    @Autowired
    private RepositorioProveedor proveedorRepositorio;

    @Autowired
    private ServicioImagen imagenServicio;

    @Transactional
    public void registrar(EnumServiciosOfrecidos servicios, MultipartFile archivo, String nombreUsuario, String nombre, String apellido, Date fechaNacimiento, String dni, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreUsuario(nombreUsuario);
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setFechaNacimiento(fechaNacimiento);
        proveedor.setDni(dni);
        proveedor.setEmail(email);
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        proveedor.setRol(Rol.PROVEEDOR);
        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setFotoPerfil(imagen);
        proveedor.setServicios(servicios);
        proveedor.setEstadoProveedorActivo(Boolean.TRUE);
        proveedor.setReputacion(0.0);
        proveedor.setNivel(EnumNivel.INICIAL);
        proveedorRepositorio.save(proveedor);
    }

    @Transactional
    public void actualizar(EnumServiciosOfrecidos servicios, MultipartFile archivo, String id, String nombre, String email, String password, String password2, String nombreUsuario, String apellido, Date fechaNacimiento, String dni) throws MiException {

        validar(nombre, email, password, password2);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();

            proveedor.setNombreUsuario(nombreUsuario);
            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setFechaNacimiento(fechaNacimiento);
            proveedor.setDni(dni);
            proveedor.setEmail(email);
            proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
            proveedor.setRol(Rol.PROVEEDOR);
            proveedor.setServicios(servicios);
            proveedor.setEstadoProveedorActivo(Boolean.TRUE);
            proveedor.setReputacion(0.0);
            proveedor.setNivel(EnumNivel.INICIAL);
            proveedorRepositorio.save(proveedor);

            String idImagen = null;

            if (proveedor.getFotoPerfil() != null) {
                idImagen = proveedor.getFotoPerfil().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            proveedor.setFotoPerfil(imagen);

            proveedorRepositorio.save(proveedor);
        }
    }

    public Proveedor getOne(String id) {
        return proveedorRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedores() {

        List<Proveedor> proveedores = new ArrayList();

        proveedores = proveedorRepositorio.findAll();

        return proveedores;
    }

    @Transactional
    public void cambiarRol(String id) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();

            if (proveedor.getRol().equals(Rol.USUARIO)) {

                proveedor.setRol(Rol.PROVEEDOR);

            } else if (proveedor.getRol().equals(Rol.PROVEEDOR)) {
                proveedor.setRol(Rol.USUARIO);
            }
        }
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
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

        Proveedor proveedor = proveedorRepositorio.buscarPorEmail(email);

        if (proveedor != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + proveedor.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = (HttpSession) attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", proveedor);

            return new User(proveedor.getEmail(), proveedor.getPassword(), permisos);
        } else {
            return null;
        }
    }


    
    
    public List<Proveedor> findAllbyfechadesc() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    

}
