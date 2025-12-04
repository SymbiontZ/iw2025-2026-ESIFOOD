package es.uca.esifoodteam.pedidos.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.repositories.PedidoRepository;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> findByUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> findByEstado(String estadoNombre) {
        return pedidoRepository.findByEstadoNombre(estadoNombre);
    }

    public List<Pedido> findBetween(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaHoraBetween(inicio, fin);
    }

    public Pedido create(Pedido pedido) {
        if (pedido.getFechaHora() == null) {
            pedido.setFechaHora(LocalDateTime.now());
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido update(Long id, Pedido datos) {
        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        existente.setEstado(datos.getEstado());
        existente.setUsuario(datos.getUsuario());
        existente.setPrecio(datos.getPrecio());
        existente.setFechaHora(datos.getFechaHora());
        existente.setObservaciones(datos.getObservaciones());

        return pedidoRepository.save(existente);
    }

    public void delete(Long id) {
        pedidoRepository.deleteById(id);
    }
}