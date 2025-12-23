package es.uca.esifoodteam.cart;

import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.html.*;
import java.util.*;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.cart.models.*;
import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.productos.services.*;


@Route("carrito")
public class CarritoView extends MainLayout{
    private CarritoService carritoService;
    private ProductoService productoService;

    @Autowired
    public CarritoView(CarritoService carritoService, ProductoService productoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
        add(new H1("Test"));

        Paragraph p = new Paragraph("Este es el carrito de la compra.");
        Paragraph test = new Paragraph("Cargando...");
        add(p);
        add(test);

        // Cargar el carrito de forma asíncrona
        carritoService.getCarro().thenAccept(carro -> {
            StringBuilder contenido = new StringBuilder("Contenido del carrito:\n");
            
            format_carrito(carro, contenido);

            test.setText(contenido.toString());
        }).exceptionally(ex -> {
            test.setText("Error al cargar el carrito: " + ex.getMessage());
            return null;
        });
    }

    public void format_carrito(Carro carro, StringBuilder contenido) {
        List<ItemCarro> items = carro.getItems();

        if (items == null || items.isEmpty()) {
            contenido.append("El carrito está vacío.\n");
            return;
        }

        for (ItemCarro item : items) {
            Producto producto = item.getProducto();
            if (producto != null) {
                contenido.append("- ")
                         .append(producto.getNombre())
                         .append(" x ")
                         .append(item.getCantidad())
                         .append("\n");
            }
        }
    }

}
