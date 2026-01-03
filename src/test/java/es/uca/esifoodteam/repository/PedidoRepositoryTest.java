package es.uca.esifoodteam.repository;

import es.uca.esifoodteam.TestAuditingConfig;
import es.uca.esifoodteam.pedidos.models.EstadoPedido;
import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.repositories.EstadoPedidoRepository;
import es.uca.esifoodteam.pedidos.repositories.PedidoRepository;
import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.TipoUsuarioRepository;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestAuditingConfig.class)
class PedidoRepositoryTest {

    @Autowired PedidoRepository pedidoRepository;
    @Autowired EstadoPedidoRepository estadoPedidoRepository;

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired TipoUsuarioRepository tipoUsuarioRepository;

    private Usuario crearUsuario(String email) {
        TipoUsuario tipo = tipoUsuarioRepository.saveAndFlush(new TipoUsuario("CLIENTE"));
        Usuario u = new Usuario();
        u.setTipo_id(tipo);
        u.setNombre("Usuario Test");
        u.setEmail(email);
        u.setPass("hash-no-real"); // en tests vale cualquier string (no autenticamos)
        u.setTelefono("");
        u.setDireccion("");
        u.setEsActivo(true);
        return usuarioRepository.saveAndFlush(u);
    }

    @Test
    void save_and_findByUsuarioId() {
        Usuario u = crearUsuario("u1@test.com");
        EstadoPedido creado = estadoPedidoRepository.saveAndFlush(new EstadoPedido("CREADO"));

        Pedido p = new Pedido();
        p.setEstado(creado);
        p.setUsuario(u);
        p.setPrecio(new BigDecimal("15.00"));
        p.setFechaHora(LocalDateTime.now());
        p.setObservaciones("test");

        Pedido saved = pedidoRepository.saveAndFlush(p);
        assertNotNull(saved.getId());

        List<Pedido> byUser = pedidoRepository.findByUsuarioId(u.getId());
        assertEquals(1, byUser.size());
        assertEquals(saved.getId(), byUser.get(0).getId());
    }

    @Test
    void findByEstadoNombre_returnsMatches() {
        Usuario u = crearUsuario("u2@test.com");
        EstadoPedido pagado = estadoPedidoRepository.saveAndFlush(new EstadoPedido("PAGADO"));

        Pedido p = new Pedido();
        p.setEstado(pagado);
        p.setUsuario(u);
        p.setPrecio(new BigDecimal("20.00"));
        p.setFechaHora(LocalDateTime.now());

        pedidoRepository.saveAndFlush(p);

        List<Pedido> found = pedidoRepository.findByEstadoNombre("PAGADO");
        assertFalse(found.isEmpty());
        assertEquals("PAGADO", found.get(0).getEstado().getNombre());
    }

    @Test
    void findByFechaHoraBetween_returnsMatches() {
        Usuario u = crearUsuario("u3@test.com");
        EstadoPedido creado = estadoPedidoRepository.saveAndFlush(new EstadoPedido("CREADO"));

        LocalDateTime t1 = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime t2 = LocalDateTime.of(2025, 1, 2, 10, 0);

        Pedido p = new Pedido();
        p.setEstado(creado);
        p.setUsuario(u);
        p.setPrecio(new BigDecimal("9.99"));
        p.setFechaHora(t1);

        pedidoRepository.saveAndFlush(p);

        List<Pedido> found = pedidoRepository.findByFechaHoraBetween(t1.minusHours(1), t2.plusHours(1));
        assertEquals(1, found.size());
        assertEquals(t1, found.get(0).getFechaHora());
    }
}
