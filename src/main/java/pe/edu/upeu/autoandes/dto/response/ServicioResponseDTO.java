package pe.edu.upeu.autoandes.dto.response;

import lombok.*;
import java.math.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private BigDecimal precio;
    private Integer duracionMin;
    private Integer activo;
}
