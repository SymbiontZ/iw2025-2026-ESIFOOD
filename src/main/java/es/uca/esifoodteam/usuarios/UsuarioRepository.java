package es.uca.esifoodteam.usuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Búsquedas por campos únicos
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndEsActivo(String email, Boolean activo);
    
    Optional<Usuario> findByTelefono(String telefono);
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    
    // Búsquedas por relaciones
    List<Usuario> findByTipo(TipoUsuario tipo);
    List<Usuario> findByEsActivo(Boolean esActivo);
    
    // Búsquedas por relaciones (por nombre)
    List<Usuario> findByTipoNombre(String tipoNombre);

    
    // Búsqueda por nombre (LIKE)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    
}