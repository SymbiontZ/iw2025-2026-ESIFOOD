package es.uca.esifoodteam.home;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.productos.*;
import es.uca.esifoodteam.productos.models.Producto;
import es.uca.esifoodteam.productos.services.ProductoService;
import es.uca.esifoodteam.components.productos.*;
import es.uca.esifoodteam.layouts.MainLayout;

@Route("")
@PageTitle("Inicio | ESIFOOD")
public class HomeView extends MainLayout {
    
    public HomeView(ProductoService productoService) {
        add(new H1("Bienvenido a EsiFood"));
        
        ProductoCarrusel carrusel = new ProductoCarrusel();
        add(carrusel);


        for (Producto producto : productoService.findAll()) {
            carrusel.addProducto(producto);
        }
    }
}