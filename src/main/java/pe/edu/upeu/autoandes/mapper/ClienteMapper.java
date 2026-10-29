package pe.edu.upeu.autoandes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setDocumento(cliente.getDocumento());
        dto.setNombres(cliente.getNombres());
        dto.setTelefono(cliente.getTelefono());
        return dto;
    }
}
