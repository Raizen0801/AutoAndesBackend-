package pe.edu.upeu.autoandes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.autoandes.dto.response.IngresoServicioReporteDTO;
import pe.edu.upeu.autoandes.entity.OrdenServicio;
import pe.edu.upeu.autoandes.enums.EstadoOrden;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    boolean existsByVehiculoId(Long vehiculoId);

    Optional<OrdenServicio> findByNumero(String numero);

    @Query("SELECT o FROM OrdenServicio o WHERE o.vehiculo.id = :vehiculoId AND o.estado = :estado ORDER BY o.fecha DESC, o.id DESC")
    List<OrdenServicio> findUltimasOrdenesRegistradas(
            @Param("vehiculoId") Long vehiculoId,
            @Param("estado") EstadoOrden estado);

    @Query("SELECT MAX(o.numero) FROM OrdenServicio o")
    String findMaxNumero();

    @Query("SELECT DISTINCT o FROM OrdenServicio o " +
            "JOIN FETCH o.vehiculo v " +
            "JOIN FETCH v.cliente c " +
            "LEFT JOIN FETCH o.detalles d " +
            "LEFT JOIN FETCH d.servicio s " +
            "WHERE (:placa IS NULL OR UPPER(v.placa) = UPPER(:placa)) " +
            "AND (:estado IS NULL OR o.estado = :estado) " +
            "AND (CAST(:desde AS timestamp) IS NULL OR o.fecha >= :desde) " +
            "AND (CAST(:hasta AS timestamp) IS NULL OR o.fecha <= :hasta) " +
            "ORDER BY o.fecha DESC")
    List<OrdenServicio> buscarConFiltros(
            @Param("placa") String placa,
            @Param("estado") EstadoOrden estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    @Query("SELECT o FROM OrdenServicio o " +
            "JOIN FETCH o.vehiculo v " +
            "JOIN FETCH v.cliente c " +
            "LEFT JOIN FETCH o.detalles d " +
            "LEFT JOIN FETCH d.servicio s " +
            "WHERE o.id = :id")
    Optional<OrdenServicio> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT new pe.edu.upeu.autoandes.dto.response.IngresoServicioReporteDTO(" +
            "s.codigo, " +
            "s.nombre, " +
            "SUM(CAST(d.cantidad AS long)), " +
            "SUM(d.subtotal)) " +
            "FROM DetalleOrden d " +
            "JOIN d.ordenServicio o " +
            "JOIN d.servicio s " +
            "WHERE o.estado != pe.edu.upeu.autoandes.enums.EstadoOrden.ANULADA " +
            "AND (CAST(:desde AS timestamp) IS NULL OR o.fecha >= :desde) " +
            "AND (CAST(:hasta AS timestamp) IS NULL OR o.fecha <= :hasta) " +
            "GROUP BY s.codigo, s.nombre " +
            "ORDER BY SUM(d.subtotal) DESC")
    List<IngresoServicioReporteDTO> obtenerReporteIngresosPorServicio(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}
