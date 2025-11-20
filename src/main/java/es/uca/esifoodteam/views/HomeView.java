package es.uca.esifoodteam.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.productos.*;
import es.uca.esifoodteam.components.productos.*;
import es.uca.esifoodteam.layouts.MainLayout;

@Route("")
@PageTitle("Inicio | ESIFOOD")
public class HomeView extends MainLayout {
    private final ProductoService productoService;
    
    public HomeView(ProductoService productoService) {
        this.productoService = productoService;
        add(new H1("Bienvenido a EsiFood"));
        
        for (Producto producto : productoService.findAll()) {
            add(new ProductoCard(producto));
        }
    }
}