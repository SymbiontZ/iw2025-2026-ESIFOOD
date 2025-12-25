package es.uca.esifoodteam.common.components;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Navbar extends Header {

    public Navbar() {
        addClassName("navbar");
        setWidthFull();
        setHeightFull();

        HorizontalLayout[] navbarContainers = new HorizontalLayout[3];

        for (int i = 0; i < 3; i++) {
            navbarContainers[i] = new HorizontalLayout();
            navbarContainers[i].setClassName("navbar-container");
            add(navbarContainers[i]);
        }

        // COLUMNA IZQUIERDA - OPCIONES
        Anchor carta = new Anchor("/productos", "menús");
        carta.setClassName("navbar-link");
        navbarContainers[0].add(carta);

        // COLUMNA EN MEDIO - TÍTULO
        Anchor title = new Anchor("/", "ESIFOOD");
        title.setClassName("navbar-title");
        navbarContainers[1].add(title);

        // COLUMNA DERECHA - LOGIN / LOGOUT
        if (isUserLoggedIn()) {
            // Mostrar nombre/email del usuario (opcional)
            String username = getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                Anchor userInfo = new Anchor("/perfil", username);
                userInfo.setClassName("navbar-link");
                navbarContainers[2].add(userInfo);
            }

            // Enlace para cerrar sesión (Spring Security lo maneja en /logout)
            Anchor logout = new Anchor("/logout", "Cerrar sesión");
            logout.setRouterIgnore(true);
            navbarContainers[2].add(logout);
        } else {
            Anchor login = new Anchor("/login", "Iniciar sesión");
            login.setClassName("navbar-link");
            navbarContainers[2].add(login);
        }
    }

    private boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return "";
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}