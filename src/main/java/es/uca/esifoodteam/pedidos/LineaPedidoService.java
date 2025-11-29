package es.uca.esifoodteam.pedidos;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LineaPedidoService {

    private final LineaPedidoRepository lineaPedidoRepository;
    private final PedidoRepository pedidoRepository;

    public LineaPedidoService(LineaPedidoRepository lineaPedidoRepository,
                              PedidoRepository pedidoRepository) {
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<LineaPedido> findByPedido(Long pedidoId) {
        return lineaPedidoRepository.findByPedidoId(pedidoId);
    }

    public LineaPedido addLineaToPedido(Long pedidoId, LineaPedido linea) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        linea.setPedido(pedido);
        return lineaPedidoRepository.save(linea);
    }

    public void delete(Long lineaId) {
        lineaPedidoRepository.deleteById(lineaId);
    }
}
