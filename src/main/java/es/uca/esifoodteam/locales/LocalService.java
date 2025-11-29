package es.uca.esifoodteam.locales;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocalService {

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public List<Local> findAll() {
        return localRepository.findAll();
    }

    public Optional<Local> findById(Long id) {
        return localRepository.findById(id);
    }

    public List<Local> buscarPorNombre(String nombre) {
        return localRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Local> findAbiertos() {
        return localRepository.findByEstaAbiertoTrue();
    }

    public Local save(Local local) {
        return localRepository.save(local);
    }

    public void delete(Long id) {
        localRepository.deleteById(id);
    }
}
