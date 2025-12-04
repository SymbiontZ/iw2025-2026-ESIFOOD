package es.uca.esifoodteam.pedidos.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.esifoodteam.pedidos.models.ActualizacionPedido;
import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.repositories.ActualizacionPedidoRepository;
import es.uca.esifoodteam.pedidos.repositories.PedidoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ActualizacionPedidoService {

    private final ActualizacionPedidoRepository actualizacionPedidoRepository;
    private final PedidoRepository pedidoRepository;

    public ActualizacionPedidoService(ActualizacionPedidoRepository actualizacionPedidoRepository,
                                      PedidoRepository pedidoRepository) {
        this.actualizacionPedidoRepository = actualizacionPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<ActualizacionPedido> findByPedido(Long pedidoId) {
        return actualizacionPedidoRepository.findByPedidoIdOrderByFechaHoraAsc(pedidoId);
    }

    public ActualizacionPedido crearActualizacion(Long pedidoId, String comentario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        ActualizacionPedido act = new ActualizacionPedido();
        act.setPedido(pedido);
        act.setFechaHora(LocalDateTime.now());
        act.setComentario(comentario);

        return actualizacionPedidoRepository.save(act);
    }
}
