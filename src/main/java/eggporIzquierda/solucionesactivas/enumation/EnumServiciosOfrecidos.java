package eggporIzquierda.solucionesactivas.enumation;

public enum EnumServiciosOfrecidos {

    ELECTRICISTA ("ELECTRICISTA"),
    PLOMERO ("PLOMERO"),
    GASISTA ("GASISTA"),
    ALBANIL ("ALBAÑIL"),
    ABOGADO ("ABOGADO"),
    CONTADOR ("CONTADOR"),
    ESCRIBANO ("ESCRIBANO"),
    PERSONALTRAINER ("PERSONALTRAINER"),
    MASAJISTA ("MASAJISTA"),
    PASEADORMASCOTAS ("PASEADORMASCOTAS");

    private String nombre;

    private EnumServiciosOfrecidos(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }
    
}
