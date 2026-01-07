package es.uca.esifoodteam.productos.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.*;
import es.uca.esifoodteam.common.components.*;

import es.uca.esifoodteam.productos.models.*;

public class TipoProductoCard extends VerticalLayout{
    
    public TipoProductoCard(TipoProducto tipoProducto) {
        setPadding(true);
        setSpacing(true);
        setHeightFull();

        addClassName("tipo-producto-card");

        ImageLink imagen = new ImageLink(
            "/productos/" + tipoProducto.getNombreId(),
            tipoProducto.getNombreId(),
            "Imagen " + tipoProducto.getNombre(),
            "tipo-menu-image"
        );

        H2 nombre = new H2(tipoProducto.getNombre());
        nombre.addClassName("tipo-producto-nombre");
        
        add(imagen, nombre);
    }
    
}
