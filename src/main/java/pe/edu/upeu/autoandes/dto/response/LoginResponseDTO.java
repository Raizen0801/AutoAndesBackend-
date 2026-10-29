package pe.edu.upeu.autoandes.dto.response;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private Long expiraEnSegundos = 3600L;
    private String nombreCompleto;
    private Set<String> roles;
    private Set<String> permisos;
}
