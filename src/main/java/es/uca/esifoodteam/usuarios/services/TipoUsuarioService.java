package es.uca.esifoodteam.usuarios.services;

import org.springframework.stereotype.Service;
import es.uca.esifoodteam.usuarios.repositories.*;
import es.uca.esifoodteam.usuarios.models.*;


@Service
public class TipoUsuarioService {
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioService(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    public TipoUsuario cliente() {
        return tipoUsuarioRepository
            .findByNombre("CLIENTE").
            orElseThrow(() -> new RuntimeException("Tipo 'CLIENTE' no encontrado"));
    }



}
