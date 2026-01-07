package es.uca.esifoodteam.productos.components;

import java.util.List;

import es.uca.esifoodteam.productos.models.*;
import com.vaadin.flow.component.html.Div;

public class TipoProductoGrid extends Div {

    public TipoProductoGrid(List<TipoProducto> items) {
        addClassName("tipo-producto-grid");
        
        for (TipoProducto tipoProducto : items) {
            add(new TipoProductoCard(tipoProducto));
        }
    }

}
