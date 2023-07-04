/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Domicilio;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioDomicilio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioDomicilio {

    @Autowired
    private RepositorioDomicilio domicilioRepositorio;

    public Domicilio registrar(String calle, String numero, String barrio, String lote, String codigoPostal, String localidad) throws MiException {

        validar(calle, numero, barrio, lote);

        Domicilio d = new Domicilio();
        d.setCalle(calle);
        d.setNumero(numero);
        d.setBarrio(barrio);
        d.setLote(lote);
        d.setCodigoPostal(codigoPostal);
        d.setLocalidad(localidad);
        return domicilioRepositorio.save(d);

    }

    private void validar(String calle, String numero, String barrio, String lote) throws MiException {

        if (calle.isEmpty() ) {
            throw new MiException("Calle es un campo requerido");
        }
        if (barrio.isEmpty() ) {
            throw new MiException("Barrio es un campo requerido");
        }

        // validar el codigo postal con expresion regular
    }

    Domicilio modificar(String calle, String numero, String barrio, String lote, String IdDom) {

        Domicilio d = new Domicilio();
     
       
                if (IdDom != null) {
                    Optional<Domicilio> respuesta = domicilioRepositorio.findById(IdDom);
                    if (respuesta.isPresent()) {
                        d = respuesta.get();
                        
                        d.setCalle(calle);
                        d.setNumero(numero);
                        d.setBarrio(barrio);
                        d.setLote(lote);
                        return domicilioRepositorio.save(d);
                    }
                }
                
                
            return null;
    }

}
