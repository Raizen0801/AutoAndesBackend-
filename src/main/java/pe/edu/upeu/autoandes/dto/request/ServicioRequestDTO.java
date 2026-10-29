package pe.edu.upeu.autoandes.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^SRV-\\d{3}$", message = "El código debe tener el formato SRV-000")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 15, message = "La duración debe ser como mínimo de 15 minutos")
    @Max(value = 480, message = "La duración no puede superar los 480 minutos")
    private Integer duracionMin;

    private Integer activo;
}
