package es.uca.esifoodteam.integration;

import es.uca.esifoodteam.TestAuditingConfig;
import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.TipoUsuarioRepository;
import es.uca.esifoodteam.usuarios.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAuditingConfig.class)
class UsuarioFlowIT {

    @Autowired UsuarioService usuarioService;
    @Autowired TipoUsuarioRepository tipoUsuarioRepository;

    @Test
    void createUsuario_endToEnd_persists_andSetsAuditing() {
        String tag = String.valueOf(System.nanoTime());

        TipoUsuario tipo = tipoUsuarioRepository.save(new TipoUsuario("CLIENTE_" + tag));

        Usuario u = new Usuario();
        u.setTipo_id(tipo);
        u.setNombre("Cliente IT " + tag);
        u.setEmail("cliente-it-" + tag + "@test.com");
        u.setPass("hashhash"); // >= 8
        u.setTelefono("6" + tag.substring(0, Math.min(8, tag.length()))); // algo único
        u.setDireccion("Calle IT");
        u.setEsActivo(true);

        Usuario saved = usuarioService.create(u);

        assertNotNull(saved.getId());
        assertEquals("Cliente IT " + tag, saved.getNombre());

        assertNotNull(saved.getCreatedDate(), "createdDate debe setearse por auditing");
        assertEquals("test-user", saved.getCreatedBy(), "createdBy debe venir del auditorAware de test");
    }


    @Test
    void createUsuario_throws_onDuplicateEmail() {
        TipoUsuario tipo = tipoUsuarioRepository.save(new TipoUsuario("CLIENTE"));

        Usuario u1 = new Usuario();
        u1.setTipo_id(tipo);
        u1.setNombre("A");
        u1.setEmail("dup@test.com");
        u1.setPass("hashhash");
        u1.setTelefono("600000010");
        u1.setDireccion("Dir");
        u1.setEsActivo(true);
        usuarioService.create(u1);

        Usuario u2 = new Usuario();
        u2.setTipo_id(tipo);
        u2.setNombre("B");
        u2.setEmail("dup@test.com"); // duplicado
        u2.setPass("hashhash");
        u2.setTelefono("600000011");
        u2.setDireccion("Dir");
        u2.setEsActivo(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.create(u2));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void createUsuario_throws_onDuplicateTelefono() {
        TipoUsuario tipo = tipoUsuarioRepository.save(new TipoUsuario("CLIENTE"));

        Usuario u1 = new Usuario();
        u1.setTipo_id(tipo);
        u1.setNombre("A");
        u1.setEmail("tel1@test.com");
        u1.setPass("hashhash");
        u1.setTelefono("600000020");
        u1.setDireccion("Dir");
        u1.setEsActivo(true);
        usuarioService.create(u1);

        Usuario u2 = new Usuario();
        u2.setTipo_id(tipo);
        u2.setNombre("B");
        u2.setEmail("tel2@test.com");
        u2.setPass("hashhash");
        u2.setTelefono("600000020"); // duplicado
        u2.setDireccion("Dir");
        u2.setEsActivo(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> usuarioService.create(u2));
        assertTrue(ex.getMessage().toLowerCase().contains("tel"));
    }
}
