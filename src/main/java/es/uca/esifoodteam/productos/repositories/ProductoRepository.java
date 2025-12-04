package es.uca.esifoodteam.productos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.productos.models.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Solo disponibles
    List<Producto> findByDisponibleTrue();

    // Por tipo (solo aplicable a Simple, pero accesible por la jerarquía)
    List<Producto> findByClass(Class<? extends Producto> tipo);
}
