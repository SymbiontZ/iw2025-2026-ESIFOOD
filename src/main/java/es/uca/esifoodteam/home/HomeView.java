package es.uca.esifoodteam.home;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.List;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.productos.components.ProductoGrid;
import es.uca.esifoodteam.productos.services.ProductoService;
import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.cart.CarritoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("")
@PageTitle("Inicio | ESIFOOD")
@AnonymousAllowed
public class HomeView extends MainLayout {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeView.class);
    
    public HomeView(ProductoService productoService, CarritoService carritoService) {
        try {
            add(new H1("Bienvenido a EsiFood"));
            
            List<Producto> productos = productoService.findDisponibles();
            logger.info("Productos disponibles: {}", productos.size());
            
            ProductoGrid grid = new ProductoGrid(carritoService);
            grid.setItems(productos);
            add(grid);
        } catch (Exception e) {
            logger.error("Error en HomeView", e);
            throw new RuntimeException("Error al cargar HomeView", e);
        }
    }
}