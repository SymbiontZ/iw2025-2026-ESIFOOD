package es.uca.esifoodteam.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;

@Service

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioService tipoUsuarioService;

    public UsuarioService(UsuarioRepository usuarioRepository, TipoUsuarioService tipoUsuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioService = tipoUsuarioService;
    }

    // Listar todos
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // Buscar por ID
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    // Crear usuario (con validaciones)
    @Transactional
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
    @Transactional
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
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.findById(id).ifPresent(usuario -> usuario.setEsActivo(false));
    }

    // Métodos útiles para Vaadin Grid
    public List<Usuario> findByTipo(TipoUsuario tipo) {
        return usuarioRepository.findByTipo(tipo);
    }

    public List<Usuario> findActivos() {
        return usuarioRepository.findByEsActivo(true);
    }

    public List<Usuario> searchByNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }
}