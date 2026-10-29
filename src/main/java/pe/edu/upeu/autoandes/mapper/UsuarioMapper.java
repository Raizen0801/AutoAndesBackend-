package pe.edu.upeu.autoandes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.enums.*;
import pe.edu.upeu.autoandes.security.*;

import java.util.*;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        Set<String> roles = new HashSet<>();
        if (usuario.getRoles() != null) {
            for (Rol r : usuario.getRoles()) {
                roles.add(r.getNombre());
            }
        }

        Set<Permiso> enumPermisos = PermisosPorRol.getPermisosPorRoles(roles);
        Set<String> permisos = new HashSet<>();
        for (Permiso p : enumPermisos) {
            permisos.add(p.name());
        }

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setActivo(usuario.getActivo());
        dto.setRoles(roles);
        dto.setPermisos(permisos);

        return dto;
    }
}
