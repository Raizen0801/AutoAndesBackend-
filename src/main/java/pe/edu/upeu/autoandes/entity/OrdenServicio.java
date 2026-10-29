package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.upeu.autoandes.enums.*;
import java.math.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "orden_servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 9)
    private String numero;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 12)
    private EstadoOrden estado;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "usuario_registro", nullable = false, length = 30)
    private String usuarioRegistro;

    @Column(name = "motivo_anulacion", length = 200)
    private String motivoAnulacion;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetalleOrden> detalles = new ArrayList<>();

    public void addDetalle(DetalleOrden detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
        detalle.setOrdenServicio(this);
    }

    public void removeDetalle(DetalleOrden detalle) {
        if (this.detalles != null) {
            this.detalles.remove(detalle);
        }
        detalle.setOrdenServicio(null);
    }
}
