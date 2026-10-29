package pe.edu.upeu.autoandes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.request.CambiarEstadoServicioDTO;
import pe.edu.upeu.autoandes.dto.request.ServicioRequestDTO;
import pe.edu.upeu.autoandes.dto.response.ServicioResponseDTO;
import pe.edu.upeu.autoandes.entity.Servicio;
import pe.edu.upeu.autoandes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.autoandes.exception.ReglaNegocioException;
import pe.edu.upeu.autoandes.mapper.ServicioMapper;
import pe.edu.upeu.autoandes.repository.DetalleOrdenRepository;
import pe.edu.upeu.autoandes.repository.ServicioRepository;
import pe.edu.upeu.autoandes.service.service.ServicioService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;
    private final ServicioMapper servicioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> listarTodos() {
        log.info("Listando todos los servicios");
        return servicioRepository.findAll()
                .stream()
                .map(servicioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponseDTO obtenerPorId(Long id) {
        log.info("Consultando servicio con ID: {}", id);
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Servicio con ID {} no encontrado", id);
                    return new RecursoNoEncontradoException("Servicio con ID " + id + " no encontrado");
                });
        return servicioMapper.toDTO(servicio);
    }

    @Override
    @Transactional
    public ServicioResponseDTO crear(ServicioRequestDTO request) {
        log.info("Registrando nuevo servicio con codigo: {}", request.getCodigo());

        if (servicioRepository.existsByCodigo(request.getCodigo())) {
            log.warn("RN-01 rechazada: ya existe un servicio con el código {}", request.getCodigo());
            throw new ReglaNegocioException("Ya existe un servicio con el código " + request.getCodigo());
        }

        Servicio servicio = servicioMapper.toEntity(request);
        Servicio guardado = servicioRepository.save(servicio);
        log.info("Servicio registrado exitosamente con ID: {}", guardado.getId());
        return servicioMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public ServicioResponseDTO actualizar(Long id, ServicioRequestDTO request) {
        log.info("Actualizando servicio con ID: {}", id);

        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Servicio con ID {} no encontrado para actualizar", id);
                    return new RecursoNoEncontradoException("Servicio con ID " + id + " no encontrado");
                });

        if (servicioRepository.existsByCodigoAndIdNot(request.getCodigo(), id)) {
            log.warn("RN-01 rechazada: el código {} ya está en uso por otro servicio", request.getCodigo());
            throw new ReglaNegocioException("Ya existe otro servicio con el código " + request.getCodigo());
        }

        servicioMapper.updateEntityFromDTO(request, servicio);
        Servicio actualizado = servicioRepository.save(servicio);
        log.info("Servicio con ID {} actualizado exitosamente", actualizado.getId());
        return servicioMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Intentando eliminar servicio con ID: {}", id);

        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Servicio con ID {} no encontrado para eliminar", id);
                    return new RecursoNoEncontradoException("Servicio con ID " + id + " no encontrado");
                });

        if (detalleOrdenRepository.existsByServicioId(id)) {
            log.warn("RN-01 rechazada: no se puede eliminar el servicio ID {} porque tiene órdenes asociadas", id);
            throw new ReglaNegocioException("No se puede eliminar físicamente el servicio porque tiene órdenes asociadas. Use la opción de dar de baja.");
        }

        servicioRepository.delete(servicio);
        log.info("Servicio con ID {} eliminado exitosamente", id);
    }

    @Override
    @Transactional
    public ServicioResponseDTO cambiarEstado(Long id, CambiarEstadoServicioDTO request) {
        log.info("Cambiando estado para servicio con ID: {}", id);

        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Servicio con ID {} no encontrado para cambiar estado", id);
                    return new RecursoNoEncontradoException("Servicio con ID " + id + " no encontrado");
                });

        if (request != null && request.getActivo() != null) {
            servicio.setActivo(request.getActivo());
        } else {
            servicio.setActivo(servicio.getActivo() == 1 ? 0 : 1);
        }

        Servicio guardado = servicioRepository.save(servicio);
        log.info("Estado del servicio ID {} cambiado a: {}", id, guardado.getActivo());
        return servicioMapper.toDTO(guardado);
    }
}
