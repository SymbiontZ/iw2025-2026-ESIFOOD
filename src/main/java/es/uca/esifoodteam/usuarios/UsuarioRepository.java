package es.uca.esifoodteam.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Búsquedas por campos únicos
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTelefono(String telefono);
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    
    // Búsquedas por relaciones (por ID)
    List<Usuario> findByTipoId(Long tipoId);
    
    // Búsquedas por relaciones (por nombre)
    List<Usuario> findByTipoNombre(String tipoNombre);

    
    // Búsqueda por nombre (LIKE)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}