package es.uca.esifoodteam.pedidos;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EstadoPedidoService {

    private final EstadoPedidoRepository estadoPedidoRepository;

    public EstadoPedidoService(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    public List<EstadoPedido> findAll() {
        return estadoPedidoRepository.findAll();
    }

    public EstadoPedido findByNombreOrThrow(String nombre) {
        return estadoPedidoRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("EstadoPedido no encontrado: " + nombre));
    }
}
