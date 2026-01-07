package es.uca.esifoodteam.cart;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.*;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.cart.models.*;
import es.uca.esifoodteam.pedidos.models.*;
import es.uca.esifoodteam.pedidos.services.PedidoService;
import es.uca.esifoodteam.pedidos.repositories.EstadoPedidoRepository;
import es.uca.esifoodteam.productos.models.*;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;
import es.uca.esifoodteam.usuarios.models.Usuario;

@Route("carrito")
public class CarritoView extends MainLayout {
    private final CarritoService carritoService;
    private final PedidoService pedidoService;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final CurrentUserService currentUserService;

    private VerticalLayout mainLayout;
    private Carro carroActual;
    private Boolean tipoEntregaSeleccionado;

    @Autowired
    public CarritoView(CarritoService carritoService, PedidoService pedidoService,
                       EstadoPedidoRepository estadoPedidoRepository,
                       CurrentUserService currentUserService) {
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
        this.estadoPedidoRepository = estadoPedidoRepository;
        this.currentUserService = currentUserService;

        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setPadding(false);
        mainLayout.getStyle().set("background", "linear-gradient(to right, #F9F5F0 0%, #f5f1ed 100%)")
                             .set("padding", "3rem 1rem");
        addContent(mainLayout);

        // Cargar carrito
        carritoService.getCarro().thenAccept(carro -> {
            carroActual = carro;
            renderizar(carro);
        }).exceptionally(ex -> {
            mostrarError("Error al cargar carrito: " + ex.getMessage());
            return null;
        });
    }

    private void renderizar(Carro carro) {
        mainLayout.removeAll();
        
        if (carro.getItems() == null || carro.getItems().isEmpty()) {
            mostrarCarritoVacio();
            return;
        }

        VerticalLayout containerCentral = new VerticalLayout();
        containerCentral.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        containerCentral.setWidthFull();

        // Card principal
        Div card = new Div();
        card.setWidthFull();
        card.getStyle()
            .set("max-width", "700px")
            .set("background", "white")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 12px rgba(52, 79, 31, 0.1)")
            .set("padding", "2.5rem");

        // Header
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setSpacing(true);
        header.setWidthFull();
        
        Icon cartIcon = new Icon(VaadinIcon.CART);
        cartIcon.setSize("2.5rem");
        cartIcon.getStyle().set("color", "#344f1f");
        
        H1 titulo = new H1("Mi Carrito");
        titulo.getStyle().set("margin", "0").set("color", "#344f1f").set("flex-grow", "1");
        
        header.add(cartIcon, titulo);

        // Productos
        VerticalLayout productosLayout = new VerticalLayout();
        productosLayout.setSpacing(true);
        productosLayout.setPadding(false);
        
        if (carro.getItems() != null) {
            for (ItemCarro item : carro.getItems()) {
                if (item.getProducto() != null) {
                    productosLayout.add(crearFilaProducto(item));
                }
            }
        }

        // Resumen
        BigDecimal total = calcularSubtotal(carro);
        Div resumenDiv = crearResumen(total);

        card.add(header, new Hr(), productosLayout, new Hr(), resumenDiv, crearBotonesAccion());
        containerCentral.add(card);
        mainLayout.add(containerCentral);
    }

