package es.uca.esifoodteam.pedidos;

import es.uca.esifoodteam.pedidos.models.EstadoPedido;
import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.repositories.PedidoRepository;
import es.uca.esifoodteam.pedidos.services.PedidoService;
import es.uca.esifoodteam.usuarios.models.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PedidoServiceTest {

    @Mock PedidoRepository pedidoRepository;

    @InjectMocks PedidoService pedidoService;

    @Test
    void create_setsFechaHoraWhenNull_andSaves() {
        Pedido pedido = new Pedido();
        pedido.setEstado(new EstadoPedido("CREADO"));
        pedido.setUsuario(mock(Usuario.class));
        pedido.setPrecio(new BigDecimal("10.00"));
        pedido.setFechaHora(null);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido saved = pedidoService.create(pedido);

        assertNotNull(saved.getFechaHora(), "Debe asignar fechaHora si viene null");

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        assertNotNull(captor.getValue().getFechaHora(), "Debe guardar el pedido con fechaHora ya asignada");
    }

    @Test
    void create_doesNotOverrideFechaHoraWhenAlreadySet() {
        LocalDateTime fija = LocalDateTime.of(2025, 3, 3, 13, 37);

        Pedido pedido = new Pedido();
        pedido.setEstado(new EstadoPedido("CREADO"));
        pedido.setUsuario(mock(Usuario.class));
        pedido.setPrecio(new BigDecimal("10.00"));
        pedido.setFechaHora(fija);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido saved = pedidoService.create(pedido);

        assertEquals(fija, saved.getFechaHora(), "Si fechaHora ya viene, no debe pisarla");
        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        assertEquals(fija, captor.getValue().getFechaHora(), "Debe persistir la fechaHora original");
    }

    @Test
    void update_updatesFields_andSaves() {
        Long id = 7L;

        Pedido existente = new Pedido();
        existente.setId(id);
        existente.setEstado(new EstadoPedido("CREADO"));
        existente.setUsuario(mock(Usuario.class));
        existente.setPrecio(new BigDecimal("5.00"));
        existente.setFechaHora(LocalDateTime.of(2025, 1, 1, 10, 0));
        existente.setObservaciones("old");

        Pedido datos = new Pedido();
        datos.setEstado(new EstadoPedido("PAGADO"));
        datos.setUsuario(mock(Usuario.class));
        datos.setPrecio(new BigDecimal("12.50"));
        datos.setFechaHora(LocalDateTime.of(2025, 2, 2, 12, 0));
        datos.setObservaciones("new");

        when(pedidoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido actualizado = pedidoService.update(id, datos);

        assertEquals("PAGADO", actualizado.getEstado().getNombre());
        assertEquals(new BigDecimal("12.50"), actualizado.getPrecio());
        assertEquals(LocalDateTime.of(2025, 2, 2, 12, 0), actualizado.getFechaHora());
        assertEquals("new", actualizado.getObservaciones());
        verify(pedidoRepository).save(existente);
    }

    @Test
    void update_throwsWhenPedidoNotFound() {
        Long id = 404L;
        when(pedidoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> pedidoService.update(id, new Pedido()));
        assertTrue(ex.getMessage().toLowerCase().contains("no encontrado"));
    }
}
