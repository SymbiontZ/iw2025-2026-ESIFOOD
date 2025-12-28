package es.uca.esifoodteam.pedidos.views;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.pedidos.models.EstadoPedido;
import es.uca.esifoodteam.pedidos.models.LineaPedido;
import es.uca.esifoodteam.pedidos.models.Pedido;
import es.uca.esifoodteam.pedidos.repositories.EstadoPedidoRepository;
import es.uca.esifoodteam.pedidos.services.PedidoService;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;

@Route("admin/pedidos")
@PageTitle("Pedidos | Admin")
public class AdminPedidosView extends MainLayout {

    private final PedidoService pedidoService;
    private final EstadoPedidoRepository estadoPedidoRepository;
    private final CurrentUserService currentUserService;  // ✅ IGUAL

    private final Grid<Pedido> grid = new Grid<>(Pedido.class, false);
    private final Grid<LineaPedido> lineasGrid = new Grid<>(LineaPedido.class, false);
    private final BeanValidationBinder<Pedido> binder = new BeanValidationBinder<>(Pedido.class);
    private Pedido pedidoEditando;
    private Pedido pedidoInfo;

    // Campos de formulario
    private final TextField precioField = new TextField("Precio (€)");
    private final TextField fechaField = new TextField("Fecha (yyyy-MM-dd HH:mm)");
    private final ComboBox<EstadoPedido> estadoField = new ComboBox<>("Estado");
    private final TextArea observacionesField = new TextArea("Observaciones");

    private final Button guardar = new Button("Actualizar");
    private final Button cancelar = new Button("Cancelar");
    private final Button masInfoBtn = new Button("Más Info");

    public AdminPedidosView(PedidoService pedidoService,
                            EstadoPedidoRepository estadoPedidoRepository,
                            CurrentUserService currentUserService) {  // ✅ IGUAL
        this.pedidoService = pedidoService;
        this.estadoPedidoRepository = estadoPedidoRepository;
        this.currentUserService = currentUserService;

        // ✅ EXACTAMENTE IGUAL que AdminEstablecimientoView
        if (!tienePermisos()) {
            Notification.show("Acceso denegado", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("/"));
            return;
        }

        // VerticalLayout para CONTENIDO (NO principal)
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);

        H2 titulo = new H2("📦 Gestión de Pedidos");
        content.add(titulo);

        configurarGridPedidos();
        configurarGridLineas();
        configurarFormulario();

        // Barra con botón Más Info
        HorizontalLayout barra = new HorizontalLayout(masInfoBtn);
        barra.setWidthFull();
        barra.setSpacing(true);
        content.add(barra);

        // Grids
        VerticalLayout seccionGrids = new VerticalLayout(grid, lineasGrid);
        seccionGrids.setSizeFull();
        seccionGrids.setPadding(false);
        seccionGrids.setSpacing(true);
        seccionGrids.setHeight("100%");

        // FORMULARIO
        VerticalLayout formulario = crearLayoutFormulario();
        formulario.setWidth("400px");
        formulario.setMinWidth("400px");
        formulario.setPadding(true);
        formulario.setSpacing(true);
        formulario.getStyle().set("border", "1px solid #ccc");
        formulario.getStyle().set("border-radius", "4px");

        // Layout principal
        HorizontalLayout contenido = new HorizontalLayout(seccionGrids, formulario);
        contenido.setSizeFull();
        contenido.setSpacing(true);
        contenido.setFlexGrow(2, seccionGrids);
        contenido.setFlexGrow(1, formulario);

        content.add(contenido);
        
        add(content);
        
