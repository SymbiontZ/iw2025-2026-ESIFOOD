package es.uca.esifoodteam.productos.views;

import java.util.*;


import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.*;

import es.uca.esifoodteam.productos.services.*;
import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.productos.components.*;


@Route("productos")
@PageTitle("ESIFOOD | Menús")
public class MostrarTipoProductos extends MainLayout
    implements HasUrlParameter<String> {
        
    private final TipoProductoService tipoProductoService;
    private final ProductoService productoService;

    public MostrarTipoProductos(TipoProductoService tipoProductoService, ProductoService productoService) {
        this.tipoProductoService = tipoProductoService;
        this.productoService = productoService;
    }
    
    
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            System.out.println("Parametro recibido: " + parameter);
            Optional<TipoProducto> tipoSeleccionado = tipoProductoService.findByNombreId(parameter);
            if (tipoSeleccionado.isPresent()) {
                mostrarTipoProducto(tipoSeleccionado.get());
            } else {
                mostrarNoEncontrado();
            }
        } else {
            System.out.println("No se ha recibido ningun parametro");
            mostrarTipoProductos();
        }
    }

    private void mostrarTipoProductos() {
        clearContent();
        List<TipoProducto> tipos = tipoProductoService.findAll();
        if (tipos.isEmpty()) {
            add(new H2("No hay tipos de productos disponibles :C"));
            return;
        }
        add(new TipoProductoGrid(tipos));
    }

    private void mostrarTipoProducto(TipoProducto tipoProducto) {
        clearContent();
        List<Producto> productos = productoService.findByTipoProducto(tipoProducto);

        if (productos.isEmpty()) {
            add(new H2("No hay productos disponibles :C"));
            return;
        }

        
    }

    private void mostrarNoEncontrado() {
        clearContent();
        add(new H2("No se encontró el tipo de producto"));
    }
}
