package es.uca.esifoodteam.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.pedidos.models.Pedido;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByEstadoNombre(String nombreEstado);

    List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
