/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eggporIzquierda.solucionesactivas.service;

import eggporIzquierda.solucionesactivas.entity.Domicilio;
import eggporIzquierda.solucionesactivas.exception.MiException;
import eggporIzquierda.solucionesactivas.repository.RepositorioDomicilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ServicioDomicilio {
    @Autowired
    private RepositorioDomicilio domicilioRepositorio;
    
     public Domicilio registrar(String calle, String numero, String barrio, String lote, String codigoPostal, String localidad) throws MiException{
     
          validar(calle, numero, barrio, lote,codigoPostal,localidad );

        Domicilio d = new Domicilio();
                d.setCalle(calle);
                d.setNumero(numero);
                d.setBarrio(barrio);
                d.setLote(lote);
                d.setCodigoPostal(codigoPostal);
                d.setLocalidad(localidad);
                return domicilioRepositorio.save(d);
     
     }
     
     
     
     
      private void validar(String calle, String numero, String barrio, String lote, String codigoPostal, String localidad) throws MiException {

        if (calle.isEmpty() || calle == null) {
            throw new MiException("Calle es un campo requerido");
        }
        if (barrio.isEmpty() || barrio == null) {
            throw new MiException("Barrio es un campo requerido");
        }
        if (codigoPostal.isEmpty() || codigoPostal == null) {
            throw new MiException("Codigo postal es un campo requerido");
        }

       // validar el codigo postal con expresion regular

    }

}

     
   
    
//    public Imagen guardar(MultipartFile archivo) throws MiException{
//        if (archivo != null) {
//            try {
//                Imagen imagen = new Imagen();
//                imagen.setMime(archivo.getContentType());
//                imagen.setNombre(archivo.getName());
//                imagen.setContenido(archivo.getBytes());
//                return imagenRepositorio.save(imagen);
//                
//            } catch (IOException e) {
//                System.err.println(e.getMessage());
//            }
//        }
//        return null;
//    }
    