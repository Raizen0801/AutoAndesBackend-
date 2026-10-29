package pe.edu.upeu.autoandes.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pe.edu.upeu.autoandes.entity.Rol;
import pe.edu.upeu.autoandes.enums.Permiso;

import java.util.*;

public final class PermisosPorRol {

    private static final Map<String, Set<Permiso>> PERMISOS = Map.of(
            "ADMIN", Collections.unmodifiableSet(EnumSet.allOf(Permiso.class)),
            "ASESOR", Collections.unmodifiableSet(EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.VEHICULO_LEER,
                    Permiso.VEHICULO_ESCRIBIR,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_REGISTRAR)),
            "JEFE_TALLER", Collections.unmodifiableSet(EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.SERVICIO_ESCRIBIR,
                    Permiso.VEHICULO_LEER,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_ANULAR,
                    Permiso.REPORTE_VER))
    );

    private PermisosPorRol() {}

    public static Set<Permiso> de(String nombreRol) {
        return PERMISOS.getOrDefault(nombreRol, Set.of());
    }

    public static Set<Permiso> getPermisosPorRoles(Collection<String> roles) {
        Set<Permiso> permisos = new HashSet<>();
        if (roles != null) {
            for (String rol : roles) {
                permisos.addAll(de(rol));
            }
        }
        return permisos;
    }

    public static List<GrantedAuthority> authorities(Collection<Rol> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles == null) return authorities;

        for (Rol rol : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
            for (Permiso p : de(rol.getNombre())) {
                authorities.add(new SimpleGrantedAuthority(p.name()));
            }
        }
        return authorities;
    }
}