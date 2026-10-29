package pe.edu.upeu.autoandes.security;

public final class Permisos {

    private Permisos() {
    }

    public static final String SERVICIO_LEER = "SERVICIO_LEER";
    public static final String SERVICIO_ESCRIBIR = "SERVICIO_ESCRIBIR";
    public static final String VEHICULO_LEER = "VEHICULO_LEER";
    public static final String VEHICULO_ESCRIBIR = "VEHICULO_ESCRIBIR";
    public static final String ORDEN_LEER = "ORDEN_LEER";
    public static final String ORDEN_REGISTRAR = "ORDEN_REGISTRAR";
    public static final String ORDEN_ANULAR = "ORDEN_ANULAR";
    public static final String REPORTE_VER = "REPORTE_VER";

    public static final String HAS_SERVICIO_LEER = "hasAuthority('SERVICIO_LEER')";
    public static final String HAS_SERVICIO_ESCRIBIR = "hasAuthority('SERVICIO_ESCRIBIR')";
    public static final String HAS_VEHICULO_LEER = "hasAuthority('VEHICULO_LEER')";
    public static final String HAS_VEHICULO_ESCRIBIR = "hasAuthority('VEHICULO_ESCRIBIR')";
    public static final String HAS_ORDEN_LEER = "hasAuthority('ORDEN_LEER')";
    public static final String HAS_ORDEN_REGISTRAR = "hasAuthority('ORDEN_REGISTRAR')";
    public static final String HAS_ORDEN_ANULAR = "hasAuthority('ORDEN_ANULAR')";
    public static final String HAS_REPORTE_VER = "hasAuthority('REPORTE_VER')";
}
