package pe.edu.upeu.autoandes.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenRequestDTO {

    @NotNull(message = "El servicioId es obligatorio")
    private Long servicioId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima por servicio es 1")
    @Max(value = 5, message = "La cantidad máxima por servicio es 5")
    private Integer cantidad;
}
