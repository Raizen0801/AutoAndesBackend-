package pe.edu.upeu.autoandes.service.service;

import pe.edu.upeu.autoandes.dto.response.*;

import java.time.*;
import java.util.*;

public interface ReporteService {
    List<IngresoServicioReporteDTO> obtenerIngresosPorServicio(LocalDate desde, LocalDate hasta);
}
