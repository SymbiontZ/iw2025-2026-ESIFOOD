package es.uca.esifoodteam.common.components;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
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
            // Mostrar icono del carrito
            Anchor cartAnchor = new Anchor("/carrito");
            Icon cartIcon = new Icon(VaadinIcon.CART);
            cartIcon.setClassName("navbar-icon");
            cartAnchor.add(cartIcon);
            navbarContainers[2].add(cartAnchor);

            // Mostrar menú de usuario con clic izquierdo
            MenuBar userMenu = new MenuBar();
            userMenu.getStyle().set("border", "none").set("background", "transparent").set("padding", "0");
            
            Icon userIcon = new Icon(VaadinIcon.USER);
            userIcon.setClassName("navbar-icon");
            userMenu.addItem(userIcon, e -> {}).getSubMenu().addItem("Cuenta", ev -> userMenu.getUI().ifPresent(ui -> ui.navigate("/perfil")));
            userMenu.getItems().get(0).getSubMenu().addItem("Pedidos", ev -> userMenu.getUI().ifPresent(ui -> ui.navigate("/pedidos")));
            
            navbarContainers[2].add(userMenu);

            // Enlace para cerrar sesión (Spring Security lo maneja en /logout)
            Anchor logout = new Anchor("/logout");
            Icon logoutIcon = new Icon(VaadinIcon.SIGN_OUT);
            logoutIcon.setClassName("navbar-icon");
            logout.add(logoutIcon);
            logout.setRouterIgnore(true);

            navbarContainers[2].add(logout);
        } else {
            Anchor login = new Anchor("/login");
            Icon loginIcon = new Icon(VaadinIcon.SIGN_IN);
            loginIcon.setClassName("navbar-icon");
            login.add(loginIcon);
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
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return auth != null ? auth.getPrincipal().toString() : "";
}
}