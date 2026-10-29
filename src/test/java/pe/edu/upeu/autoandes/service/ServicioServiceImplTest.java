package pe.edu.upeu.autoandes.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.exception.*;
import pe.edu.upeu.autoandes.mapper.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.service.impl.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioServiceImplTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private DetalleOrdenRepository detalleOrdenRepository;

    @Mock
    private ServicioMapper servicioMapper;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    @Test
    void testRN01_CodigoDuplicado_LanzaReglaNegocioException() {
        ServicioRequestDTO request = new ServicioRequestDTO("SRV-001", "Cambio de aceite", BigDecimal.valueOf(120), 45, 1);
        when(servicioRepository.existsByCodigo("SRV-001")).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> servicioService.crear(request));
    }

    @Test
    void testRN01_EliminarServicioConOrdenes_LanzaReglaNegocioException() {
        Servicio servicio = new Servicio(1L, "SRV-001", "Cambio de aceite", BigDecimal.valueOf(120), 45, 1);
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(detalleOrdenRepository.existsByServicioId(1L)).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> servicioService.eliminar(1L));
    }
}
