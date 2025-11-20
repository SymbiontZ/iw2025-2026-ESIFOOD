package es.uca.esifoodteam.productos;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    private final ProductoRepository productoRepo;

    public ProductoService(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    public Producto save(Producto producto) {
        return productoRepo.save(producto);
    }

    public Producto findById(Long id) {
        return productoRepo.findById(id).orElse(null);
    }

    public List<Producto> findAll() {
        return productoRepo.findAll();
    }

}
