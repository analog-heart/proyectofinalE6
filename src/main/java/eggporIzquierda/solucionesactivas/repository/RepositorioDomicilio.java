
package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDomicilio extends JpaRepository <Domicilio, String> {


    
    
}
