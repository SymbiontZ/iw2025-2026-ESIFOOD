package es.uca.esifoodteam.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.esifoodteam.establecimientos.EstablecimientoRepository;
import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioService tipoUsuarioService;
    private final EstablecimientoRepository establecimientoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, 
                         TipoUsuarioService tipoUsuarioService,
                         EstablecimientoRepository establecimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioService = tipoUsuarioService;
        this.establecimientoRepository = establecimientoRepository;
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
        if (usuario.getEsActivo() == null) {
            throw new RuntimeException("Estado requerido");
        }

        if (usuario.getEstablecimientoTrabajo() != null && usuario.getEstablecimientoTrabajo().getId() != null) {
            if (!establecimientoRepository.existsById(usuario.getEstablecimientoTrabajo().getId())) {
                throw new RuntimeException("Establecimiento no existe");
            }
        }
        
        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    @Transactional
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // ✅ CORREGIDO: validar email excluyendo el usuario actual
        if (!usuario.getEmail().equals(usuarioDetails.getEmail()) && 
            usuarioRepository.existsByEmailAndIdNot(usuarioDetails.getEmail(), id)) {
            throw new RuntimeException("Email ya existe");
        }
        
        // Validar teléfono igual
        if (!usuario.getTelefono().equals(usuarioDetails.getTelefono()) && 
            usuarioRepository.existsByTelefonoAndIdNot(usuarioDetails.getTelefono(), id)) {
            throw new RuntimeException("Teléfono ya existe");
        }
        
        // ✅ USA getters REALES de tu modelo Usuario
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setDireccion(usuarioDetails.getDireccion());
        usuario.setTipo_id(usuarioDetails.getTipo_id());
        usuario.setEstablecimientoTrabajo(usuarioDetails.getEstablecimientoTrabajo());
        usuario.setEsActivo(usuarioDetails.getEsActivo());

        return usuarioRepository.save(usuario);
    }

    // Eliminar usuario (desactivar)
    @Transactional
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado ID: " + id));
        
        usuario.setEsActivo(false);
        usuarioRepository.save(usuario); 
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