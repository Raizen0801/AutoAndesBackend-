package pe.edu.upeu.autoandes.dto.response;

import lombok.*;
import java.math.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngresoServicioReporteDTO {
    private String codigo;
    private String servicio;
    private Long cantidadTotal;
    private BigDecimal importeTotal;
}
