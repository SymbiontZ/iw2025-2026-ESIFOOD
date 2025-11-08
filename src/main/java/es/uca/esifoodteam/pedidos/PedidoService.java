package es.uca.esifoodteam.pedidos;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepo;

    public PedidoService(PedidoRepository pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    public List<Pedido> findByEstado(EstadoPedido estado) {
        return pedidoRepo.findByEstado(estado);
    }

    public Pedido save(Pedido pedido) {
        return pedidoRepo.save(pedido);
    }
}
