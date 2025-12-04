package es.uca.esifoodteam.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Minus.Horizontal;

import es.uca.esifoodteam.productos.*;
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