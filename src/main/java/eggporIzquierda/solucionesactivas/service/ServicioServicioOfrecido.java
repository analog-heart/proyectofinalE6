package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioServicioOfrecido;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioServicioOfrecido {

    @Autowired
    private ServicioImagen imagenServicio;

    @Autowired
    private RepositorioServicioOfrecido servOfrecidoRepositorio;

    
    //-Alta---------------------------
    @Transactional
    public void registrarServicio(String serv_descripcionrecibido) throws MiException {
        String serv_descripcion = serv_descripcionrecibido.toUpperCase();
       
        validar(serv_descripcion);
        ServicioOfrecido serv = new ServicioOfrecido();
        serv.setServ_descripcion(serv_descripcion);
        servOfrecidoRepositorio.save(serv);
        
    }
    
    //-Modificar y Agregar Foto--------
    @Transactional
    public void modificarServicio(String serv_id,String serv_descripcion, MultipartFile serv_imagen) throws MiException {
        validar(serv_descripcion);

        Optional<ServicioOfrecido> respuesta = servOfrecidoRepositorio.findById(serv_id);
        if (respuesta.isPresent()) {
            ServicioOfrecido serv = respuesta.get();
            serv.setServ_descripcion(serv_descripcion);

            if (serv_imagen != null) {
                Imagen imagen = imagenServicio.guardar(serv_imagen);
                serv.setServ_imagen(imagen);
            }

            servOfrecidoRepositorio.save(serv);
        }

    }

     //-Eliminar---------------------------
    @Transactional
    public void eliminarServicio(String serv_id) throws MiException{
        
          Optional<ServicioOfrecido> respuesta = servOfrecidoRepositorio.findById(serv_id);
          if (respuesta.isPresent()) {
              
              
            ServicioOfrecido serv = respuesta.get();
            servOfrecidoRepositorio.delete(serv);
          }
    }
    
    
    
    @Transactional(readOnly = true)
    public List<ServicioOfrecido> listarServicios() {
        List<ServicioOfrecido> serviciosList = new ArrayList();
        serviciosList = servOfrecidoRepositorio.findAll();
        return serviciosList;
    }

    public ServicioOfrecido getOne(String serv_id) {
        return servOfrecidoRepositorio.getOne(serv_id);
    }

    public ServicioOfrecido getOneByDescripcion(String serv_descripcion) {
        return servOfrecidoRepositorio.getOneByDescripcion(serv_descripcion);
    }

    private void validar(String serv_descripcion) throws MiException {

        if (serv_descripcion.isEmpty() || serv_descripcion == null) {
            throw new MiException("La descripcion del Servicio no puede estar vacio");
        }
        
         ServicioOfrecido respuesta = servOfrecidoRepositorio.getOneByDescripcion(serv_descripcion);
        if (respuesta != null) {
             throw new MiException("Ya existe un servicio similar , consulte la base o modifiquelo");
        }
    }

}
