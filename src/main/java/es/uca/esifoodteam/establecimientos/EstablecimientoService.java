package es.uca.esifoodteam.establecimientos;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstablecimientoService {

    private final EstablecimientoRepository EstablecimientoRepository;

    public EstablecimientoService(EstablecimientoRepository EstablecimientoRepository) {
        this.EstablecimientoRepository = EstablecimientoRepository;
    }

    public List<Establecimiento> findAll() {
        return EstablecimientoRepository.findAll();
    }

    public Optional<Establecimiento> findById(Long id) {
        return EstablecimientoRepository.findById(id);
    }

    public List<Establecimiento> buscarPorNombre(String nombre) {
        return EstablecimientoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Establecimiento> findDisponibles() {
        return EstablecimientoRepository.findByEstaDisponibleTrue();
    }

    public Establecimiento save(Establecimiento local) {
        return EstablecimientoRepository.save(local);
    }

    public void delete(Long id) {
        EstablecimientoRepository.deleteById(id);
    }
}
