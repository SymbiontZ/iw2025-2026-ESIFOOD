package es.uca.esifoodteam.integration;

import es.uca.esifoodteam.TestAuditingConfig;
import es.uca.esifoodteam.TestData;
import es.uca.esifoodteam.pedidos.models.EstadoPedido;
import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.repositories.EstadoPedidoRepository;
import es.uca.esifoodteam.pedidos.services.PedidoService;
import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.TipoUsuarioRepository;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAuditingConfig.class)
class PedidoFlowIT {

    @Autowired PedidoService pedidoService;
    @Autowired EstadoPedidoRepository estadoPedidoRepository;

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired TipoUsuarioRepository tipoUsuarioRepository;

    @Test
    void createPedido_endToEnd_persists_andSetsFechaHora() {
        TipoUsuario tipo = tipoUsuarioRepository.save(new TipoUsuario("CLIENTE"));
        Usuario u = usuarioRepository.save(TestData.usuarioCliente(tipo, "cliente-it@test.com"));

        EstadoPedido creado = estadoPedidoRepository.save(new EstadoPedido("CREADO"));

        Pedido p = new Pedido();
        p.setEstado(creado);
        p.setUsuario(u);
        p.setPrecio(new BigDecimal("30.00"));
        p.setFechaHora(null);

        Pedido saved = pedidoService.create(p);

        assertNotNull(saved.getId());
        assertNotNull(saved.getFechaHora(), "El servicio debe setear fechaHora si viene null");
        assertEquals("CREADO", saved.getEstado().getNombre());
        assertEquals("Cliente IT", saved.getUsuario().getNombre());
    }
}
