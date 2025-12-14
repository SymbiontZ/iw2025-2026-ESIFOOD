package es.uca.esifoodteam.productos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.esifoodteam.productos.models.TipoProducto;

import java.util.*;


@Repository
public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {

    Optional<TipoProducto> findByNombre(String nombre);
    
    Optional<TipoProducto> findByNombreId(String nombreId);

    List<TipoProducto> findAll();

}
