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
    Optional<Usuario> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    boolean existsByDni(String dni);
    
    // Búsquedas por relaciones
    List<Usuario> findByTipo_idId(Long tipoId);
    List<Usuario> findByEstado_idId(Long estadoId);
    
    // Usuarios activos (para listas principales)
    List<Usuario> findByEstado_idNombre(String estadoNombre);
    
    // Búsqueda por nombre (LIKE)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}