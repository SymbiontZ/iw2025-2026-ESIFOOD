package es.uca.esifoodteam.home;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.productos.components.ProductoGrid;
import es.uca.esifoodteam.productos.services.ProductoService;

@Route("")
@PageTitle("Inicio | ESIFOOD")
@AnonymousAllowed
public class HomeView extends MainLayout {
    
    public HomeView(ProductoService productoService) {
        add(new H1("Bienvenido a EsiFood"));
        
        ProductoGrid grid = new ProductoGrid(productoService.findDisponibles());
        add(grid);            
    }
}