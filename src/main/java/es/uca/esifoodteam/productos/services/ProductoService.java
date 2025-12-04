package es.uca.esifoodteam.productos.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.esifoodteam.productos.models.Producto;
import es.uca.esifoodteam.productos.repositories.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarPorNombre(String filtro) {
        return productoRepository.findByNombreContainingIgnoreCase(filtro);
    }

    public List<Producto> findByLocal(Long localId) {
        return productoRepository.findByLocalId(localId);
    }

    public List<Producto> findDisponibles() {
        return productoRepository.findByDisponibleTrue();
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        productoRepository.deleteById(id);
    }
}
