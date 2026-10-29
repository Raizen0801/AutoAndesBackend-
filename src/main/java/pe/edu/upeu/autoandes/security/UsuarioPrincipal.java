package pe.edu.upeu.autoandes.security;

import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.enums.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPrincipal implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String nombreCompleto;
    private boolean activo;
    private Collection<? extends GrantedAuthority> authorities;
    private Set<String> roles;
    private Set<String> permisos;

    public static UsuarioPrincipal build(Usuario usuario) {
        Set<String> rolesNombres = new HashSet<>();
        if (usuario.getRoles() != null) {
            for (Rol rol : usuario.getRoles()) {
                rolesNombres.add(rol.getNombre());
            }
        }

        Set<Permiso> permisosEnum = PermisosPorRol.getPermisosPorRoles(rolesNombres);
        Set<String> permisosStr = new HashSet<>();
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Permiso p : permisosEnum) {
            permisosStr.add(p.name());
            authorities.add(new SimpleGrantedAuthority(p.name()));
        }

        return new UsuarioPrincipal(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getNombreCompleto(),
                usuario.getActivo() != null && usuario.getActivo() == 1,
                authorities,
                rolesNombres,
                permisosStr
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
