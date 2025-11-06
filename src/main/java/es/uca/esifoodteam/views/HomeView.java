package es.uca.esifoodteam.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {
    public HomeView() {
        add(new H1("Bienvenido a EsiFood"));
    }
}