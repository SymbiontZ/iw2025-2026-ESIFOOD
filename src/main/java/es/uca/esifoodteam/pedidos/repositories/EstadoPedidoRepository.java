package es.uca.esifoodteam.pedidos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.pedidos.models.EstadoPedido;

import java.util.Optional;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Long> {
    Optional<EstadoPedido> findByNombre(String nombre);
}
