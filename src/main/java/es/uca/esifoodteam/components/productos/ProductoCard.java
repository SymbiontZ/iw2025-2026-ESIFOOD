package es.uca.esifoodteam.components.productos;

import es.uca.esifoodteam.productos.*;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProductoCard extends VerticalLayout{
    private final Image imagen;
    private final H3 nombre;
    private final Paragraph descripcion;

    public ProductoCard(Producto producto) {
        addClassName("producto-card");    
        setPadding(false);
        setSpacing(false);

        if (producto.getImagenUrl() != null)
            imagen = new Image(producto.getImagenUrl(), "Imagen de " + producto.getNombre());
        else
            imagen = new Image("", "Imagen no disponible");

        nombre = new H3(producto.getNombre());
        descripcion = new Paragraph(producto.getDescripcion());

        add(imagen, nombre, descripcion);
    }

    public void setProducto(Producto producto) {
        imagen.setSrc(producto.getImagenUrl());
        imagen.setAlt("Imagen de " + producto.getNombre());
        nombre.setText(producto.getNombre());
        descripcion.setText(producto.getDescripcion());
    }

}
