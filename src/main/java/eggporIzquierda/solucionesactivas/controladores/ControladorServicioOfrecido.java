package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.service.ServicioServicioOfrecido;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/servicioOfrecido")
public class ControladorServicioOfrecido {

    @Autowired
    private ServicioServicioOfrecido servOfrecidoServicio;

    //-----------------------------Servicios Ofrecidos---------------------
    @GetMapping("/altaservicio_ofrecido")
    public String altaServicio() {
        return "servicio_ofrecido_alta.html";
    }

    @PostMapping("/altaservicio_ofrecido_ok")
    public String guardarServicio(@RequestParam String serv_descripcion, ModelMap modelo) throws MiException {

        try {
            servOfrecidoServicio.registrarServicio(serv_descripcion);
            return "redirect:/admin/dashboard";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "servicio_ofrecido_alta.html";
        }

        
    }

    //-----------listar
    @GetMapping("/listarServicio_ofrecido")
    public String listarservicio_ofrecido(ModelMap modelo) {
        List<ServicioOfrecido> serviciosList = servOfrecidoServicio.listarServicios();
        modelo.addAttribute("serviciosList", serviciosList);
        return "servicio_ofrecido_list.html";
    }

    //-----------modificar ---
    @GetMapping("/modificar_servicio_ofrecido/{serv_id}")
    public String modificar_servicio_ofrecido(ModelMap modelo, @PathVariable String serv_id) {

        modelo.addAttribute("servicio", servOfrecidoServicio.findById(serv_id));

        return "servicio_ofrecido_modificar.html";

    }

    @PostMapping("/modificar_servicio_ofrecido/{serv_id}")
    public String modificar_servicio_ofrecido(@PathVariable @RequestParam String serv_id, @RequestParam String serv_descripcion, MultipartFile serv_imagen, ModelMap modelo) {

        try {
            servOfrecidoServicio.modificarServicio(serv_id, serv_descripcion, serv_imagen);
            return "redirect:/servicioOfrecido/listarServicio_ofrecido";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "servicio_ofrecido_modificar.html";
        }

    }
}
