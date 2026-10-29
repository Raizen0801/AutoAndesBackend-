package pe.edu.upeu.autoandes.exception.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.time.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private Integer status;

    private String error;

    private String mensaje;

    private String ruta;

    private Map<String, String> campos;

    public ErrorResponseDTO(LocalDateTime timestamp, Integer status, String error, String mensaje, String ruta) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }
}
