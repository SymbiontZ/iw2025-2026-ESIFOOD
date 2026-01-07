package es.uca.esifoodteam.productos.components;

import es.uca.esifoodteam.cart.CarritoService;
import es.uca.esifoodteam.productos.models.Producto;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class ProductoGrid extends VerticalLayout {
    private final CarritoService carritoService;
    private List<Producto> items;
    private VerticalLayout gridContainer;

    @Autowired
    public ProductoGrid(CarritoService carritoService) {
        this.carritoService = carritoService;
        addClassName("producto-grid");
        setPadding(true);
        setSpacing(true);
        setWidthFull();
        
        gridContainer = new VerticalLayout();
        gridContainer.addClassName("producto-grid-container");
        gridContainer.setPadding(false);
        gridContainer.setSpacing(true);
        gridContainer.setWidthFull();
        
        add(gridContainer);
    }

    public void setItems(List<Producto> items) {
        this.items = items;
        gridContainer.removeAll();
        for (Producto producto : items) {
            ProductoCard card = new ProductoCard(carritoService);
            card.setProducto(producto);
            card.addClassName("producto-grid-item");
            gridContainer.add(card);
        }
    }

    public List<Producto> getItems() {
        return items;
    }

}