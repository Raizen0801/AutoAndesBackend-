package pe.edu.upeu.autoandes.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnularOrdenRequestDTO {

    @NotBlank(message = "El motivo de anulación es obligatorio")
    @Size(min = 10, max = 200, message = "El motivo de anulación debe tener entre 10 y 200 caracteres")
    private String motivo;
}
