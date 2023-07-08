package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Imagen;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioImagen;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioImagen {
      @Autowired
    private RepositorioImagen imagenRepositorio;
    
    public Imagen guardar(MultipartFile archivo) throws MiException{
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
                
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException{
         if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional(readOnly = true)
	public List<Imagen> listarTodos() {
		return imagenRepositorio.findAll();
	}
      
}

//-------------------------------
//    @Autowired
//    private ResourceLoader resourceLoader;
//----------------------------------------------------
    
//    @PostConstruct
//    public void cargarImagenes() throws IOException {
//        
//        
//        String[] serv_descripcio = {"ELECTRICISTA", "PEQUEÑOS_ARREGLOS", "PLOMERO", "PINTOR", "TECNICO_AA", "GASISTA", "ALBAÑIL", "JARDINERO", "CARPINTERO", "FUMIGADOR"};
//        for (String x : serv_descripcio) {
//            Imagen imagen_serv = (Imagen)resourceLoader.getResource(String.format("classpath:static/IMG/ICONS_SERVICIOS/%s.jpg", x));
//            
//         
//            imagen_serv.setMime("serv_image/png");
//            imagen_serv.setNombre("electricista.png");
//            imagen_serv.setContenido(imagen_serv.getContenido());
//            imagenRepositorio.save(imagen_serv);
//
//        }
//
//    }