    private void mostrarCarritoVacio() {
        VerticalLayout containerCentral = new VerticalLayout();
        containerCentral.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        containerCentral.setWidthFull();
        containerCentral.setHeightFull();
        containerCentral.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Card vacío
        Div card = new Div();
        card.getStyle()
            .set("max-width", "500px")
            .set("background", "white")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 12px rgba(52, 79, 31, 0.1)")
            .set("padding", "3rem")
            .set("text-align", "center");

        Icon emptyIcon = new Icon(VaadinIcon.CART_O);
        emptyIcon.setSize("4rem");
        emptyIcon.getStyle().set("color", "#344f1f");

        H2 vacio = new H2("Tu carrito está vacío");
        vacio.getStyle().set("color", "#344f1f").set("margin", "1rem 0 0.5rem 0");

        Paragraph desc = new Paragraph("No hay productos en tu carrito. ¡Continúa comprando!");
        desc.getStyle().set("color", "#666").set("margin-bottom", "2rem");

        Button verBtn = new Button("Ver Productos", new Icon(VaadinIcon.SHOP));
        verBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        verBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("/productos")));
        verBtn.setWidth("100%");

        card.add(emptyIcon, vacio, desc, verBtn);
        containerCentral.add(card);
        mainLayout.add(containerCentral);
    }

    private HorizontalLayout crearFilaProducto(ItemCarro item) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.setAlignItems(FlexComponent.Alignment.CENTER);
        fila.setWidthFull();
        fila.setSpacing(true);
        
        Span nombre = new Span(item.getProducto().getNombre());
        nombre.getStyle().set("font-weight", "500").set("flex-grow", "1");
        
        Span cantidad = new Span("x" + item.getCantidad());
        cantidad.getStyle().set("min-width", "60px").set("text-align", "center");
        
        BigDecimal subtotal = item.getProducto().getPrecio()
            .multiply(BigDecimal.valueOf(item.getCantidad()));
        Span precio = new Span(String.format("%.2f €", subtotal));
        precio.getStyle().set("font-weight", "600").set("color", "#344f1f").set("min-width", "100px").set("text-align", "right");

        Button borrarBtn = new Button(new Icon(VaadinIcon.TRASH));
        borrarBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
        borrarBtn.getElement().setProperty("title", "Eliminar del carrito");
        borrarBtn.addClickListener(e -> {
            borrarBtn.setEnabled(false);
            Long productoId = item.getProducto().getId();
            carritoService.eliminarDelCarrito(productoId)
                .thenCompose(v -> carritoService.getCarro())
                .thenAccept(carro -> getUI().ifPresent(ui -> ui.access(() -> {
                    carroActual = carro;
                    renderizar(carro);
                    mostrarNotificacionExito("Producto eliminado del carrito");
                })))
                .exceptionally(ex -> {
                    getUI().ifPresent(ui -> ui.access(() -> {
                        mostrarNotificacionError("No se pudo eliminar: " + ex.getMessage());
                        borrarBtn.setEnabled(true);
                    }));
                    return null;
                });
        });
        
        fila.add(nombre, cantidad, precio, borrarBtn);
        fila.getStyle().set("padding", "12px 0").set("border-bottom", "1px solid #e0e0e0");
        
        return fila;
    }

    private Div crearResumen(BigDecimal total) {
        Div resumenDiv = new Div();
        resumenDiv.getStyle().set("padding", "1rem 0");

        HorizontalLayout fila = new HorizontalLayout();
        fila.setWidthFull();
        fila.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Span label = new Span("TOTAL:");
        label.getStyle().set("font-weight", "600").set("font-size", "1.1rem");

        Span valor = new Span(String.format("%.2f €", total));
        valor.getStyle().set("font-weight", "700").set("color", "#344f1f").set("font-size", "1.2rem");

        fila.add(label, valor);
        resumenDiv.add(fila);

        return resumenDiv;
    }

    private VerticalLayout crearBotonesAccion() {
        VerticalLayout botones = new VerticalLayout();
        botones.setSpacing(true);
        botones.setPadding(false);
        botones.setWidthFull();
        botones.getStyle().set("margin-top", "1.5rem");

        Button pagarBtn = new Button("Proceder al pago", new Icon(VaadinIcon.CREDIT_CARD));
        pagarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        pagarBtn.setWidth("100%");
        pagarBtn.addClickListener(e -> mostrarModalEntrega());

        Button continuarBtn = new Button("Continuar comprando", new Icon(VaadinIcon.SHOP));
        continuarBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_LARGE);
        continuarBtn.setWidth("100%");
        continuarBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("/productos")));

        botones.add(pagarBtn, continuarBtn);
        return botones;
    }

    private BigDecimal calcularSubtotal(Carro carro) {
        BigDecimal subtotal = BigDecimal.ZERO;
        if (carro.getItems() != null) {
            for (ItemCarro item : carro.getItems()) {
                if (item.getProducto() != null) {
                    subtotal = subtotal.add(
                        item.getProducto().getPrecio()
                            .multiply(BigDecimal.valueOf(item.getCantidad()))
                    );
                }
            }
        }
        return subtotal;
    }

    private void mostrarModalEntrega() {
        Dialog modal = new Dialog();
        modal.setHeaderTitle("Tipo de Entrega");
        modal.setWidth("420px");
        
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        Paragraph descripcion = new Paragraph("¿Cómo deseas recibir tu pedido?");
        descripcion.getStyle().set("margin-top", "0");
        
        RadioButtonGroup<Boolean> tipoEntrega = new RadioButtonGroup<>();
        tipoEntrega.setItems(true, false);
        tipoEntrega.setItemLabelGenerator(item -> 
            item ? "Recogida en tienda" : "Entrega a domicilio"
        );
        tipoEntrega.setValue(true);
        tipoEntrega.setWidth("100%");

        content.add(descripcion, tipoEntrega);
        modal.add(content);

        Button confirmarBtn = new Button("Confirmar", e -> {
            tipoEntregaSeleccionado = tipoEntrega.getValue();
            modal.close();
            crearPedido();
        });
        confirmarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button cancelarBtn = new Button("Cancelar", e -> modal.close());
        
        HorizontalLayout botones = new HorizontalLayout(cancelarBtn, confirmarBtn);
        botones.setSpacing(true);
        modal.getFooter().add(botones);
        
        modal.open();
    }

    private void crearPedido() {
        mostrarProcesando();
        
        Usuario usuarioActual = currentUserService.getCurrentUsuario();
        
        if (usuarioActual == null) {
            mostrarNotificacionError("Debes estar autenticado");
            getUI().ifPresent(ui -> ui.navigate("/login"));
            return;
        }

        getUI().ifPresent(ui -> {
            try {
                Pedido pedido = construirYGuardarPedido(usuarioActual);
                finalizarPedido(pedido, ui);
            } catch (Exception ex) {
                manejarErrorPedido(ex);
            }
        });
    }

    private void mostrarProcesando() {
        mainLayout.removeAll();
        VerticalLayout procesandoLayout = new VerticalLayout();
        procesandoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        procesandoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        procesandoLayout.setHeight("400px");
        procesandoLayout.setWidthFull();
        
        ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setWidth("200px");
        
        procesandoLayout.add(
            progress,
            new H2("Procesando tu pedido..."),
            new Paragraph("Por favor, espera mientras guardamos tu compra.")
        );
        
        mainLayout.add(procesandoLayout);
    }

    private Pedido construirYGuardarPedido(Usuario usuario) throws Exception {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEsRecogida(tipoEntregaSeleccionado);
        pedido.setPrecio(calcularSubtotal(carroActual));
        
        EstadoPedido estado = estadoPedidoRepository.findByNombre("En preparación")
            .orElseThrow(() -> new Exception("Estado 'En preparación' no encontrado"));
        pedido.setEstado(estado);
        
        List<LineaPedido> lineas = construirLineasPedido(pedido);
        pedido.setLineas(lineas);
        
        return pedidoService.create(pedido);
    }

    private List<LineaPedido> construirLineasPedido(Pedido pedido) {
        List<LineaPedido> lineas = new ArrayList<>();
        
        if (carroActual.getItems() != null) {
            for (ItemCarro item : carroActual.getItems()) {
                if (item.getProducto() != null) {
                    LineaPedido linea = new LineaPedido();
                    linea.setProducto(item.getProducto());
                    linea.setCantidad(item.getCantidad());
                    
                    BigDecimal precioUnitario = item.getProducto().getPrecio();
                    linea.setPrecioUnitario(precioUnitario);
                    linea.setSubtotal(
                        precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()))
                    );
                    linea.setPedido(pedido);
                    lineas.add(linea);
                }
            }
        }
        
        return lineas;
    }

    private void finalizarPedido(Pedido pedido, com.vaadin.flow.component.UI ui) {
        ui.access(() -> {
            mostrarNotificacionExito("¡Pedido creado exitosamente!");
            
            carritoService.vaciarCarrito().thenAccept(v -> {
                ui.access(() -> {
                    ui.navigate("/");
                });
            }).exceptionally(ex -> {
                System.err.println("Error al vaciar carrito: " + ex.getMessage());
                ui.access(() -> ui.navigate("/"));
                return null;
            });
        });
    }

    private void manejarErrorPedido(Exception ex) {
        getUI().ifPresent(ui -> ui.access(() -> {
            mostrarNotificacionError("Error: " + ex.getMessage());
            renderizar(carroActual);
        }));
    }

    private void mostrarNotificacionExito(String mensaje) {
        Notification notification = new Notification(mensaje);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }

    private void mostrarNotificacionError(String mensaje) {
        Notification notification = new Notification(mensaje);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(5000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }

    private void mostrarError(String mensaje) {
        mainLayout.removeAll();
        mainLayout.add(
            new H2("Error"),
            new Paragraph(mensaje)
        );
    }
}
