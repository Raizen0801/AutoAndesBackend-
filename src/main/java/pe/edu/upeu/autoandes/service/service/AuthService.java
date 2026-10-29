package pe.edu.upeu.autoandes.service.service;

import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
    UsuarioResponseDTO obtenerUsuarioActual(String username);
}
