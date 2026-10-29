package pe.edu.upeu.autoandes.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.entity.Usuario;
import pe.edu.upeu.autoandes.repository.UsuarioRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> {
                    log.warn("Fallo de autenticación: usuario '{}' no existe", username);
                    return new UsernameNotFoundException("Credenciales inválidas o cuenta inactiva");
                });

        if (usuario.getActivo() == null || usuario.getActivo() == 0) {
            log.warn("Fallo de autenticación: la cuenta '{}' está inactiva", username);
            throw new DisabledException("Credenciales inválidas o cuenta inactiva");
        }

        return UsuarioPrincipal.build(usuario);
    }
}
