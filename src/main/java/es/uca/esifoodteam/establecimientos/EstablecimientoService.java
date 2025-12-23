package es.uca.esifoodteam.establecimientos;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Establecimiento save(Establecimiento local) {
        return EstablecimientoRepository.save(local);
    }

    public void delete(Long id) {
        EstablecimientoRepository.deleteById(id);
    }
}
