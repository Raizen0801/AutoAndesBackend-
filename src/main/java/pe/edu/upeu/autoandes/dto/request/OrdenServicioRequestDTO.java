package pe.edu.upeu.autoandes.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioRequestDTO {

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El kilometraje es obligatorio")
    @Min(value = 1, message = "El kilometraje debe ser mayor a 0")
    private Integer kilometraje;

    @NotNull(message = "Los detalles de la orden son obligatorios")
    @Size(min = 1, max = 10, message = "Una orden debe tener entre 1 y 10 detalles")
    @Valid
    private List<DetalleOrdenRequestDTO> detalles;
}
