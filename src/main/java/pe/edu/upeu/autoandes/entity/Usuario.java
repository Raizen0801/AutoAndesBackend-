package pe.edu.upeu.autoandes.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nombrecompleto", nullable = false, length = 120)
    private String nombreCompleto;

    @Column(name = "activo", nullable = false)
    private Integer activo = 1;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarioroles",
            joinColumns = @JoinColumn(name = "usuarioid"),
            inverseJoinColumns = @JoinColumn(name = "rolid")
    )
    private Set<Rol> roles = new HashSet<>();
}
