package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Contrato;
import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.enumation.EnumNivel;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.repository.RepositorioDomicilio;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.repository.RepositorioServicioOfrecido;
import eggporIzquierda.solucionesactivas.repository.RepositorioUsuario;
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
public class ServicioProveedor implements UserDetailsService {

    @Autowired
    private RepositorioProveedor proveedorRepositorio;

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @Autowired
    private RepositorioContrato contratoRepositorio;

    @Autowired
    private ServicioImagen imagenServicio;

    @Autowired
    private RepositorioServicioOfrecido servOfrecidoServicio;

    @Transactional
    public void registrar(String serviciosID2, String serviciosID, MultipartFile archivo, String nombreUsuario,
            String nombre, String apellido, String fechaNacimiento, String dni, String email, String password,
            String password2, String telefono) throws MiException {

        validar(nombre, apellido, email, password, password2, dni, telefono);
        Proveedor proveedor = new Proveedor();
        // ----------recupero con el id el dato de la clase servicio

        Optional<ServicioOfrecido> respuesta = servOfrecidoServicio.findById(serviciosID);

        if (respuesta.isPresent()) {
            ServicioOfrecido servicioTemporal = respuesta.get();
            // ----------creo una lista de servicios , le guardo los datos que recupere con
            // el id y lo seteo en proveedor
            List<ServicioOfrecido> serviciosList = new ArrayList<>();
            serviciosList.add(servicioTemporal);
            proveedor.setServicios(serviciosList);
            System.out.println(serviciosList.toString());
        }

        proveedor.setNombreUsuario(nombreUsuario);
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setDni(dni);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        proveedor.setTelefono(telefono);
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        proveedor.setEstadoProveedorActivo(Boolean.FALSE);
        proveedor.setReputacion(0.0);
        proveedor.setNivel(EnumNivel.INICIAL);
        // al registarse se guarda como usuario y solo cuando el admin lo acepte
        // cambiara a PROVEEDOR
        proveedor.setRol(Rol.USUARIO);

        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNac = formato.parse(fechaNacimiento);
            proveedor.setFechaNacimiento(fechaNac);
        } catch (ParseException e) {

            e.printStackTrace();
        }

        if (!archivo.isEmpty()) {
            Imagen imagen = imagenServicio.guardar(archivo);
            proveedor.setFotoPerfil(imagen);
        }

        // seteo fecha de alta
        Date fechatemp = new Date();
        proveedor.setFecha(fechatemp);

        proveedorRepositorio.save(proveedor);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombre, String email,
            String nombreUsuario, String apellido, String dni, String telefono) throws MiException {

        validardatosbasicos(nombre, apellido, email, dni, telefono);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Proveedor proveedor = respuesta.get();

            proveedor.setNombreUsuario(nombreUsuario);
            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setDni(dni);
            proveedor.setEmail(email);
            proveedor.setTelefono(telefono);

            if (proveedor.getFotoPerfil() != null && !archivo.isEmpty()) {
                String idImagen = proveedor.getFotoPerfil().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                proveedor.setFotoPerfil(imagen);

            } else if (proveedor.getFotoPerfil() == null && !archivo.isEmpty()) {

                Imagen imagen = imagenServicio.guardar(archivo);
                proveedor.setFotoPerfil(imagen);

            }

            proveedorRepositorio.save(proveedor);
        }
    }

    public Proveedor getOne(String id) {
        return proveedorRepositorio.getById(id);
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

    public void grabarReputacion(String id) throws MiException {

        List<Contrato> contratos = contratoRepositorio.listarPorEstadoCalificado(id);
        Double acumulador = 0d;

        for (int i = 0; i < contratos.size(); i++) {
            if (contratos.get(i).getCalificacion() != null) {
                acumulador = acumulador + contratos.get(i).getCalificacion();
            }
        }

        Double reputacion = acumulador / contratos.size();
        System.out.println("La reputacion actual es: " + reputacion);

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setReputacion(reputacion);
            proveedorRepositorio.save(proveedor);
        }

        if (respuesta.isEmpty()) {
            throw new MiException("El proveedor no existe");
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

    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedores() {
        List<Proveedor> proveedores = proveedorRepositorio.findAll();
        return proveedores;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedoresActivos() {
        List<Proveedor> proveedores = proveedorRepositorio.listarProveedoresActivos();
        return proveedores;
    }

    public List<Proveedor> buscarProveedoresXnombre(String nombre) {
        List<Proveedor> proveedoresXnombre = proveedorRepositorio.buscarPorNombre(nombre);
        return proveedoresXnombre;
    }

    public List<Proveedor> buscarProveedoresxPalabraClave(String palabraClave) {
        List<Proveedor> proveedoresList = proveedorRepositorio.listarXpalabraClave(palabraClave);
        return proveedoresList;

    }

    public List<Proveedor> listarProveedoresconfiltro(String serv_descripcion) {
        return proveedorRepositorio.listarProveedoresXServicio(serv_descripcion);

    }

    // -------autogestion desde mi perfil
    public void suspenderMiCuenta(String id) {

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setEstadoProveedorActivo(false);
            proveedorRepositorio.save(proveedor);
        }
    }

    public void reactivarMiCuenta(String id) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setEstadoProveedorActivo(true);
            proveedorRepositorio.save(proveedor);
        }

    }

    public void cantidadDeTrabajos(String id) throws MiException {

        System.out.println("log 4");

        List<Contrato> contratosCalif = contratoRepositorio.listarPorEstadoCalificado(id);
        List<Contrato> contratosTerminados = contratoRepositorio.listarPorEstadoTerminado(id);

        Integer cantidadTotal = contratosCalif.size() + contratosTerminados.size();

        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setCantidadTrabajos(cantidadTotal);
            proveedorRepositorio.save(proveedor);
        }

        if (respuesta.isEmpty()) {
            throw new MiException("El proveedor no existe");
        }

    }

    private void validardatosbasicos(String nombre, String apellido, String email, String dni, String telefono)
            throws MiException {
        if (nombre.isEmpty()) {
            throw new MiException("el nombre no puede estar vacío");
        }

        if (apellido.isEmpty()) {
            throw new MiException("el apellido no estar vacio");
        }
        if (email.isEmpty()) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
        if (dni.isEmpty()) {
            throw new MiException("el dni no puede  estar vacio");
        }
        if (telefono.isEmpty()) {
            throw new MiException("el email no puede estar vacio");
        }
    }

    private void validar(String nombre, String apellido, String email, String password, String password2, String dni,
            String telefono) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new MiException("el apellido no puede ser nulo o estar vacío");
        }
        if (dni.isEmpty() || dni == null || dni.length() != 8) {
            throw new MiException("DNI no valido");
        }

        if (telefono.isEmpty() || telefono == null || telefono.length() < 9 || telefono.length() > 20) {
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

    // ------Autorizacion Inicial del Admin----

    public void autorizarnuevoproveedor(String id) {
        Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Proveedor proveedor = respuesta.get();
            proveedor.setEstadoProveedorActivo(true);
            proveedor.setRol(Rol.PROVEEDOR);
            proveedorRepositorio.save(proveedor);
        }
    }

}
