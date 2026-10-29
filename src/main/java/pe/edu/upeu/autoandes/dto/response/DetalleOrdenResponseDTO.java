package pe.edu.upeu.autoandes.dto.response;

import lombok.*;
import java.math.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenResponseDTO {
    private Long id;
    private Long servicioId;
    private String servicioCodigo;
    private String servicioNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
