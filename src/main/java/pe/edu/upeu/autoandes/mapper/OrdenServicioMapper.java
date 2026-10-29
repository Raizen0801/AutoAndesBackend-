package pe.edu.upeu.autoandes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;
import java.util.*;

@Component
public class OrdenServicioMapper {

    public OrdenServicioResponseDTO toDTO(OrdenServicio orden) {
        if (orden == null) {
            return null;
        }
        OrdenServicioResponseDTO dto = new OrdenServicioResponseDTO();
        dto.setId(orden.getId());
        dto.setNumero(orden.getNumero());
        dto.setFecha(orden.getFecha());
        dto.setKilometraje(orden.getKilometraje());
        dto.setEstado(orden.getEstado());
        dto.setTotal(orden.getTotal());
        dto.setUsuarioRegistro(orden.getUsuarioRegistro());
        dto.setMotivoAnulacion(orden.getMotivoAnulacion());

        if (orden.getVehiculo() != null) {
            dto.setVehiculoId(orden.getVehiculo().getId());
            dto.setVehiculoPlaca(orden.getVehiculo().getPlaca());
            dto.setVehiculoMarca(orden.getVehiculo().getMarca());
            dto.setVehiculoModelo(orden.getVehiculo().getModelo());
            if (orden.getVehiculo().getCliente() != null) {
                dto.setClienteNombre(orden.getVehiculo().getCliente().getNombres());
            }
        }

        List<DetalleOrdenResponseDTO> detalleDTOs = new ArrayList<>();
        if (orden.getDetalles() != null) {
            for (DetalleOrden d : orden.getDetalles()) {
                detalleDTOs.add(toDetalleDTO(d));
            }
        }
        dto.setDetalles(detalleDTOs);

        return dto;
    }

    public DetalleOrdenResponseDTO toDetalleDTO(DetalleOrden detalle) {
        if (detalle == null) {
            return null;
        }
        DetalleOrdenResponseDTO dto = new DetalleOrdenResponseDTO();
        dto.setId(detalle.getId());
        if (detalle.getServicio() != null) {
            dto.setServicioId(detalle.getServicio().getId());
            dto.setServicioCodigo(detalle.getServicio().getCodigo());
            dto.setServicioNombre(detalle.getServicio().getNombre());
        }
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
