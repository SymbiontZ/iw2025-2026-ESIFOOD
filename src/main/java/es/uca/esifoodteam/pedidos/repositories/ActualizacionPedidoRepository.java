package es.uca.esifoodteam.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.pedidos.models.ActualizacionPedido;

import java.util.List;

@Repository
public interface ActualizacionPedidoRepository extends JpaRepository<ActualizacionPedido, Long> {

    // Todas las actualizaciones de un pedido
    List<ActualizacionPedido> findByPedidoId(Long pedidoId);

    // Ordenadas por fecha/hora, si quieres histórico
    List<ActualizacionPedido> findByPedidoIdOrderByFechaHoraAsc(Long pedidoId);
}
