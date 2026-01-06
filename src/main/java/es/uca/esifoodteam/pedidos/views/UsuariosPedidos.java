package es.uca.esifoodteam.pedidos.views;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.services.PedidoService;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;

@Route("pedidos")
@PageTitle("Mis Pedidos | ESIFOOD")
public class UsuariosPedidos extends MainLayout {

    private final PedidoService pedidoService;
    private final CurrentUserService currentUserService;

    private final Grid<Pedido> gridUltimos = new Grid<>(Pedido.class, false);
    private final Grid<Pedido> gridHistorial = new Grid<>(Pedido.class, false);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public UsuariosPedidos(PedidoService pedidoService, CurrentUserService currentUserService) {
        this.pedidoService = pedidoService;
        this.currentUserService = currentUserService;

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);

        // Título
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setAlignItems(Alignment.CENTER);
        Icon titleIcon = new Icon(VaadinIcon.BOOK_DOLLAR);
        titleIcon.setSize("32px");
        titleIcon.getStyle().set("color", "#344f1f");
        H2 titulo = new H2("Mis Pedidos");
        titulo.getStyle().set("margin", "0").set("color", "#344f1f");
        titleLayout.add(titleIcon, titulo);
        content.add(titleLayout);

        // Sección de ÚLTIMOS PEDIDOS
        HorizontalLayout subtituloUltimosLayout = new HorizontalLayout();
        subtituloUltimosLayout.setAlignItems(Alignment.CENTER);
        Icon ultimosIcon = new Icon(VaadinIcon.FIRE);
        ultimosIcon.setSize("20px");
        ultimosIcon.getStyle().set("color", "#f39c12");
        H3 subtituloUltimos = new H3("Últimos Pedidos");
        subtituloUltimos.getStyle().set("margin", "0").set("color", "#344f1f");
        subtituloUltimosLayout.add(ultimosIcon, subtituloUltimos);
        content.add(subtituloUltimosLayout);

        configurarGridUltimos();
        content.add(gridUltimos);

        // Divisor visual
        Div divisor = new Div();
        divisor.getStyle()
                .set("border-top", "2px solid #e0e0e0")
                .set("margin", "20px 0");
        content.add(divisor);

        // Sección de HISTORIAL COMPLETO
        HorizontalLayout subtituloHistorialLayout = new HorizontalLayout();
        subtituloHistorialLayout.setAlignItems(Alignment.CENTER);
        Icon historialIcon = new Icon(VaadinIcon.TIME_BACKWARD);
        historialIcon.setSize("20px");
        historialIcon.getStyle().set("color", "#3498db");
        H3 subtituloHistorial = new H3("Historial Completo");
        subtituloHistorial.getStyle().set("margin", "0").set("color", "#344f1f");
        subtituloHistorialLayout.add(historialIcon, subtituloHistorial);
        content.add(subtituloHistorialLayout);

        configurarGridHistorial();
        content.add(gridHistorial);

