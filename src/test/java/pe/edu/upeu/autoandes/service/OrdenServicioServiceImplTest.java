package pe.edu.upeu.autoandes.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.enums.*;
import pe.edu.upeu.autoandes.exception.*;
import pe.edu.upeu.autoandes.mapper.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.service.impl.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServicioServiceImplTest {

    @Mock
    private OrdenServicioRepository ordenServicioRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private OrdenServicioMapper ordenServicioMapper;

    @InjectMocks
    private OrdenServicioServiceImpl ordenServicioService;

    @Test
    void testRN03_ServicioDuplicadoEnDetalles_LanzaIllegalArgumentException() {
        Vehiculo vehiculo = new Vehiculo(1L, "AAA-111", "Toyota", "Corolla", 2020, new Cliente());
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        List<DetalleOrdenRequestDTO> detalles = List.of(
                new DetalleOrdenRequestDTO(1L, 1),
                new DetalleOrdenRequestDTO(1L, 2)
        );
        OrdenServicioRequestDTO request = new OrdenServicioRequestDTO(1L, 50000, detalles);

        assertThrows(IllegalArgumentException.class, () -> ordenServicioService.registrarOrden(request, "asesor"));
    }

    @Test
    void testRN03_ServicioInactivo_LanzaReglaNegocioException() {
        Vehiculo vehiculo = new Vehiculo(1L, "AAA-111", "Toyota", "Corolla", 2020, new Cliente());
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(ordenServicioRepository.findUltimasOrdenesRegistradas(1L, EstadoOrden.REGISTRADA)).thenReturn(Collections.emptyList());

        Servicio servicioInactivo = new Servicio(8L, "SRV-008", "Lavado integral", BigDecimal.valueOf(95), 60, 0);
        when(servicioRepository.findById(8L)).thenReturn(Optional.of(servicioInactivo));

        List<DetalleOrdenRequestDTO> detalles = List.of(new DetalleOrdenRequestDTO(8L, 1));
        OrdenServicioRequestDTO request = new OrdenServicioRequestDTO(1L, 50000, detalles);

        assertThrows(ReglaNegocioException.class, () -> ordenServicioService.registrarOrden(request, "asesor"));
    }

    @Test
    void testRN04_KilometrajeMenorQueUltimaOrden_LanzaReglaNegocioException() {
        Vehiculo vehiculo = new Vehiculo(1L, "AAA-111", "Toyota", "Corolla", 2020, new Cliente());
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        OrdenServicio ordenPrevia = new OrdenServicio();
        ordenPrevia.setKilometraje(45000);
        when(ordenServicioRepository.findUltimasOrdenesRegistradas(1L, EstadoOrden.REGISTRADA))
                .thenReturn(List.of(ordenPrevia));

        List<DetalleOrdenRequestDTO> detalles = List.of(new DetalleOrdenRequestDTO(1L, 1));
        OrdenServicioRequestDTO request = new OrdenServicioRequestDTO(1L, 40000, detalles);

        assertThrows(ReglaNegocioException.class, () -> ordenServicioService.registrarOrden(request, "asesor"));
    }

    @Test
    void testRN05_AnularOrdenDeFechaAnterior_LanzaReglaNegocioException() {
        OrdenServicio orden = new OrdenServicio();
        orden.setId(1L);
        orden.setNumero("OS-000001");
        orden.setEstado(EstadoOrden.REGISTRADA);
        orden.setFecha(LocalDateTime.now().minusDays(2));

        when(ordenServicioRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(orden));

        AnularOrdenRequestDTO request = new AnularOrdenRequestDTO("Error en registro por parte del cliente");

        assertThrows(ReglaNegocioException.class, () -> ordenServicioService.anularOrden(1L, request));
    }
}
