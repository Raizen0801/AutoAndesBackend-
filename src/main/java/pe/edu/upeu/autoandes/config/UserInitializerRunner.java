package pe.edu.upeu.autoandes.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.repository.*;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInitializerRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Verificando consistencia de contraseñas BCrypt para usuarios semilla...");

        // Lista de usuarios y sus contraseñas por defecto
        Map<String, String> credenciales = Map.of(
                "admin", "Auto2026*",
                "asesor", "Auto2026*",
                "jefe", "Auto2026*",
                "inactivo", "Auto2026*"
        );

        for (Map.Entry<String, String> entry : credenciales.entrySet()) {
            usuarioRepository.findByUsername(entry.getKey()).ifPresent(usuario -> {
                String rawPassword = entry.getValue();
                // Si la contrasena no esta encriptada con BCrypt o no coincide, se re-encripta
                if (usuario.getPassword() == null || !usuario.getPassword().startsWith("$2a$") ||
                        !passwordEncoder.matches(rawPassword, usuario.getPassword())) {
                    usuario.setPassword(passwordEncoder.encode(rawPassword));
                    usuarioRepository.save(usuario);
                    log.info("Contraseña actualizada con BCrypt para el usuario: '{}'", usuario.getUsername());
                }
            });
        }
        log.info("Usuarios de prueba verificados correctamente.");
    }
}
