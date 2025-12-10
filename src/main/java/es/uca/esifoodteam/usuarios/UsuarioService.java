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
        
        // Verificar que tipo y estado existen
        if (usuario.getTipo_id() == null || usuario.getTipo_id().getId() == null) {
            throw new RuntimeException("Tipo de usuario requerido");
        }
        if (usuario.getEsActivo() == null || usuario.getEsActivo() == null) {
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
        usuario.setTipo_id(usuarioDetails.getTipo_id());
        usuario.setEsActivo(usuarioDetails.getEsActivo());

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
    public List<Usuario> findByTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoId(tipoUsuario.getId());
    }


    public List<Usuario> searchByNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
}