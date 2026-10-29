package pe.edu.upeu.autoandes.security;

import pe.edu.upeu.autoandes.enums.Permiso;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class PermisosPorRol {

    private PermisosPorRol() {
    }

    private static final Map<String, Set<Permiso>> ROLE_PERMISSIONS = Map.of(
            "ADMIN", EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.SERVICIO_ESCRIBIR,
                    Permiso.VEHICULO_LEER,
                    Permiso.VEHICULO_ESCRIBIR,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_REGISTRAR,
                    Permiso.ORDEN_ANULAR,
                    Permiso.REPORTE_VER
            ),
            "ASESOR", EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.VEHICULO_LEER,
                    Permiso.VEHICULO_ESCRIBIR,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_REGISTRAR
            ),
            "JEFE_TALLER", EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.SERVICIO_ESCRIBIR,
                    Permiso.VEHICULO_LEER,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_ANULAR,
                    Permiso.REPORTE_VER
            )
    );

    public static Set<Permiso> getPermisosPorRol(String rolNombre) {
        if (rolNombre == null) {
            return Collections.emptySet();
        }
        return ROLE_PERMISSIONS.getOrDefault(rolNombre.toUpperCase(), Collections.emptySet());
    }

    public static Set<Permiso> getPermisosPorRoles(Collection<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Permiso> unionPermisos = EnumSet.noneOf(Permiso.class);
        for (String rol : roles) {
            unionPermisos.addAll(getPermisosPorRol(rol));
        }
        return unionPermisos;
    }
}
