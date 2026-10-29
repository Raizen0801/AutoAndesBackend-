package pe.edu.upeu.autoandes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.service.service.*;

import java.time.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final OrdenServicioRepository ordenServicioRepository;

    @Override
    public List<IngresoServicioReporteDTO> obtenerIngresosPorServicio(LocalDate desde, LocalDate hasta) {
        log.info("Generando reporte de ingresos por servicio (JPQL agregado) con rango desde: {} hasta: {}", desde, hasta);

        LocalDateTime desdeDT = desde != null ? desde.atStartOfDay() : null;
        LocalDateTime hastaDT = hasta != null ? hasta.atTime(LocalTime.MAX) : null;

        List<IngresoServicioReporteDTO> resultado = ordenServicioRepository.obtenerReporteIngresosPorServicio(desdeDT, hastaDT);
        log.info("Reporte generado exitosamente con {} filas de servicios", resultado.size());

        return resultado;
    }
}
