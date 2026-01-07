package es.uca.esifoodteam.productos.components;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import es.uca.esifoodteam.cart.CarritoService;
import es.uca.esifoodteam.productos.models.Producto;
import java.util.List;

public class ProductosRecientesSection extends VerticalLayout {

    public ProductosRecientesSection(List<Producto> productos, CarritoService carritoService) {
        addClassName("productos-section");
        setSpacing(true);
        setPadding(true);
        setWidthFull();

        H2 titulo = new H2("Productos Recientes");
        titulo.addClassName("admin-header");
        add(titulo);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassName("productos-recientes-container");
        container.setSpacing(true);
        container.setPadding(false);
        container.setWidthFull();
        container.setHeight("auto");

        for (Producto producto : productos) {
            ProductoCard card = new ProductoCard(carritoService);
            card.setProducto(producto);
            card.addClassName("producto-reciente-item");
            container.add(card);
        }

        add(container);
    }
}
