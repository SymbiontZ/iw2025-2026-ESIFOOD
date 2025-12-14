package es.uca.esifoodteam.productos.components;

import es.uca.esifoodteam.productos.models.Producto;

import java.util.List;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class ProductoGrid extends HorizontalLayout {
    public ProductoGrid(List<Producto> items) {
        addClassName("producto-grid");
        setPadding(false);
        setSpacing(true);
        
        for (Producto producto : items) {
            add(new ProductoCard(producto));
        }
    }

}