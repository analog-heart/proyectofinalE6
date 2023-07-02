package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
import eggporIzquierda.solucionesactivas.service.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ControladorImagen {

        @Autowired
     private ServicioServicioOfrecido servOfrecidoServicio;
    
    @Autowired
    ServicioUsuario usuarioServicio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {
        Usuario usuario = (Usuario) usuarioServicio.getOne(id);
        byte[] imagen = usuario.getFotoPerfil().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

     @GetMapping("/servicio_ofrecido/{id}")
    public ResponseEntity<byte[]> imagenServicio(@PathVariable String id) {
        ServicioOfrecido servicio = servOfrecidoServicio.getOne(id);
        byte[] imagen = servicio.getServ_imagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentType(MediaType.IMAGE_GIF);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
        
}
