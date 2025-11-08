package es.uca.esifoodteam.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    public Optional<Pedido> findById(Long id);
    public List<Pedido> findByEstado(EstadoPedido estado);


}
