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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.autoandes.dto.request.VehiculoRequestDTO;
import pe.edu.upeu.autoandes.dto.response.VehiculoResponseDTO;
import pe.edu.upeu.autoandes.security.Permisos;
import pe.edu.upeu.autoandes.service.service.VehiculoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "CRUD de vehículos asociados a clientes")
@SecurityRequirement(name = "Bearer Authentication")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    @PreAuthorize(Permisos.HAS_VEHICULO_LEER)
    @Operation(summary = "Listar vehículos", description = "Consulta todos los vehículos, con opción de filtrar por clienteId")
    public ResponseEntity<List<VehiculoResponseDTO>> listar(
            @RequestParam(name = "clienteId", required = false) Long clienteId) {
        return ResponseEntity.ok(vehiculoService.listar(clienteId));
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.HAS_VEHICULO_LEER)
    @Operation(summary = "Obtener vehículo por ID", description = "Devuelve el vehículo por su identificador")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize(Permisos.HAS_VEHICULO_ESCRIBIR)
    @Operation(summary = "Registrar nuevo vehículo", description = "Crea un nuevo vehículo validando placa única y cliente existente (RN-02)")
    public ResponseEntity<VehiculoResponseDTO> crear(@Valid @RequestBody VehiculoRequestDTO request) {
        VehiculoResponseDTO creado = vehiculoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Permisos.HAS_VEHICULO_ESCRIBIR)
    @Operation(summary = "Actualizar vehículo", description = "Actualiza los datos del vehículo (RN-02)")
    public ResponseEntity<VehiculoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO request) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Permisos.HAS_VEHICULO_ESCRIBIR)
    @Operation(summary = "Eliminar vehículo", description = "Elimina físicamente un vehículo si no tiene órdenes registradas (RN-02)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
