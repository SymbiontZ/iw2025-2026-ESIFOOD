package es.uca.esifoodteam.usuarios.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.UsuarioRepository;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public AuthService(BCryptPasswordEncoder passwordEncoder, UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }


    public Optional<Usuario> login(String email, String pass) {
        return usuarioRepository.findByEmailAndEsActivo(email, true)
            .filter(usuario -> passwordEncoder.matches(pass, usuario.getPass()));
    }

    public Usuario createWithPassword(Usuario usuario, String passPlana) {
        usuario.setPass(passwordEncoder.encode(passPlana)); 
        return usuarioService.create(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailAndEsActivo(email, true)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Pass almacenada (hash): " + usuario.getPass());
        System.out.println("Tipo usuario: " + usuario.getTipo_id().getNombre());
        System.out.println("==================");

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getTipo_id().getNombre().toUpperCase());

        return org.springframework.security.core.userdetails.User.builder()
            .username(usuario.getEmail())
            .password(usuario.getPass())
            .authorities(authority)
            .build();
    }
}