package pe.edu.upeu.autoandes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;

@Component
public class VehiculoMapper {

    public VehiculoResponseDTO toDTO(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }
        VehiculoResponseDTO dto = new VehiculoResponseDTO();
        dto.setId(vehiculo.getId());
        dto.setPlaca(vehiculo.getPlaca());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());
        dto.setAnio(vehiculo.getAnio());
        if (vehiculo.getCliente() != null) {
            dto.setClienteId(vehiculo.getCliente().getId());
            dto.setClienteNombre(vehiculo.getCliente().getNombres());
        }
        return dto;
    }

    public Vehiculo toEntity(VehiculoRequestDTO dto, Cliente cliente) {
        if (dto == null) {
            return null;
        }
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setCliente(cliente);
        return vehiculo;
    }

    public void updateEntityFromDTO(VehiculoRequestDTO dto, Vehiculo vehiculo, Cliente cliente) {
        if (dto == null || vehiculo == null) {
            return;
        }
        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        if (cliente != null) {
            vehiculo.setCliente(cliente);
        }
    }
}
