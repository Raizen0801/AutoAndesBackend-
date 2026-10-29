package pe.edu.upeu.autoandes.service.service;

import pe.edu.upeu.autoandes.dto.response.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    List<ClienteResponseDTO> listarTodos();
    ClienteResponseDTO obtenerPorId(Long id);
}
