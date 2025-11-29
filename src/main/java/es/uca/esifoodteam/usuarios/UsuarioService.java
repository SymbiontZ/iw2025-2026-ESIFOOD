package es.uca.esifoodteam.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private EstadoUsuarioRepository estadoUsuarioRepository;

    // Listar todos
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Crear usuario (con validaciones)
    public Usuario create(Usuario usuario) {
        // Verificar duplicados
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email ya existe");
        }
        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new RuntimeException("Teléfono ya existe");
        }
        if (usuarioRepository.existsByDni(usuario.getDni())) {
            throw new RuntimeException("DNI ya existe");
        }
        
        // Verificar que tipo y estado existen
        if (usuario.getTipo_id() == null || usuario.getTipo_id().getId() == null) {
            throw new RuntimeException("Tipo de usuario requerido");
        }
        if (usuario.getEstado_id() == null || usuario.getEstado_id().getId() == null) {
            throw new RuntimeException("Estado requerido");
        }
        
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar solo si los datos cambian
        if (!usuario.getEmail().equals(usuarioDetails.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDetails.getEmail())) {
            throw new RuntimeException("Email ya existe");
        }
        
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setDireccion(usuarioDetails.getDireccion());
        usuario.setDni(usuarioDetails.getDni());
        usuario.setTipo_id(usuarioDetails.getTipo_id());
        usuario.setEstado_id(usuarioDetails.getEstado_id());

        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // Métodos útiles para Vaadin Grid
    public List<Usuario> findByTipo(Long tipoId) {
        return usuarioRepository.findByTipo_idId(tipoId);
    }

    public List<Usuario> findActivos() {
        return usuarioRepository.findByEstado_idNombre("ACTIVO");
    }

    public List<Usuario> searchByNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
}