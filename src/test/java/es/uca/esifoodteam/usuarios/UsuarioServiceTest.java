package es.uca.esifoodteam.usuarios;

import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;
import es.uca.esifoodteam.usuarios.services.TipoUsuarioService;
import es.uca.esifoodteam.usuarios.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    TipoUsuarioService tipoUsuarioService;

    @InjectMocks
    UsuarioService usuarioService;

    // ========== CREATE ==========

    @Test
    void create_throwsWhenEmailExists() {
        Usuario u = usuarioValido("a@a.com", "111");

        when(usuarioRepository.existsByEmail("a@a.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.create(u));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenTelefonoExists() {
        Usuario u = usuarioValido("a@a.com", "111");

        when(usuarioRepository.existsByEmail("a@a.com")).thenReturn(false);
        when(usuarioRepository.existsByTelefono("111")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.create(u));
        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("teléfono") || msg.contains("telefono"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenTipoMissing() {
        Usuario u = usuarioValido("a@a.com", "111");
        u.setTipo_id(null); // requerido

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByTelefono(anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.create(u));
        assertTrue(ex.getMessage().toLowerCase().contains("tipo"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenEstadoMissing() {
        Usuario u = usuarioValido("a@a.com", "111");
        u.setEsActivo(null); // requerido

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByTelefono(anyString())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.create(u));
        assertTrue(ex.getMessage().toLowerCase().contains("estado"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void create_savesWhenValid() {
        Usuario u = usuarioValido("a@a.com", "111");

        when(usuarioRepository.existsByEmail("a@a.com")).thenReturn(false);
        when(usuarioRepository.existsByTelefono("111")).thenReturn(false);
        when(usuarioRepository.save(u)).thenReturn(u);

        Usuario saved = usuarioService.create(u);

        assertSame(u, saved);
        verify(usuarioRepository).save(u);
    }

    // ========== UPDATE ==========

    @Test
    void update_throwsWhenUserNotFound() {
        Long id = 99L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.update(id, new Usuario()));
        assertTrue(ex.getMessage().toLowerCase().contains("no encontrado"));
    }

    @Test
    void update_throwsWhenEmailAlreadyUsedByOtherUser() {
        Long id = 1L;

        Usuario existente = usuarioValido("old@a.com", "111");
        Usuario cambios = usuarioValido("new@a.com", "111");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmailAndIdNot("new@a.com", id)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.update(id, cambios));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void update_throwsWhenTelefonoAlreadyUsedByOtherUser() {
        Long id = 1L;

        Usuario existente = usuarioValido("a@a.com", "111");
        Usuario cambios = usuarioValido("a@a.com", "222");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByTelefonoAndIdNot("222", id)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.update(id, cambios));
        String msg = ex.getMessage().toLowerCase();
        assertTrue(msg.contains("teléfono") || msg.contains("telefono"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void update_updatesFields_setsModifiedByUSUARIO_andSaves() {
        Long id = 1L;

        Usuario existente = usuarioValido("old@a.com", "111");

        Usuario cambios = usuarioValido("new@a.com", "222");
        cambios.setNombre("Nuevo Nombre");
        cambios.setDireccion("Calle 1");
        cambios.setEsActivo(true);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByEmailAndIdNot("new@a.com", id)).thenReturn(false);
        when(usuarioRepository.existsByTelefonoAndIdNot("222", id)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario actualizado = usuarioService.update(id, cambios);

        assertEquals("Nuevo Nombre", actualizado.getNombre());
        assertEquals("new@a.com", actualizado.getEmail());
        assertEquals("222", actualizado.getTelefono());
        assertEquals("Calle 1", actualizado.getDireccion());
        assertEquals(true, actualizado.getEsActivo());
        assertEquals("USUARIO", actualizado.getModifiedBy()); // RGPD en tu servicio

        verify(usuarioRepository).save(existente);
    }

    // ========== DELETE (soft) ==========

    @Test
    void delete_setsEsActivoFalse_andSaves() {
        Long id = 7L;
        Usuario existente = usuarioValido("a@a.com", "111");
        existente.setEsActivo(true);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        usuarioService.delete(id);

        assertFalse(existente.getEsActivo());
        verify(usuarioRepository).save(existente);
    }

    // ========== EXTRA ==========

    @Test
    void findByEmail_delegatesToRepository() {
        when(usuarioRepository.findByEmail("a@a.com")).thenReturn(Optional.of(new Usuario()));

        usuarioService.findByEmail("a@a.com");

        verify(usuarioRepository).findByEmail("a@a.com");
    }

    @Test
    void suprimirDatosPersonales_callsRepositoryAnonimizar() {
        Long id = 3L;

        usuarioService.suprimirDatosPersonales(id);

        verify(usuarioRepository).anonimizarUsuarioRGPD(id);
    }

    // ========== helper ==========

    private Usuario usuarioValido(String email, String telefono) {
        Usuario u = new Usuario();
        u.setNombre("Test");
        u.setEmail(email);
        u.setPass("password123"); // cumple @Size(min=8)
        u.setTelefono(telefono);
        u.setDireccion("Dir");
        u.setEsActivo(true);

        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(1L);
        tipo.setNombre("CLIENTE");
        u.setTipo_id(tipo);

        return u;
    }
}
