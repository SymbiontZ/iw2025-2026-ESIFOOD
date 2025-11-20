package es.uca.esifoodteam.productos;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    public  Optional<Producto> findById(Long id);   
}
