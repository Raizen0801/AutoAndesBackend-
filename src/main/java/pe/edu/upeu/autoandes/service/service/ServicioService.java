package pe.edu.upeu.autoandes.service.service;

import pe.edu.upeu.autoandes.dto.request.CambiarEstadoServicioDTO;
import pe.edu.upeu.autoandes.dto.request.ServicioRequestDTO;
import pe.edu.upeu.autoandes.dto.response.ServicioResponseDTO;

import java.util.List;

public interface ServicioService {
    List<ServicioResponseDTO> listarTodos();
    ServicioResponseDTO obtenerPorId(Long id);
    ServicioResponseDTO crear(ServicioRequestDTO request);
    ServicioResponseDTO actualizar(Long id, ServicioRequestDTO request);
    void eliminar(Long id);
    ServicioResponseDTO cambiarEstado(Long id, CambiarEstadoServicioDTO request);
}
