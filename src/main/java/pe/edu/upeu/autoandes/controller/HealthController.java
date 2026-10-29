package pe.edu.upeu.autoandes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Verificación de estado de la aplicación")
public class HealthController {

    @GetMapping
    @Operation(summary = "Verificar estado del servicio", description = "Retorna el estado operativo y timestamp del servidor")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "app", "AutoAndes Backend REST API",
                "timestamp", LocalDateTime.now()
        ));
    }
}
