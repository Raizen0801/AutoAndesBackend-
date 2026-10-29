package pe.edu.upeu.autoandes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.enums.*;
import pe.edu.upeu.autoandes.security.*;
import pe.edu.upeu.autoandes.service.service.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
@Tag(name = "Órdenes de Servicio", description = "Gestión cabecera-detalle, registro atómico, anulación y consultas")
@SecurityRequirement(name = "Bearer Authentication")
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    @PostMapping
    @PreAuthorize(Permisos.HAS_ORDEN_REGISTRAR)
    @Operation(summary = "Registrar orden de servicio", description = "Crea cabecera y detalles en una sola transacción atómica (RN-03 y RN-04)")
    public ResponseEntity<OrdenServicioResponseDTO> registrar(
            @Valid @RequestBody OrdenServicioRequestDTO request,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "asesor";
        OrdenServicioResponseDTO creada = ordenServicioService.registrarOrden(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    @PreAuthorize(Permisos.HAS_ORDEN_LEER)
    @Operation(summary = "Listar órdenes de servicio con filtros", description = "Consulta combinable por placa, estado, fecha desde y hasta")
    public ResponseEntity<List<OrdenServicioResponseDTO>> listarConFiltros(
            @RequestParam(name = "placa", required = false) String placa,
            @RequestParam(name = "estado", required = false) EstadoOrden estado,
            @RequestParam(name = "desde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(name = "hasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(ordenServicioService.listarConFiltros(placa, estado, desde, hasta));
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.HAS_ORDEN_LEER)
    @Operation(summary = "Obtener orden por ID", description = "Devuelve el detalle completo de la orden solicitada")
    public ResponseEntity<OrdenServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenServicioService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/anular")
    @PreAuthorize(Permisos.HAS_ORDEN_ANULAR)
    @Operation(summary = "Anular orden de servicio", description = "Anula una orden registrada en el mismo día indicando motivo (RN-05)")
    public ResponseEntity<OrdenServicioResponseDTO> anular(
            @PathVariable Long id,
            @Valid @RequestBody AnularOrdenRequestDTO request) {
        return ResponseEntity.ok(ordenServicioService.anularOrden(id, request));
    }
}
