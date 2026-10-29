package pe.edu.upeu.autoandes.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pe.edu.upeu.autoandes.enums.*;
import java.math.*;
import java.time.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioResponseDTO {
    private Long id;
    private String numero;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;

    private Long vehiculoId;
    private String vehiculoPlaca;
    private String vehiculoMarca;
    private String vehiculoModelo;
    private String clienteNombre;

    private Integer kilometraje;
    private EstadoOrden estado;
    private BigDecimal total;
    private String usuarioRegistro;
    private String motivoAnulacion;

    private List<DetalleOrdenResponseDTO> detalles;
}
