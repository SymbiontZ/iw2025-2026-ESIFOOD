package es.uca.esifoodteam.home;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.List;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.productos.components.ProductosRecientesListSection;
import es.uca.esifoodteam.productos.components.ProductosMasCompradosListSection;
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
            VerticalLayout content = new VerticalLayout();
            content.setSpacing(true);
            content.setPadding(true);
            content.setSizeFull();
            
            H1 titulo = new H1("Bienvenido a ESIFOOD");
            titulo.addClassName("admin-header");
            content.add(titulo);
            
            List<Producto> productos = productoService.findDisponibles();
            logger.info("Productos disponibles: {}", productos.size());
            
            if (productos.isEmpty()) {
                Paragraph mensaje = new Paragraph("No hay productos disponibles en este momento");
                content.add(mensaje);
            } else {
                // Productos recientes
                List<Producto> productosRecientes = obtenerRecientes(productos);
                if (!productosRecientes.isEmpty()) {
                    content.add(new ProductosRecientesListSection(productosRecientes, carritoService));
                }
                
                // Más comprados
                List<Producto> productosComprados = obtenerMasComprados(productos);
                if (!productosComprados.isEmpty()) {
                    content.add(new ProductosMasCompradosListSection(productosComprados, carritoService));
                }
            }
            
            addContent(content);
        } catch (Exception e) {
            logger.error("Error en HomeView", e);
            throw new RuntimeException("Error al cargar HomeView", e);
        }
    }
    
    
    private List<Producto> obtenerRecientes(List<Producto> productos) {
        return productos.stream()
            .sorted((p1, p2) -> {
                if (p1.getCreatedDate() == null || p2.getCreatedDate() == null) return 0;
                return p2.getCreatedDate().compareTo(p1.getCreatedDate());
            })
            .limit(4)
            .toList();
    }
    
    private List<Producto> obtenerMasComprados(List<Producto> productos) {
        // Nota: Esto es un ejemplo. Si tienes un campo cantidadVendida, úsalo aquí
        // Por ahora devolvemos los últimos 4 productos modificados
        return productos.stream()
            .sorted((p1, p2) -> {
                if (p1.getModifiedDate() == null || p2.getModifiedDate() == null) return 0;
                return p2.getModifiedDate().compareTo(p1.getModifiedDate());
            })
            .limit(4)
            .toList();
    }
}