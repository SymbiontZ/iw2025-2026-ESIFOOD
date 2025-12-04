package es.uca.esifoodteam.components.productos;

import es.uca.esifoodteam.productos.*;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public class ProductoCarrusel extends HorizontalLayout {
    public ProductoCarrusel() {
        addClassName("producto-carrusel");
        setPadding(false);
        setSpacing(true);
    }

    public void addProducto(Producto producto) {
        ProductoCard productoCard = new ProductoCard(producto);
        add(productoCard);
    }

}