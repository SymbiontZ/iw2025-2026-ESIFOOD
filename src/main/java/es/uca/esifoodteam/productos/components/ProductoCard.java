package es.uca.esifoodteam.productos.components;

import es.uca.esifoodteam.productos.models.Producto;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import es.uca.esifoodteam.cart.*;

public class ProductoCard extends VerticalLayout{
    private final Image imagen;
    private final H3 nombre;
    private final Paragraph descripcion;
    private final Button agregarCarro;

    private Producto producto;
    private final CarritoService carritoService;

    public ProductoCard(CarritoService carritoService) {
        this.carritoService = carritoService;
        addClassName("producto-card");    
        setPadding(false);
        setSpacing(false);

        imagen = new Image();
        imagen.addClassName("producto-card-imagen");
        
        nombre = new H3();
        nombre.addClassName("producto-card-nombre");
        
        descripcion = new Paragraph();
        descripcion.addClassName("producto-card-descripcion");
        
        agregarCarro = new Button("Agregar al carrito", new Icon(VaadinIcon.CART));
        agregarCarro.addClassName("producto-card-boton");
        agregarCarro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        agregarCarro.setWidthFull();
        agregarCarro.addClickListener(clickEvent -> {
                carritoService.agregarCarrito(producto, 1)
                    .thenAccept(result -> {
                        Notification notification = new Notification(
                            "Producto agregado al carrito",
                            3000,
                            Notification.Position.TOP_CENTER
                        );
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.open();
                    })
                    .exceptionally(ex -> {
                        System.err.println("Error al agregar al carrito: " + ex.getMessage());
                        Notification notification = new Notification(
                            "Error al agregar al carrito",
                            3000,
                            Notification.Position.TOP_CENTER
                        );
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.open();
                        return null;
                    });
            }
        );

        add(imagen, nombre, descripcion, agregarCarro);
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        updateComponent();
    }

    private void updateComponent() {
        imagen.setSrc(producto.getImagenUrl() != null ? producto.getImagenUrl() : "");
        imagen.setAlt("Imagen de " + producto.getNombre());
        nombre.setText(producto.getNombre());
        descripcion.setText(producto.getDescripcion());
    }
}
