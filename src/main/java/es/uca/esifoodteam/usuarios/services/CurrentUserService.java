package es.uca.esifoodteam.usuarios.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;

@Service
public class CurrentUserService {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getCurrentUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }

        if (email == null) {
            return null;
        }

        return usuarioRepository
                .findByEmailAndEsActivo(email, true)
                .orElse(null);
    }
}