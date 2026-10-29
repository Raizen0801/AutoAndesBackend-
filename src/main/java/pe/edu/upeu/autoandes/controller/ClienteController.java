package pe.edu.upeu.autoandes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.autoandes.dto.response.ClienteResponseDTO;
import pe.edu.upeu.autoandes.security.Permisos;
import pe.edu.upeu.autoandes.service.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para la gestión y consulta de clientes")
@SecurityRequirement(name = "Bearer Authentication")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @PreAuthorize(Permisos.HAS_VEHICULO_LEER)
    @Operation(summary = "Listar clientes", description = "Devuelve la lista de clientes registrados en el sistema")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize(Permisos.HAS_VEHICULO_LEER)
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve los detalles de un cliente específico")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }
}
