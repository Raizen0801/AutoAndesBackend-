package pe.edu.upeu.autoandes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.security.*;
import pe.edu.upeu.autoandes.service.service.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Reportes consolidados y estadísticas de ingresos")
@SecurityRequirement(name = "Bearer Authentication")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ingresos-por-servicio")
    @PreAuthorize(Permisos.HAS_REPORTE_VER)
    @Operation(summary = "Reporte de ingresos por servicio",
            description = "Consulta JPQL agregada agrupando por servicio, excluyendo órdenes anuladas y ordenada por importe total descendente")
    public ResponseEntity<List<IngresoServicioReporteDTO>> obtenerIngresosPorServicio(
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(reporteService.obtenerIngresosPorServicio(desde, hasta));
    }
}
