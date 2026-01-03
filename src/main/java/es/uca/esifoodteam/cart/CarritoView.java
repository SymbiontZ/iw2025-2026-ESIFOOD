package es.uca.esifoodteam.cart;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        mainLayout.setPadding(true);
        mainLayout.setSpacing(true);
        add(mainLayout);

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

        mainLayout.add(crearHeader());
        mainLayout.add(new Hr());
        mainLayout.add(crearSeccionProductos(carro));
        mainLayout.add(new Hr());
        mainLayout.add(crearSeccionResumen(carro));
        mainLayout.add(crearBotonesAccion());
    }

    private H1 crearHeader() {
        H1 titulo = new H1("🛒 Mi Carrito");
        titulo.getStyle().set("margin-top", "0");
        return titulo;
    }

    private void mostrarCarritoVacio() {
        mainLayout.add(crearHeader());
        VerticalLayout vacio = new VerticalLayout();
        vacio.setAlignItems(FlexComponent.Alignment.CENTER);
        vacio.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vacio.setHeight("400px");
        
        vacio.add(
            new H2("El carrito está vacío"),
            new Paragraph("No hay productos en tu carrito"),
            new Anchor("/productos", new Button("🛍️ Ver Productos", e -> {}))
        );
        mainLayout.add(vacio);
    }

    private VerticalLayout crearSeccionProductos(Carro carro) {
        VerticalLayout seccion = new VerticalLayout();
        seccion.setPadding(false);
        seccion.setSpacing(true);
        
        if (carro.getItems() != null) {
            for (ItemCarro item : carro.getItems()) {
                if (item.getProducto() != null) {
                    seccion.add(crearFilaProducto(item));
                }
            }
        }
        return seccion;
    }

    private HorizontalLayout crearFilaProducto(ItemCarro item) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.setAlignItems(FlexComponent.Alignment.CENTER);
        fila.setWidthFull();
        
        // Nombre del producto
        Span nombre = new Span(item.getProducto().getNombre());
        nombre.getStyle().set("font-weight", "500");
        nombre.setWidth("40%");
        
        // Cantidad
        Span cantidad = new Span("x" + item.getCantidad());
        cantidad.setWidth("15%");
        cantidad.getStyle().set("text-align", "center");
        
        // Subtotal
        BigDecimal subtotal = item.getProducto().getPrecio()
            .multiply(BigDecimal.valueOf(item.getCantidad()));
        Span precio = new Span(String.format("%.2f €", subtotal));
        precio.getStyle().set("font-weight", "600");
        precio.getStyle().set("color", "#2e7d32");
        precio.setWidth("25%");
        precio.getStyle().set("text-align", "right");
        
        fila.add(nombre, cantidad, precio);
        fila.getStyle().set("padding", "12px 0");
        fila.getStyle().set("border-bottom", "1px solid #e0e0e0");
        
        return fila;
    }

    private VerticalLayout crearSeccionResumen(Carro carro) {
        VerticalLayout seccion = new VerticalLayout();
        seccion.setPadding(false);
        seccion.setSpacing(true);
        
        BigDecimal total = calcularSubtotal(carro);

        VerticalLayout totalLayout = new VerticalLayout(
            crearFilaResumen("TOTAL:", String.format("%.2f €", total))
        );
        totalLayout.getStyle().set("padding-top", "12px");
        totalLayout.addClassName("carrito-total-fila");
        seccion.add(totalLayout);
        
        return seccion;
    }

    private HorizontalLayout crearFilaResumen(String label, String valor) {
        HorizontalLayout fila = new HorizontalLayout();
        fila.setWidthFull();
        fila.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        
        Span labelSpan = new Span(label);
        Span valorSpan = new Span(valor);
        valorSpan.getStyle().set("font-weight", "600");
        
        fila.add(labelSpan, valorSpan);
        return fila;
    }

    private HorizontalLayout crearBotonesAccion() {
        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setSpacing(true);
        
        Button continuarBtn = new Button("Continuar comprando", e -> 
            getUI().ifPresent(ui -> ui.navigate("/productos"))
        );
        continuarBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        continuarBtn.setWidth("50%");
        
        Button pagarBtn = new Button("💳 Proceder al pago", e -> mostrarModalEntrega());
        pagarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        pagarBtn.setWidth("50%");
        
        botones.add(continuarBtn, pagarBtn);
        botones.getStyle().set("margin-top", "24px");
        
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
        modal.setHeaderTitle("📦 Tipo de Entrega");
        modal.setWidth("400px");
        
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        Paragraph descripcion = new Paragraph("¿Cómo deseas recibir tu pedido?");
        
        RadioButtonGroup<Boolean> tipoEntrega = new RadioButtonGroup<>();
        tipoEntrega.setItems(true, false);
        tipoEntrega.setItemLabelGenerator(item -> 
            item ? "🏪 Recogida en tienda" : "🚚 Entrega a domicilio"
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
        // Actualizar pantalla a "procesando"
        mostrarProcesando();
        
        // Capturar usuario ANTES del thread
        Usuario usuarioActual = currentUserService.getCurrentUsuario();
        
        if (usuarioActual == null) {
            mostrarNotificacionError("Debes estar autenticado");
            getUI().ifPresent(ui -> ui.navigate("/login"));
            return;
        }

        // Crear pedido en background sin usar Thread
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
        
        ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(true);
        
        procesandoLayout.add(
            progress,
            new H2("⏳ Procesando tu pedido..."),
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
            
            // Guardar carrito vacío y esperar a que se complete
            carritoService.vaciarCarrito().thenAccept(v -> {
                ui.access(() -> {
                    // Navegar a la página de inicio
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
            new H2("❌ Error"),
            new Paragraph(mensaje)
        );
    }
}
