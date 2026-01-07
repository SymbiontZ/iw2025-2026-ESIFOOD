package es.uca.esifoodteam.productos.views;

import java.util.*;


import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import es.uca.esifoodteam.productos.services.*;
import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.productos.components.*;
import es.uca.esifoodteam.cart.CarritoService;


@Route("productos")
@PageTitle("ESIFOOD | Menús")
public class MostrarTipoProductos extends MainLayout
    implements HasUrlParameter<String> {
        
    private final TipoProductoService tipoProductoService;
    private final ProductoService productoService;
    private final CarritoService carritoService;

    public MostrarTipoProductos(TipoProductoService tipoProductoService, 
                                ProductoService productoService,
                                CarritoService carritoService) {
        this.tipoProductoService = tipoProductoService;
        this.productoService = productoService;
        this.carritoService = carritoService;
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
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);
        content.setSizeFull();
        
        H2 header = new H2("Nuestros Menús");
        header.addClassName("admin-header");
        content.add(header);
        
        List<TipoProducto> tipos = tipoProductoService.findAll();
        if (tipos.isEmpty()) {
            Paragraph mensaje = new Paragraph("No hay tipos de productos disponibles");
            content.add(mensaje);
            addContent(content);
            return;
        }
        content.add(new TipoProductoGrid(tipos));
        addContent(content);
    }

    private void mostrarTipoProducto(TipoProducto tipoProducto) {
        clearContent();
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);
        content.setSizeFull();
        
        H2 header = new H2(tipoProducto.getNombre());
        header.addClassName("admin-header");
        content.add(header);
        
        List<Producto> productos = productoService.findByTipoProducto(tipoProducto);

        if (productos.isEmpty()) {
            Paragraph mensaje = new Paragraph("No hay productos disponibles");
            content.add(mensaje);
            addContent(content);
            return;
        }

        ProductoGrid grid = new ProductoGrid(carritoService);
        grid.setItems(productos);
        content.add(grid);
        addContent(content);
    }

    private void mostrarNoEncontrado() {
        clearContent();
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);
        content.setSizeFull();
        
        H2 header = new H2("No se encontró el tipo de producto");
        header.addClassName("admin-header");
        Paragraph mensaje = new Paragraph("Intenta navegar desde la página de menús");
        content.add(header, mensaje);
        
        addContent(content);
    }
}
