package eggporIzquierda.solucionesactivas.controladores;

import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.service.ServicioProveedor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/proveedor")

public class ControladorProveedor {

    @Autowired
    private ServicioProveedor proveedorServicio;

    @GetMapping("/proveedores")
    public String listar(ModelMap modelo) {
        List<Proveedor> proveedores = proveedorServicio.listarProveedores();
        modelo.addAttribute("proveedores", proveedores);
        return "proveedor_list.html";
    }

    @GetMapping("/buscar")
    public String buscarProveedores(String nombre, ModelMap modelo) {

        List<Proveedor> resultados = proveedorServicio.buscarProveedoresXnombre(nombre);
        modelo.addAttribute("resultados", resultados);
        return "resultado_busqueda.html";
    }

    @GetMapping("/{id}")
    public String contacto(@PathVariable String id, ModelMap modelo) {
        Proveedor proveedor = proveedorServicio.getOne(id);
        modelo.addAttribute("proveedor", proveedor);
    return "proveedor_contratar.html";
    }
    
    
}
