package es.uca.esifoodteam.establecimientos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {

    List<Establecimiento> findByNombreContainingIgnoreCase(String nombre);

    List<Establecimiento> findByEstaDisponibleTrue();
}