        setContent(content);
    }

    private void configurarGridUltimos() {
        gridUltimos.setHeight("400px");
        gridUltimos.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        gridUltimos.getStyle().set("border-radius", "8px").set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");

        // Columnas
        gridUltimos.addColumn(p -> "#" + p.getId()).setHeader("ID").setWidth("70px").setFlexGrow(0);
        gridUltimos.addColumn(p -> p.getFechaHora().format(formatter))
                .setHeader("Fecha y Hora")
                .setWidth("180px")
                .setFlexGrow(0);
        gridUltimos.addColumn(p -> p.getEstado().getNombre()).setHeader("Estado");
        gridUltimos.addColumn(p -> "€" + String.format("%.2f", p.getPrecio())).setHeader("Precio").setWidth("100px")
                .setFlexGrow(0);
        gridUltimos.addComponentColumn(p -> {
            if (p.getEsRecogida()) {
                Icon icon = new Icon(VaadinIcon.SHOP);
                icon.setSize("16px");
                Span span = new Span(" Recogida");
                HorizontalLayout hl = new HorizontalLayout(icon, span);
                hl.setAlignItems(Alignment.CENTER);
                hl.setSpacing(false);
                return hl;
            } else {
                Icon icon = new Icon(VaadinIcon.TRUCK);
                icon.setSize("16px");
                Span span = new Span(" Entrega");
                HorizontalLayout hl = new HorizontalLayout(icon, span);
                hl.setAlignItems(Alignment.CENTER);
                hl.setSpacing(false);
                return hl;
            }
        })
                .setHeader("Tipo")
                .setWidth("140px")
                .setFlexGrow(0);

        // Click para ver detalles
        gridUltimos.asSingleSelect().addValueChangeListener(e -> {
            Pedido pedido = e.getValue();
            if (pedido != null) {
                mostrarDetallesPedido(pedido);
            }
        });
    }

    private void configurarGridHistorial() {
        gridHistorial.setHeightFull();
        gridHistorial.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
        gridHistorial.getStyle().set("border-radius", "8px").set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");

        // Columnas iguales al grid de últimos
        gridHistorial.addColumn(p -> "#" + p.getId()).setHeader("ID").setWidth("70px").setFlexGrow(0);
        gridHistorial.addColumn(p -> p.getFechaHora().format(formatter))
                .setHeader("Fecha y Hora")
                .setWidth("180px")
                .setFlexGrow(0);
        gridHistorial.addColumn(p -> p.getEstado().getNombre()).setHeader("Estado");
        gridHistorial.addColumn(p -> "€" + String.format("%.2f", p.getPrecio())).setHeader("Precio")
                .setWidth("100px").setFlexGrow(0);
        gridHistorial.addComponentColumn(p -> {
            if (p.getEsRecogida()) {
                Icon icon = new Icon(VaadinIcon.SHOP);
                icon.setSize("16px");
                Span span = new Span(" Recogida");
                HorizontalLayout hl = new HorizontalLayout(icon, span);
                hl.setAlignItems(Alignment.CENTER);
                hl.setSpacing(false);
                return hl;
            } else {
                Icon icon = new Icon(VaadinIcon.TRUCK);
                icon.setSize("16px");
                Span span = new Span(" Entrega");
                HorizontalLayout hl = new HorizontalLayout(icon, span);
                hl.setAlignItems(Alignment.CENTER);
                hl.setSpacing(false);
                return hl;
            }
        })
                .setHeader("Tipo")
                .setWidth("140px")
                .setFlexGrow(0);

        // Click para ver detalles
        gridHistorial.asSingleSelect().addValueChangeListener(e -> {
            Pedido pedido = e.getValue();
            if (pedido != null) {
                mostrarDetallesPedido(pedido);
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        cargarPedidos();
    }

    private void cargarPedidos() {
        Usuario usuarioActual = currentUserService.getCurrentUsuario();

        if (usuarioActual == null) {
            mostrarMensajeVacio("No estás autenticado");
            return;
        }

        List<Pedido> misPedidos = pedidoService.findByUsuario(usuarioActual.getId());

        if (misPedidos == null || misPedidos.isEmpty()) {
            mostrarMensajeVacio("No tienes pedidos realizados");
            return;
        }

        // Ordenar de más reciente a más antiguo
        misPedidos.sort((a, b) -> b.getFechaHora().compareTo(a.getFechaHora()));

        // Últimos 5 pedidos
        List<Pedido> ultimos = misPedidos.size() <= 5 ? misPedidos : misPedidos.subList(0, 5);
        gridUltimos.setItems(ultimos);

        // Historial completo
        gridHistorial.setItems(misPedidos);
    }

    private void mostrarMensajeVacio(String mensaje) {
        VerticalLayout emptyLayout = new VerticalLayout();
        emptyLayout.setSizeFull();
        emptyLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        emptyLayout.setAlignItems(Alignment.CENTER);
        emptyLayout.setPadding(true);
        emptyLayout.setSpacing(true);

        Div mensajeDiv = new Div();
        mensajeDiv.getStyle()
                .set("text-align", "center")
                .set("color", "#999");
        H3 mensajeH3 = new H3(mensaje);
        mensajeDiv.add(mensajeH3);
        
        emptyLayout.add(mensajeDiv);
        setContent(emptyLayout);
    }

    private void mostrarDetallesPedido(Pedido pedido) {
        // Dialog de Vaadin
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Pedido #" + pedido.getId());
        dialog.setWidth("600px");
        dialog.setModal(true);

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.setPadding(true);
        dialogContent.setSpacing(true);

        // Información general
        String fechaStr = pedido.getFechaHora().format(formatter);
        String tipo = pedido.getEsRecogida() ? "Recogida" : "Entrega";
        String estado = pedido.getEstado().getNombre();
        String precio = "€" + String.format("%.2f", pedido.getPrecio());

        HorizontalLayout infoRow1 = new HorizontalLayout();
        infoRow1.setSpacing(true);
        Icon calendarIcon = new Icon(VaadinIcon.CALENDAR);
        calendarIcon.setSize("16px");
        infoRow1.add(calendarIcon, new Span("Fecha: " + fechaStr));
        infoRow1.setAlignItems(Alignment.CENTER);

        HorizontalLayout infoRow2 = new HorizontalLayout();
        infoRow2.setSpacing(true);
        Icon statusIcon = new Icon(VaadinIcon.CHECK_CIRCLE);
        statusIcon.setSize("16px");
        infoRow2.add(statusIcon, new Span("Estado: " + estado));
        infoRow2.setAlignItems(Alignment.CENTER);

        HorizontalLayout infoRow3 = new HorizontalLayout();
        infoRow3.setSpacing(true);
        Icon typeIcon = pedido.getEsRecogida() ? new Icon(VaadinIcon.SHOP) : new Icon(VaadinIcon.TRUCK);
        typeIcon.setSize("16px");
        infoRow3.add(typeIcon, new Span("Tipo: " + tipo));
        infoRow3.setAlignItems(Alignment.CENTER);

        HorizontalLayout infoRow4 = new HorizontalLayout();
        infoRow4.setSpacing(true);
        Icon priceIcon = new Icon(VaadinIcon.DOLLAR);
        priceIcon.setSize("16px");
        priceIcon.getStyle().set("color", "#27ae60");
        Span precioSpan = new Span("Precio Total: " + precio);
        precioSpan.getStyle().set("font-weight", "bold").set("color", "#27ae60");
        infoRow4.add(priceIcon, precioSpan);
        infoRow4.setAlignItems(Alignment.CENTER);

        dialogContent.add(infoRow1, infoRow2, infoRow3, infoRow4);

        if (pedido.getObservaciones() != null && !pedido.getObservaciones().isEmpty()) {
            Div observacionesDiv = new Div();
            observacionesDiv.getStyle().set("background-color", "#f0f7ff").set("padding", "12px").set("border-radius", "4px").set("border-left", "4px solid #3498db");
            Icon noteIcon = new Icon(VaadinIcon.COMMENT);
            noteIcon.setSize("16px");
            HorizontalLayout obsLayout = new HorizontalLayout(noteIcon, new Span("Observaciones: " + pedido.getObservaciones()));
            obsLayout.setAlignItems(Alignment.START);
            obsLayout.setSpacing(true);
            observacionesDiv.add(obsLayout);
            dialogContent.add(observacionesDiv);
        }

        // Líneas del pedido
        if (pedido.getLineas() != null && !pedido.getLineas().isEmpty()) {
            HorizontalLayout lineasTitleLayout = new HorizontalLayout();
            lineasTitleLayout.setAlignItems(Alignment.CENTER);
            Icon itemsIcon = new Icon(VaadinIcon.CART);
            itemsIcon.setSize("20px");
            H3 subtituloLineas = new H3("Artículos");
            subtituloLineas.getStyle().set("margin", "0");
            lineasTitleLayout.add(itemsIcon, subtituloLineas);
            dialogContent.add(lineasTitleLayout);

            Grid<es.uca.esifoodteam.pedidos.models.LineaPedido> lineasGrid = new Grid<>(
                    es.uca.esifoodteam.pedidos.models.LineaPedido.class, false);
            lineasGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
            lineasGrid.addColumn(l -> l.getProducto().getNombre()).setHeader("Producto");
            lineasGrid.addColumn(l -> String.valueOf(l.getCantidad())).setHeader("Cantidad").setWidth("80px").setFlexGrow(0);
            lineasGrid.addColumn(l -> "€" + String.format("%.2f", l.getPrecioUnitario())).setHeader("Precio Unit.").setWidth("100px").setFlexGrow(0);
            lineasGrid.setItems(pedido.getLineas());
            lineasGrid.setHeight("250px");

            dialogContent.add(lineasGrid);
        }

        dialog.add(dialogContent);

        // Botón cerrar
        Button cerrar = new Button("Cerrar");
        cerrar.setIcon(new Icon(VaadinIcon.CLOSE));
        cerrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cerrar.addClickListener(e -> {
            dialog.close();
            gridUltimos.deselectAll();
            gridHistorial.deselectAll();
        });
        dialog.getFooter().add(cerrar);

        dialog.open();
    }
}
