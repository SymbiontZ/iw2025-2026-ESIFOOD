package es.uca.esifoodteam.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
    Optional<TipoUsuario> findByNombre(String nombre);
}
