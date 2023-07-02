
package eggporIzquierda.solucionesactivas.repository;

import eggporIzquierda.solucionesactivas.entity.ServicioOfrecido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface RepositorioServicioOfrecido extends JpaRepository <ServicioOfrecido, String>{

    @Query("SELECT s FROM ServicioOfrecido s WHERE s.nombreServicio = :nombreServicio")
    public ServicioOfrecido getOneBynombreServicio(@Param("nombreServicio")String nombreServicio);
    
    
}
