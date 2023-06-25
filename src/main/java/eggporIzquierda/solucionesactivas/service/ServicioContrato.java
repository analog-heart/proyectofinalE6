package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.ContratoProveedor;
import eggporIzquierda.solucionesactivas.entity.Proveedor;
import eggporIzquierda.solucionesactivas.entity.Usuario;
import eggporIzquierda.solucionesactivas.enumation.EnumEstadoContrato;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioContrato;
import eggporIzquierda.solucionesactivas.repository.RepositorioProveedor;
import eggporIzquierda.solucionesactivas.repository.RepositorioUsuario;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioContrato {

    @Autowired
    private RepositorioContrato repositoriocontrato;
    @Autowired
    private RepositorioProveedor repositorioproveedor;
    @Autowired
    private RepositorioUsuario repositoriousuario;

    @Transactional
    public void crearContrato() throws MiException {

//    Proveedor p = repositorioproveedor.findById(idProveedor).get();
//    Usuario u = repositoriousuario.findById(idUsuario).get();
        ContratoProveedor CP = new ContratoProveedor();

        CP.setEstado(EnumEstadoContrato.SOLICITADO);
//    CP.setProveedor(p);
//    CP.setUsuario(u);
        CP.setFechaContrato(new Date());

        repositoriocontrato.save(CP);
    }

}