        limpiarFormulario();
    }

    // ✅ EXACTAMENTE IGUAL que AdminEstablecimientoView
    private boolean tienePermisos() {
        try {
            Usuario user = currentUserService.getCurrentUsuario();
            if (user == null || user.getTipo_id() == null) {
                return false;
            }
            String tipo = user.getTipo_id().getNombre();
            return tipo.equalsIgnoreCase("ADMINISTRADOR") || tipo.equalsIgnoreCase("ENCARGADO");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        refrescarGrid();
    }

    // ✅ TODO EL RESTO DEL CÓDIGO IGUAL...
    private void configurarGridPedidos() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("350px");

        grid.addColumn(Pedido::getId).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Pedido::getPrecio).setHeader("Precio (€)").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(p -> p.getFechaHora() != null
                ? p.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "-")
                .setHeader("Fecha/Hora").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(p -> p.getEstado() != null ? p.getEstado().getNombre() : "-")
                .setHeader("Estado").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(Pedido::getObservaciones)
                .setHeader("Observaciones").setAutoWidth(true).setFlexGrow(2);

        grid.asSingleSelect().addValueChangeListener(e -> {
            Pedido pedido = e.getValue();
            if (pedido != null && pedido.getId() != null) {
                editarPedido(pedido);
                pedidoInfo = pedido;
                if (pedido.getLineas() != null) {
                    lineasGrid.setItems(pedido.getLineas());
                } else {
                    lineasGrid.setItems();
                }
                masInfoBtn.setEnabled(true);
            } else {
                limpiarFormulario();
                lineasGrid.setItems();
                pedidoInfo = null;
                masInfoBtn.setEnabled(false);
            }
        });
    }

    private void configurarGridLineas() {
        lineasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        lineasGrid.setHeight("250px");

        lineasGrid.addColumn(lp -> lp.getProducto() != null ? lp.getProducto().getNombre() : "-")
                .setHeader("Producto").setAutoWidth(true).setFlexGrow(2);
        lineasGrid.addColumn(LineaPedido::getCantidad).setHeader("Cantidad").setAutoWidth(true).setFlexGrow(0);
        lineasGrid.addColumn(LineaPedido::getPrecioUnitario).setHeader("Precio unitario").setAutoWidth(true).setFlexGrow(1);
        lineasGrid.addColumn(LineaPedido::getSubtotal).setHeader("Total línea").setAutoWidth(true).setFlexGrow(1);
    }

    private VerticalLayout crearLayoutFormulario() {
        VerticalLayout formLayout = new VerticalLayout();
        
        H2 tituloForm = new H2("Editar Pedido");
        tituloForm.getStyle().set("margin-bottom", "1rem");
        formLayout.add(tituloForm);
        
        Div info = new Div();
        info.setText("Selecciona un pedido para editar");
        info.getStyle().set("color", "#666");
        info.getStyle().set("font-style", "italic");
        info.getStyle().set("margin-bottom", "1rem");
        formLayout.add(info);
        
        formLayout.add(precioField);
        formLayout.add(fechaField);
        formLayout.add(estadoField);
        formLayout.add(observacionesField);
        
        HorizontalLayout botones = new HorizontalLayout(guardar, cancelar);
        botones.setSpacing(true);
        formLayout.add(botones);
        
        formLayout.setWidth("100%");
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        return formLayout;
    }

    private void configurarFormulario() {
        estadoField.setItems(estadoPedidoRepository.findAll());
        estadoField.setItemLabelGenerator(EstadoPedido::getNombre);
        estadoField.setWidthFull();

        precioField.setWidthFull();
        binder.bind(precioField, 
            pedido -> pedido.getPrecio() != null ? pedido.getPrecio().toString() : "",
            (pedido, valorTexto) -> {}
        );

        fechaField.setWidthFull();
        binder.forField(fechaField)
            .withConverter(
                v -> {
                    try {
                        if (v == null || v.trim().isEmpty()) return null;
                        return LocalDateTime.parse(v.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    } catch (Exception ex) {
                        throw new RuntimeException("Formato de fecha inválido");
                    }
                },
                v -> v != null ? v.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : ""
            )
            .bind(Pedido::getFechaHora, Pedido::setFechaHora);

        observacionesField.setWidthFull();
        binder.bind(estadoField, "estado");
        binder.bind(observacionesField, "observaciones");

        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.setEnabled(false);
        guardar.addClickListener(e -> guardar());
        cancelar.addClickListener(e -> cancelarEdicion());
        
        masInfoBtn.setEnabled(false);
        masInfoBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        masInfoBtn.addClickListener(e -> mostrarInfoAuditoria());
    }

    private void refrescarGrid() {
        List<Pedido> pedidos = pedidoService.findAll();
        grid.setItems(pedidos);
        lineasGrid.setItems();
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        pedidoEditando = null;
        pedidoInfo = null;
        binder.readBean(new Pedido());
        guardar.setEnabled(false);
        cancelar.setEnabled(false);
        masInfoBtn.setEnabled(false);
        precioField.clear();
        fechaField.clear();
        estadoField.clear();
        observacionesField.clear();
    }

    private void editarPedido(Pedido pedido) {
        pedidoEditando = pedido;
        binder.readBean(pedidoEditando);
        guardar.setEnabled(true);
        cancelar.setEnabled(true);
    }

    private void mostrarInfoAuditoria() {
        if (pedidoInfo == null) return;
        
        Span infoText = new Span();
        StringBuilder sb = new StringBuilder();
        sb.append("📋 Pedido #" + pedidoInfo.getId() + "\n\n");
        
        if (pedidoInfo.getUsuario() != null) {
            sb.append("👤 Usuario ID: " + pedidoInfo.getUsuario().getId() + "\n");
        }
        if (pedidoInfo.getCreatedDate() != null) {
            LocalDateTime createdLocal = LocalDateTime.ofInstant(pedidoInfo.getCreatedDate(), ZoneId.systemDefault());
            sb.append("📅 Creado: " + createdLocal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n");
        }
        if (pedidoInfo.getModifiedDate() != null) {
            LocalDateTime modifiedLocal = LocalDateTime.ofInstant(pedidoInfo.getModifiedDate(), ZoneId.systemDefault());
            sb.append("✏️ Modificado: " + modifiedLocal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n");
        }
        if (pedidoInfo.getCreatedBy() != null) {
            sb.append("👨‍💻 Creado por: " + pedidoInfo.getCreatedBy() + "\n");
        }
        if (pedidoInfo.getModifiedBy() != null) {
            sb.append("👨‍💻 Modificado por: " + pedidoInfo.getModifiedBy() + "\n");
        }
        sb.append("📦 Líneas: " + (pedidoInfo.getLineas() != null ? pedidoInfo.getLineas().size() : 0));
        if (pedidoInfo.getActualizaciones() != null) {
            sb.append("\n🔄 Actualizaciones: " + pedidoInfo.getActualizaciones().size());
        }
        
        infoText.setText(sb.toString());
        infoText.getElement().getStyle().set("white-space", "pre-line");
        
        Notification notification = new Notification(infoText);
        notification.setDuration(10000);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

    private void guardar() {
        if (pedidoEditando == null || pedidoEditando.getId() == null) {
            Notification.show("Selecciona un pedido para actualizar", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            Usuario usuarioOriginal = pedidoEditando.getUsuario();
            binder.writeBean(pedidoEditando);

            if (pedidoEditando.getUsuario() == null) {
                pedidoEditando.setUsuario(usuarioOriginal);
            }

            String textoPrecio = precioField.getValue().trim();
            if (textoPrecio.isEmpty()) {
                precioField.setInvalid(true);
                precioField.setErrorMessage("El precio es obligatorio");
                Notification.show("El precio es obligatorio", 3000, Notification.Position.MIDDLE);
                return;
            }
            
            BigDecimal precio;
            try {
                precio = new BigDecimal(textoPrecio);
                pedidoEditando.setPrecio(precio);
                precioField.setInvalid(false);
            } catch (NumberFormatException nfe) {
                precioField.setInvalid(true);
                precioField.setErrorMessage("Precio inválido (ej: 12.34)");
                Notification.show("Precio inválido", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (pedidoEditando.getFechaHora() == null) {
                pedidoEditando.setFechaHora(LocalDateTime.now());
            }

            pedidoService.update(pedidoEditando.getId(), pedidoEditando);
            Notification.show("Pedido actualizado", 3000, Notification.Position.MIDDLE);

            limpiarFormulario();
            refrescarGrid();
        } catch (ValidationException ex) {
            Notification.show("Datos inválidos: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
        } catch (Exception ex) {
            Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void cancelarEdicion() {
        limpiarFormulario();
        grid.asSingleSelect().clear();
    }
}