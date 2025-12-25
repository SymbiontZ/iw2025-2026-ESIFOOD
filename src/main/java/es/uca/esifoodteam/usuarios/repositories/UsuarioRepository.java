package es.uca.esifoodteam.usuarios.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Búsquedas por campos únicos
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndEsActivo(String email, Boolean activo);
    
    Optional<Usuario> findByTelefono(String telefono);
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    boolean existsByTelefonoAndIdNot(String telefono, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    
    // Búsquedas por relaciones
    List<Usuario> findByTipo(TipoUsuario tipo);
    List<Usuario> findByEsActivo(Boolean esActivo);
    
    // Búsquedas por relaciones (por nombre)
    List<Usuario> findByTipoNombre(String tipoNombre);
    
    // Búsqueda por nombre (LIKE)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    @Modifying
    @Query(value =  "UPDATE usuario " +
                    "SET nombre = CONCAT('ANÓNIMO_', ?1), " +
                    "email = CONCAT('anonimo_', ?1, '@eliminado.esifood'), " +
                    "telefono = '000000000', " +
                    "direccion = 'DIRECCIÓN_ANONIMIZADA', " +
                    "es_activo = false, " +
                    "modified_by = 'ANÓNIMO', " +
                    "modified_date = CURRENT_TIMESTAMP " +
                    "WHERE id = ?1", nativeQuery = true)
    void anonimizarUsuarioRGPD(Long id);
}
