package es.uca.esifoodteam.productos.components;

import java.util.List;

import es.uca.esifoodteam.productos.models.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TipoProductoGrid extends VerticalLayout {

    public TipoProductoGrid(List<TipoProducto> items) {
        setClassName("tipo-producto-grid");
        setWidthFull();
        setPadding(false);
        setSpacing(true);
        
        for (TipoProducto tipoProducto : items) {
            add(new TipoProductoCard(tipoProducto));
        }
    }

}
