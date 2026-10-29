package pe.edu.upeu.autoandes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.service.service.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints de inicio de sesión y consulta del usuario autenticado")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica con username y password, devuelve token JWT con roles y permisos")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Obtener usuario autenticado", description = "Devuelve los datos, roles y permisos del usuario del token JWT")
    public ResponseEntity<UsuarioResponseDTO> me(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(authService.obtenerUsuarioActual(username));
    }
}
