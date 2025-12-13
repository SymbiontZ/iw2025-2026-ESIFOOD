package es.uca.esifoodteam.productos.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.*;

import es.uca.esifoodteam.productos.models.*;

public class TipoProductoCard extends VerticalLayout{
    
    public TipoProductoCard(TipoProducto tipoProducto) {
        setPadding(true);
        setSpacing(true);

        addClassName("tipo-menu-card");

        Image imagen = new Image("test.jpg", tipoProducto.getNombre());

        H2 nombre = new H2(tipoProducto.getNombre());
        add(imagen, nombre);
    }
    
}
