package es.uca.esifoodteam.cart;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.*;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.cart.models.*;
import es.uca.esifoodteam.productos.models.*;





@Route("carrito")
public class CarritoView extends MainLayout{
    public CarritoView() {
        add(new H1("Test"));
    }

}
