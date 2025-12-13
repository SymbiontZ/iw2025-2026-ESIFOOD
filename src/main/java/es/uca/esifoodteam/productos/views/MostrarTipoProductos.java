package es.uca.esifoodteam.productos.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import es.uca.esifoodteam.productos.services.*;
import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.productos.components.*;


@Route("menus")
@PageTitle("ESIFOOD | Menús")
public class MostrarTipoProductos extends VerticalLayout{
    
    public MostrarTipoProductos(TipoProductoService tipoProductoService) {
        for (TipoProducto tipoProducto : tipoProductoService.findAll()) {
            add(new TipoProductoCard(tipoProducto));
        }
    }
}
