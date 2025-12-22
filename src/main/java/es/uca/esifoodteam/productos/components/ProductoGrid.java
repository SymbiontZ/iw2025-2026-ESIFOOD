package es.uca.esifoodteam.productos.components;

import es.uca.esifoodteam.cart.CarritoService;
import es.uca.esifoodteam.productos.models.Producto;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class ProductoGrid extends HorizontalLayout {
    private final CarritoService carritoService;
    private List<Producto> items;

    @Autowired
    public ProductoGrid(CarritoService carritoService) {
        this.carritoService = carritoService;
        addClassName("producto-grid");
        setPadding(false);
        setSpacing(true);
    }

    public void setItems(List<Producto> items) {
        this.items = items;
        removeAll();
        for (Producto producto : items) {
            ProductoCard card = new ProductoCard(carritoService);
            card.setProducto(producto);
            add(card);
        }
    }

    public List<Producto> getItems() {
        return items;
    }

}