package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.enumation.EnumNivel;
import eggporIzquierda.solucionesactivas.enumation.Rol;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.repository.RepositorioServicioOfrecido;
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
    private ServicioImagen imagenServicio;
    
     @Autowired
    private RepositorioServicioOfrecido servOfrecidoServicio;

    @Transactional
    public void registrar(String serviciosID2, String serviciosID, MultipartFile archivo, String nombreUsuario, String nombre, String apellido, String fechaNacimiento, String dni, String email, String password, String password2, String telefono) throws MiException {

        
        validar(nombre, email, password, password2);
         Proveedor proveedor = new Proveedor();
         //----------recupero con el id el dato de la clase servicio
       
         
         Optional <ServicioOfrecido> respuesta = servOfrecidoServicio.findById(serviciosID);
       
        if (respuesta.isPresent()) {
         ServicioOfrecido servicioTemporal = respuesta.get();
        //----------creo una lista de servicios , le guardo los datos que recupere con el id y lo seteo en proveedor
        List <ServicioOfrecido> serviciosList = new ArrayList<>();
        serviciosList.add(servicioTemporal);
         proveedor.setServicios(serviciosList);
            System.out.println( serviciosList.toString());
        }  
       
        proveedor.setNombreUsuario(nombreUsuario);
        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        
        proveedor.setDni(dni);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        proveedor.setTelefono(telefono);
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        proveedor.setRol(Rol.PROVEEDOR);

        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNac = formato.parse(fechaNacimiento);
            proveedor.setFechaNacimiento(fechaNac);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        
        if(archivo != null){
        Imagen imagen = imagenServicio.guardar(archivo);
        proveedor.setFotoPerfil(imagen);
        }
        
       if(archivo == null){
           
       }
       //seteo fecha de alta
        Date fechatemp = new Date();
        proveedor.setFecha(fechatemp);
        
        proveedor.setEstadoProveedorActivo(Boolean.TRUE);
        proveedor.setReputacion(0.0);
        proveedor.setNivel(EnumNivel.INICIAL);
        proveedorRepositorio.save(proveedor);
        
         
    }

        
        
    @Transactional
    public void actualizar(ServicioOfrecido servicios, MultipartFile archivo, String id, String nombre, String email, String password,
            String password2, String nombreUsuario, String apellido, Date fechaNacimiento, String dni) throws MiException {

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
            
            List <ServicioOfrecido> serviciosList = new ArrayList<>();
            serviciosList.add(servicios);
            proveedor.setServicios(serviciosList);
           
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
    
    @Transactional(readOnly = true)
    public List<Proveedor> listarProveedoresActivos() {

        List<Proveedor> proveedores = new ArrayList();

        proveedores = proveedorRepositorio.listarProveedoresActivos();
        
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
    
       public List<Proveedor> buscarProveedoresXnombre(String nombre){
       
       List<Proveedor> proveedoresXnombre = new ArrayList();
       
       proveedoresXnombre = proveedorRepositorio.buscarPorNombre(nombre);
       
       return proveedoresXnombre;
   }

  
    public List<Proveedor> buscarProveedoresxFiltro(String palabraClave) {
       List<Proveedor> proveedoresList= new ArrayList();
       
       proveedoresList = proveedorRepositorio.listarXpalabraClave(palabraClave);
       
       return proveedoresList;
        
    }


       
    
       
       //----------
       @Transactional
    public void registrar(String serviciosID2, String serviciosID, MultipartFile archivo, String nombreUsuario, String nombre, String apellido, Date fechaNacimiento, String dni, String email, String password, String password2) throws MiException {
        
        validar(nombre, email, password, password2);
         Proveedor proveedor = new Proveedor();
         //----------recupero con el id el dato de la clase servicio
       
         
         Optional <ServicioOfrecido> respuesta = servOfrecidoServicio.findById(serviciosID);
         Optional <ServicioOfrecido> respuesta2 = servOfrecidoServicio.findById(serviciosID2);
       
        if (respuesta.isPresent()) {
         ServicioOfrecido servicioTemporal = respuesta.get();
        //----------creo una lista de servicios , le guardo los datos que recupere con el id y lo seteo en proveedor
        List <ServicioOfrecido> serviciosList = new ArrayList<>();
        serviciosList.add(servicioTemporal);
         proveedor.setServicios(serviciosList);
            
        } 
          if (respuesta2.isPresent()) {
         ServicioOfrecido servicioTemporal = respuesta2.get();
        //----------creo una lista de servicios , le guardo los datos que recupere con el id y lo seteo en proveedor
        List <ServicioOfrecido> serviciosList2 = proveedor.getServicios();
        serviciosList2.add(servicioTemporal);
         proveedor.setServicios(serviciosList2);
            
        }  
       
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
        
        proveedor.setEstadoProveedorActivo(Boolean.TRUE);
        proveedor.setReputacion(0.0);
        proveedor.setNivel(EnumNivel.INICIAL);
        proveedorRepositorio.save(proveedor);
        
         
    }
       
}
