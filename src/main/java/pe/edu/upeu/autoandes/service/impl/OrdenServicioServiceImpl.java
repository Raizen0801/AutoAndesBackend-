package pe.edu.upeu.autoandes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.request.*;
import pe.edu.upeu.autoandes.dto.response.*;
import pe.edu.upeu.autoandes.entity.*;
import pe.edu.upeu.autoandes.enums.*;
import pe.edu.upeu.autoandes.exception.*;
import pe.edu.upeu.autoandes.mapper.*;
import pe.edu.upeu.autoandes.repository.*;
import pe.edu.upeu.autoandes.service.service.*;

import java.math.*;
import java.time.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository ordenServicioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;
    private final OrdenServicioMapper ordenServicioMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrdenServicioResponseDTO registrarOrden(OrdenServicioRequestDTO request, String username) {
        log.info("Iniciando registro atómico de orden de servicio para vehículo ID: {} por usuario: {}",
                request.getVehiculoId(), username);

        // 1. Validar vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> {
                    log.warn("Vehículo con ID {} no encontrado al registrar orden", request.getVehiculoId());
                    return new RecursoNoEncontradoException("Vehículo con ID " + request.getVehiculoId() + " no encontrado");
                });

        // 2. Validar detalles (RN-03)
        if (request.getDetalles() == null || request.getDetalles().isEmpty() || request.getDetalles().size() > 10) {
            log.warn("RN-03 rechazada: número de detalles fuera de rango (1 a 10)");
            throw new IllegalArgumentException("Una orden de servicio debe contener entre 1 y 10 líneas de detalle");
        }

        Set<Long> serviciosSet = new HashSet<>();
        for (DetalleOrdenRequestDTO det : request.getDetalles()) {
            if (det.getCantidad() == null || det.getCantidad() < 1 || det.getCantidad() > 5) {
                log.warn("RN-03 rechazada: cantidad {} fuera de rango (1 a 5)", det.getCantidad());
                throw new IllegalArgumentException("La cantidad por servicio debe ser de 1 a 5 unidades");
            }
            if (!serviciosSet.add(det.getServicioId())) {
                log.warn("RN-03 rechazada: servicio ID {} duplicado en la orden", det.getServicioId());
                throw new IllegalArgumentException("No se pueden repetir servicios en los detalles de la orden");
            }
        }

        // 3. Validar kilometraje respecto a última orden REGISTRADA (RN-04)
        List<OrdenServicio> ultimasOrdenes = ordenServicioRepository.findUltimasOrdenesRegistradas(
                vehiculo.getId(),
                EstadoOrden.REGISTRADA
        );
        if (!ultimasOrdenes.isEmpty()) {
            OrdenServicio ultimaOrden = ultimasOrdenes.get(0);
            if (request.getKilometraje() < ultimaOrden.getKilometraje()) {
                log.warn("RN-04 rechazada: kilometraje {} menor al último registrado {}",
                        request.getKilometraje(), ultimaOrden.getKilometraje());
                throw new ReglaNegocioException("El kilometraje ingresado (" + request.getKilometraje() +
                        " km) no puede ser menor que el de la última orden registrada (" +
                        ultimaOrden.getKilometraje() + " km)");
            }
        }

        // 4. Generar correlativo automático único "OS-000001"
        String siguienteNumero = generarSiguienteNumeroOrden();

        // 5. Construir cabecera
        OrdenServicio orden = new OrdenServicio();
        orden.setNumero(siguienteNumero);
        orden.setFecha(LocalDateTime.now());
        orden.setVehiculo(vehiculo);
        orden.setKilometraje(request.getKilometraje());
        orden.setEstado(EstadoOrden.REGISTRADA);
        orden.setUsuarioRegistro(username != null ? username : "sistema");

        BigDecimal total = BigDecimal.ZERO;

        // 6. Procesar detalles con precios de BD y verificar estado activo (RN-03 y RN-04)
        for (DetalleOrdenRequestDTO detalleDto : request.getDetalles()) {
            Servicio servicio = servicioRepository.findById(detalleDto.getServicioId())
                    .orElseThrow(() -> {
                        log.warn("Servicio con ID {} no encontrado al crear detalle", detalleDto.getServicioId());
                        return new RecursoNoEncontradoException("Servicio con ID " + detalleDto.getServicioId() + " no encontrado");
                    });

            if (servicio.getActivo() == null || servicio.getActivo() == 0) {
                log.warn("RN-03 rechazada: servicio '{}' ({}) inactivo", servicio.getNombre(), servicio.getCodigo());
                throw new ReglaNegocioException("El servicio '" + servicio.getNombre() + "' (" +
                        servicio.getCodigo() + ") se encuentra inactivo y no puede ser incluido en una orden");
            }

            BigDecimal precioUnitario = servicio.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDto.getCantidad()));
            total = total.add(subtotal);

            DetalleOrden detalle = new DetalleOrden();
            detalle.setServicio(servicio);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            orden.addDetalle(detalle);
        }

        orden.setTotal(total);

        // Guardar cabecera y detalles en la misma transacción atómica
        OrdenServicio ordenGuardada = ordenServicioRepository.save(orden);
        log.info("Orden de servicio registrada exitosamente con número: {} y total: {}",
                ordenGuardada.getNumero(), ordenGuardada.getTotal());

        return ordenServicioMapper.toDTO(ordenGuardada);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrdenServicioResponseDTO anularOrden(Long id, AnularOrdenRequestDTO request) {
        log.info("Iniciando anulación de orden de servicio ID: {}", id);

        OrdenServicio orden = ordenServicioRepository.findByIdWithDetails(id)
                .orElseThrow(() -> {
                    log.warn("Orden con ID {} no encontrada para anular", id);
                    return new RecursoNoEncontradoException("Orden de servicio con ID " + id + " no encontrada");
                });

        // RN-05: Solo se anula una orden REGISTRADA
        if (orden.getEstado() != EstadoOrden.REGISTRADA) {
            log.warn("RN-05 rechazada: la orden {} ya está en estado {}", orden.getNumero(), orden.getEstado());
            throw new ReglaNegocioException("Solo se pueden anular órdenes en estado REGISTRADA");
        }

        // RN-05: Y en el mismo día de su registro
        LocalDate fechaOrden = orden.getFecha().toLocalDate();
        LocalDate hoy = LocalDate.now();
        if (!fechaOrden.isEqual(hoy)) {
            log.warn("RN-05 rechazada: la orden {} fue registrada en fecha {} (hoy es {})",
                    orden.getNumero(), fechaOrden, hoy);
            throw new ReglaNegocioException("Solo se pueden anular órdenes registradas en el mismo día");
        }

        orden.setEstado(EstadoOrden.ANULADA);
        orden.setMotivoAnulacion(request.getMotivo());

        OrdenServicio ordenActualizada = ordenServicioRepository.save(orden);
        log.info("Orden {} anulada exitosamente con motivo: {}", orden.getNumero(), request.getMotivo());

        return ordenServicioMapper.toDTO(ordenActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenServicioResponseDTO obtenerPorId(Long id) {
        log.info("Consultando orden de servicio con ID: {}", id);
        OrdenServicio orden = ordenServicioRepository.findByIdWithDetails(id)
                .orElseThrow(() -> {
                    log.warn("Orden con ID {} no encontrada", id);
                    return new RecursoNoEncontradoException("Orden de servicio con ID " + id + " no encontrada");
                });
        return ordenServicioMapper.toDTO(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioResponseDTO> listarConFiltros(String placa, EstadoOrden estado, LocalDate desde, LocalDate hasta) {
        log.info("Listando órdenes con filtros -> placa: {}, estado: {}, desde: {}, hasta: {}",
                placa, estado, desde, hasta);

        LocalDateTime desdeDT = desde != null ? desde.atStartOfDay() : null;
        LocalDateTime hastaDT = hasta != null ? hasta.atTime(LocalTime.MAX) : null;

        List<OrdenServicio> ordenes = ordenServicioRepository.buscarConFiltros(
                (placa != null && !placa.isBlank()) ? placa.trim() : null,
                estado,
                desdeDT,
                hastaDT
        );

        return ordenes.stream()
                .map(ordenServicioMapper::toDTO)
                .toList();
    }

    private synchronized String generarSiguienteNumeroOrden() {
        String maxNumero = ordenServicioRepository.findMaxNumero();
        int siguienteCorrelativo = 1;
        if (maxNumero != null && maxNumero.startsWith("OS-")) {
            try {
                String numeroStr = maxNumero.substring(3);
                siguienteCorrelativo = Integer.parseInt(numeroStr) + 1;
            } catch (NumberFormatException e) {
                siguienteCorrelativo = (int) ordenServicioRepository.count() + 1;
            }
        }
        return String.format("OS-%06d", siguienteCorrelativo);
    }
}
