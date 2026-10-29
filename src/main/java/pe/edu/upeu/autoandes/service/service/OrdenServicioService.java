package pe.edu.upeu.autoandes.service.service;

import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.enums.*;

import java.time.*;
import java.util.*;

public interface OrdenServicioService {
    OrdenServicioResponseDTO registrarOrden(OrdenServicioRequestDTO request, String username);
    OrdenServicioResponseDTO anularOrden(Long id, AnularOrdenRequestDTO request);
    OrdenServicioResponseDTO obtenerPorId(Long id);
    List<OrdenServicioResponseDTO> listarConFiltros(String placa, EstadoOrden estado, LocalDate desde, LocalDate hasta);
}
