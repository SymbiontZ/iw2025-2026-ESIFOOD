package es.uca.esifoodteam.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.pedidos.models.LineaPedido;

import java.util.List;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long> {

    List<LineaPedido> findByPedidoId(Long pedidoId);
}
