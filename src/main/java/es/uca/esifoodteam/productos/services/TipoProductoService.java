package es.uca.esifoodteam.productos.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.esifoodteam.productos.models.TipoProducto;
import es.uca.esifoodteam.productos.repositories.TipoProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TipoProductoService {

    private final TipoProductoRepository tipoProductoRepository;

    public TipoProductoService(TipoProductoRepository tipoProductoRepository) {
        this.tipoProductoRepository = tipoProductoRepository;
    }

    public List<TipoProducto> findAll() {
        return tipoProductoRepository.findAll();
    }

    public Optional<TipoProducto> findById(Long id) {
        return tipoProductoRepository.findById(id);
    }

    public Optional<TipoProducto> findByNombre(String nombre) {
        return tipoProductoRepository.findByNombre(nombre);
    }
    
    public Optional<TipoProducto> findByNombreId(String nombreId) {
        return tipoProductoRepository.findByNombreId(nombreId);
    }


    public TipoProducto save(TipoProducto tipoProducto) {
        return tipoProductoRepository.save(tipoProducto);
    }

    public void delete(Long id) {
        tipoProductoRepository.deleteById(id);
    }
}
