package es.uca.esifoodteam.common.components;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;

public class Navbar extends Header{
    public Navbar() {
        addClassName("navbar");
        setWidthFull();
        setHeightFull();

        HorizontalLayout [] navbarContainers = new HorizontalLayout[3];

        for (int i= 0; i<3; i++) {
            navbarContainers[i] = new HorizontalLayout();
            navbarContainers[i].setClassName("navbar-container");
            add(navbarContainers[i]);
        }

        //COLUMNA IZQUIERDA - OPCIONES
        Anchor carta = new Anchor("/productos", "menús");
        carta.setClassName("navbar-link");
        navbarContainers[0].add(carta);


        //COLUMNA EN MEDIO - TITULO
        Anchor title = new Anchor("/", "ESIFOOD");
        title.setClassName("navbar-title");
        navbarContainers[1].add(title);


        //COLUMNA DERECHA - LOGIN
        Anchor login = new Anchor("/login", "Iniciar sesión");
        login.setClassName("navbar-link");
        navbarContainers[2].add(login);

    }
}
