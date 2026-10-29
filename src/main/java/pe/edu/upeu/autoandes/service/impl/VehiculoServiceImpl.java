package pe.edu.upeu.autoandes.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.autoandes.dto.request.VehiculoRequestDTO;
import pe.edu.upeu.autoandes.dto.response.VehiculoResponseDTO;
import pe.edu.upeu.autoandes.entity.Cliente;
import pe.edu.upeu.autoandes.entity.Vehiculo;
import pe.edu.upeu.autoandes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.autoandes.exception.ReglaNegocioException;
import pe.edu.upeu.autoandes.mapper.VehiculoMapper;
import pe.edu.upeu.autoandes.repository.ClienteRepository;
import pe.edu.upeu.autoandes.repository.OrdenServicioRepository;
import pe.edu.upeu.autoandes.repository.VehiculoRepository;
import pe.edu.upeu.autoandes.service.service.VehiculoService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final VehiculoMapper vehiculoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listar(Long clienteId) {
        log.info("Consultando vehiculos con filtro clienteId: {}", clienteId);
        List<Vehiculo> vehiculos;
        if (clienteId != null) {
            vehiculos = vehiculoRepository.findByClienteId(clienteId);
        } else {
            vehiculos = vehiculoRepository.findAllWithCliente();
        }
        return vehiculos.stream()
                .map(vehiculoMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerPorId(Long id) {
        log.info("Consultando vehiculo con ID: {}", id);
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Vehículo con ID {} no encontrado", id);
                    return new RecursoNoEncontradoException("Vehículo con ID " + id + " no encontrado");
                });
        return vehiculoMapper.toDTO(vehiculo);
    }

    @Override
    @Transactional
    public VehiculoResponseDTO crear(VehiculoRequestDTO request) {
        log.info("Registrando nuevo vehículo con placa: {}", request.getPlaca());

        if (vehiculoRepository.existsByPlaca(request.getPlaca())) {
            log.warn("RN-02 rechazada: ya existe un vehículo con la placa {}", request.getPlaca());
            throw new ReglaNegocioException("Ya existe un vehículo registrado con la placa " + request.getPlaca());
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> {
                    log.warn("RN-02 rechazada: cliente con ID {} no existe", request.getClienteId());
                    return new RecursoNoEncontradoException("Cliente con ID " + request.getClienteId() + " no encontrado");
                });

        Vehiculo vehiculo = vehiculoMapper.toEntity(request, cliente);
        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo registrado exitosamente con ID: {}", guardado.getId());
        return vehiculoMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO request) {
        log.info("Actualizando vehículo con ID: {}", id);

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Vehículo con ID {} no encontrado para actualizar", id);
                    return new RecursoNoEncontradoException("Vehículo con ID " + id + " no encontrado");
                });

        if (vehiculoRepository.existsByPlacaAndIdNot(request.getPlaca(), id)) {
            log.warn("RN-02 rechazada: la placa {} ya está en uso por otro vehículo", request.getPlaca());
            throw new ReglaNegocioException("Ya existe otro vehículo con la placa " + request.getPlaca());
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> {
                    log.warn("RN-02 rechazada: cliente con ID {} no existe", request.getClienteId());
                    return new RecursoNoEncontradoException("Cliente con ID " + request.getClienteId() + " no encontrado");
                });

        vehiculoMapper.updateEntityFromDTO(request, vehiculo, cliente);
        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        log.info("Vehículo con ID {} actualizado exitosamente", actualizado.getId());
        return vehiculoMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Intentando eliminar vehículo con ID: {}", id);

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Vehículo con ID {} no encontrado para eliminar", id);
                    return new RecursoNoEncontradoException("Vehículo con ID " + id + " no encontrado");
                });

        if (ordenServicioRepository.existsByVehiculoId(id)) {
            log.warn("RN-02 rechazada: el vehículo con ID {} tiene órdenes registradas y no puede eliminarse", id);
            throw new ReglaNegocioException("No se puede eliminar el vehículo porque tiene órdenes de servicio asociadas");
        }

        vehiculoRepository.delete(vehiculo);
        log.info("Vehículo con ID {} eliminado exitosamente", id);
    }
}
