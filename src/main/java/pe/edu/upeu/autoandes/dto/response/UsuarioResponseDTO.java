package pe.edu.upeu.autoandes.dto.response;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String nombreCompleto;
    private Integer activo;
    private Set<String> roles;
    private Set<String> permisos;
}
