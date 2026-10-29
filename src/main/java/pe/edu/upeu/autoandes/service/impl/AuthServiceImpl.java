package pe.edu.upeu.autoandes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.exception.*;
import pe.edu.upeu.autoandes.mapper.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.security.*;
import pe.edu.upeu.autoandes.service.service.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Intento de autenticación para el usuario: '{}'", request.getUsername());

        Usuario usuario = usuarioRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Fallo de autenticación: usuario '{}' no existe en la BD", request.getUsername());
                    return new BadCredentialsException("Credenciales inválidas o cuenta inactiva");
                });

        if (usuario.getActivo() == null || usuario.getActivo() == 0) {
            log.warn("Fallo de autenticación: el usuario '{}' está desactivado", request.getUsername());
            throw new DisabledException("Credenciales inválidas o cuenta inactiva");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.warn("Fallo de autenticación: contraseña incorrecta para el usuario '{}'", request.getUsername());
            throw new BadCredentialsException("Credenciales inválidas o cuenta inactiva");
        }

        UsuarioPrincipal principal = UsuarioPrincipal.build(usuario);
        String token = jwtService.generarToken(principal);

        log.info("Autenticación exitosa para el usuario '{}' con roles: {}",
                usuario.getUsername(), principal.getRoles());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setTipo("Bearer");
        response.setExpiraEnSegundos(3600L);
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setRoles(principal.getRoles());
        response.setPermisos(principal.getPermisos());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioActual(String username) {
        log.info("Consultando datos de perfil para el usuario autenticado: '{}'", username);

        Usuario usuario = usuarioRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> {
                    log.warn("Usuario autenticado '{}' no encontrado en BD", username);
                    return new RecursoNoEncontradoException("Usuario '" + username + "' no encontrado");
                });

        return usuarioMapper.toDTO(usuario);
    }
}
