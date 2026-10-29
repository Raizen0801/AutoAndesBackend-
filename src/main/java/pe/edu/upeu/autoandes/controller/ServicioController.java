package pe.edu.upeu.autoandes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.autoandes.dto.request.CambiarEstadoServicioDTO;
import pe.edu.upeu.autoandes.dto.request.ServicioRequestDTO;
import pe.edu.upeu.autoandes.dto.response.ServicioResponseDTO;
import pe.edu.upeu.autoandes.security.Permisos;
import pe.edu.upeu.autoandes.service.service.ServicioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "CRUD y catálogo de servicios del taller")
@SecurityRequirement(name = "Bearer Authentication")
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    @PreAuthorize(Permisos.HAS_SERVICIO_LEER)
    @Operation(summary = "Listar servicios", description = "Retorna la lista de todos los servicios registrados")
    public ResponseEntity<List<ServicioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.HAS_SERVICIO_LEER)
    @Operation(summary = "Obtener servicio por ID", description = "Retorna el servicio especificado por su identificador")
    public ResponseEntity<ServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.HAS_SERVICIO_ESCRIBIR)
    @Operation(summary = "Crear nuevo servicio", description = "Registra un nuevo servicio en el catálogo (RN-01)")
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO request) {
        ServicioResponseDTO creado = servicioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.HAS_SERVICIO_ESCRIBIR)
    @Operation(summary = "Actualizar servicio", description = "Actualiza la información de un servicio existente (RN-01)")
    public ResponseEntity<ServicioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioRequestDTO request) {
        return ResponseEntity.ok(servicioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.HAS_SERVICIO_ESCRIBIR)
    @Operation(summary = "Eliminar servicio", description = "Elimina físicamente un servicio si no tiene órdenes asociadas (RN-01)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize(Permisos.HAS_SERVICIO_ESCRIBIR)
    @Operation(summary = "Alternar estado del servicio", description = "Permite alternar o actualizar el estado activo/inactivo (RN-01)")
    public ResponseEntity<ServicioResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody(required = false) CambiarEstadoServicioDTO request) {
        return ResponseEntity.ok(servicioService.cambiarEstado(id, request));
    }
}
