package es.uca.esifoodteam.productos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.productos.models.*;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar por nombre (contiene, ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Solo disponibles
    List<Producto> findByDisponibleTrue();

    // Por tipo (busca en la colección tipos)
    List<Producto> findByTiposContaining(TipoProducto tipoProducto);
}
