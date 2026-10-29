package pe.edu.upeu.autoandes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.autoandes.entity.*;

import java.util.*;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Usuario> findByUsernameWithRoles(@Param("username") String username);

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
