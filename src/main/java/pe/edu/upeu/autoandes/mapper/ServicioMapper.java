package pe.edu.upeu.autoandes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;

@Component
public class ServicioMapper {

    public ServicioResponseDTO toDTO(Servicio servicio) {
        if (servicio == null) {
            return null;
        }
        ServicioResponseDTO dto = new ServicioResponseDTO();
        dto.setId(servicio.getId());
        dto.setCodigo(servicio.getCodigo());
        dto.setNombre(servicio.getNombre());
        dto.setPrecio(servicio.getPrecio());
        dto.setDuracionMin(servicio.getDuracionMin());
        dto.setActivo(servicio.getActivo());
        return dto;
    }

    public Servicio toEntity(ServicioRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Servicio servicio = new Servicio();
        servicio.setCodigo(dto.getCodigo());
        servicio.setNombre(dto.getNombre());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracionMin(dto.getDuracionMin());
        servicio.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return servicio;
    }

    public void updateEntityFromDTO(ServicioRequestDTO dto, Servicio servicio) {
        if (dto == null || servicio == null) {
            return;
        }
        servicio.setCodigo(dto.getCodigo());
        servicio.setNombre(dto.getNombre());
        servicio.setPrecio(dto.getPrecio());
        servicio.setDuracionMin(dto.getDuracionMin());
        if (dto.getActivo() != null) {
            servicio.setActivo(dto.getActivo());
        }
    }
}
