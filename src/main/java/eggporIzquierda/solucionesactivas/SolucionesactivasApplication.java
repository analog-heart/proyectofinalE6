package eggporIzquierda.solucionesactivas;

import eggporIzquierda.solucionesactivas.service.ServicioContrato;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolucionesactivasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolucionesactivasApplication.class, args);
                
                
                ServicioContrato sc = new ServicioContrato();
                sc.crearContrato("11de24e3-5a1f-4b21-8a4f-5e87460dce1d", "13182323-cf12-4e65-90e1-24b7cb6c3a6c");
                
          }
        
       }
