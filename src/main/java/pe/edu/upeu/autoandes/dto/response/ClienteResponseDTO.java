package pe.edu.upeu.autoandes.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private Long id;
    private String documento;
    private String nombres;
    private String telefono;
}
