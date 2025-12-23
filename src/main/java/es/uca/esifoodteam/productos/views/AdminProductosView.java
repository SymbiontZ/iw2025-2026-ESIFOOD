package es.uca.esifoodteam.productos.views;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.productos.models.Producto;
import es.uca.esifoodteam.productos.repositories.ProductoRepository;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;

@Route("admin/productos")
@PageTitle("Gestión Productos | ESIFOOD")
public class AdminProductosView extends VerticalLayout {

    private final ProductoRepository productoRepository;
    private final CurrentUserService currentUserService;

    private Grid<Producto> grid;
    private Dialog dialogEditar;
    private Dialog dialogVer;
    private BeanValidationBinder<Producto> binder;
    private Producto productoEditando = null;
    private TextField precioField;

    // Buscador
    private TextField buscador;
    private Button btnBuscar;
    private Button btnLimpiar;
    private List<Producto> todosLosProductos;
    private HorizontalLayout buscadorLayout;

    public AdminProductosView(ProductoRepository productoRepository,
                             CurrentUserService currentUserService) {
        this.productoRepository = productoRepository;
        this.currentUserService = currentUserService;

        if (!isAdministrador()) {
            Notification.show("Acceso denegado. Solo administradores.", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("/"));
            return;
        }

        crearUI();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        cargarGrid();
    }

    private void cargarGrid() {
        todosLosProductos = productoRepository.findAll();
        grid.setItems(todosLosProductos);
    }

    private void crearUI() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        H2 header = new H2("🍔 Gestión de Productos");
        header.addClassName("admin-header");

        Button btnAgregar = new Button("➕ Agregar Nuevo Producto", new Icon(VaadinIcon.PLUS_CIRCLE));
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnAgregar.addClickListener(e -> abrirDialogCrear());

        crearBuscadorConBotones();

        HorizontalLayout controles = new HorizontalLayout(btnAgregar, buscadorLayout);
        controles.setJustifyContentMode(JustifyContentMode.BETWEEN);
        controles.setWidthFull();
        controles.setPadding(true);
        controles.setSpacing(true);

        grid = new Grid<>(Producto.class, false);
        configurarGrid();

        crearDialogEditar();
        crearDialogVer();

        add(header, controles, grid, dialogEditar, dialogVer);
    }

    private void crearBuscadorConBotones() {
        buscador = new TextField();
        buscador.setPlaceholder("Buscar por nombre...");
        buscador.setWidth("300px");

        btnBuscar = new Button("🔍 Buscar", e -> ejecutarBusqueda());
        btnBuscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnLimpiar = new Button("🗑️ Limpiar", e -> limpiarBusqueda());
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        buscador.addKeyPressListener(Key.ENTER, e -> ejecutarBusqueda());

        buscadorLayout = new HorizontalLayout(buscador, btnBuscar, btnLimpiar);
        buscadorLayout.setSpacing(true);
        buscadorLayout.setAlignItems(Alignment.CENTER);
    }

    private void configurarGrid() {
        grid.setHeight("500px");

        grid.addColumn(Producto::getNombre).setHeader("Nombre");
        grid.addColumn(Producto::getDescripcion).setHeader("Descripción");
        grid.addColumn(Producto::getPrecio).setHeader("Precio");
        grid.addColumn(Producto::isDisponible).setHeader("Disponible");
        
        grid.addComponentColumn(this::crearBotonesAccion).setHeader("Acciones");
    }

    private HorizontalLayout crearBotonesAccion(Producto producto) {
        HorizontalLayout acciones = new HorizontalLayout();

        Button btnVer = new Button(new Icon(VaadinIcon.EYE));
        btnVer.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnVer.addClickListener(e -> abrirDialogVer(producto));

        Button btnEditar = new Button(new Icon(VaadinIcon.EDIT));
        btnEditar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_TERTIARY);
        btnEditar.addClickListener(e -> abrirDialogEditar(producto));

        Button btnEliminar = new Button(new Icon(VaadinIcon.TRASH));
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        btnEliminar.addClickListener(e -> eliminarProducto(producto));

        acciones.add(btnVer, btnEditar, btnEliminar);
        acciones.setSpacing(true);
        return acciones;
    }

    private void crearDialogEditar() {
        dialogEditar = new Dialog();
        dialogEditar.setHeaderTitle("✏️ Editar Producto");
        dialogEditar.setWidth("500px");

        TextField nombreField = new TextField("Nombre");
        TextField descripcionField = new TextField("Descripción");
        precioField = new TextField("Precio (€)");
        precioField.setPrefixComponent(new Paragraph("€"));
        Checkbox disponibleField = new Checkbox("Disponible");

        binder = new BeanValidationBinder<>(Producto.class);
        binder.bind(nombreField, "nombre");
        binder.bind(descripcionField, "descripcion");
        binder.bind(precioField, "precio");
        binder.bind(disponibleField, "disponible");

        Button btnGuardar = new Button("💾 Guardar", e -> guardarProducto());
        Button btnCancelar = new Button("❌ Cancelar", e -> {
            dialogEditar.close();
            productoEditando = null;
        });

        VerticalLayout form = new VerticalLayout(
            nombreField, descripcionField, precioField, disponibleField,
            new HorizontalLayout(btnGuardar, btnCancelar)
        );
        form.setSpacing(true);
        form.setPadding(true);

        dialogEditar.add(form);
    }

    private void crearDialogVer() {
        dialogVer = new Dialog();
        dialogVer.setHeaderTitle("👁️ Ver Producto");
        dialogVer.setWidth("450px");
    }

    private void abrirDialogCrear() {
        productoEditando = null;
        binder.readBean(new Producto());
        precioField.focus();
        dialogEditar.setHeaderTitle("➕ Nuevo Producto");
        dialogEditar.open();
    }

    private void abrirDialogEditar(Producto producto) {
        productoEditando = producto;
        binder.readBean(producto);
        dialogEditar.setHeaderTitle("✏️ Editar " + producto.getNombre());
        dialogEditar.open();
    }

    private void abrirDialogVer(Producto producto) {
        dialogVer.removeAll();

        VerticalLayout detalles = new VerticalLayout();
        detalles.setSpacing(true);
        detalles.setPadding(true);

        detalles.add(new H3(producto.getNombre()));
        detalles.add(new Paragraph("Descripción: " + (producto.getDescripcion() != null ? producto.getDescripcion() : "-")));
        detalles.add(new Paragraph("Precio: " + producto.getPrecio() + " €"));
        detalles.add(new Paragraph("Estado: " + (producto.isDisponible() ? "✅ Activo" : "❌ Inactivo")));
        detalles.add(new Paragraph("Creado: " + (producto.getCreatedDate() != null ? producto.getCreatedDate().toString() : "-")));
        detalles.add(new Paragraph("Por: " + (producto.getCreatedBy() != null ? producto.getCreatedBy() : "-")));

        dialogVer.add(detalles);
        dialogVer.open();
    }

    private void guardarProducto() {
        try {
            if (productoEditando == null) {
                Producto nuevoProducto = new Producto();
                binder.writeBean(nuevoProducto);

                if (nuevoProducto.getNombre() == null || nuevoProducto.getNombre().trim().isEmpty()) {
                    Notification.show("❌ El nombre es obligatorio", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (nuevoProducto.getPrecio() == null || nuevoProducto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                    Notification.show("❌ El precio debe ser mayor que 0", 3000, Notification.Position.MIDDLE);
                    return;
                }

                productoRepository.save(nuevoProducto);
                Notification.show("✅ Producto creado correctamente", 3000, Notification.Position.MIDDLE);
            } else {
                binder.writeBean(productoEditando);
                productoRepository.save(productoEditando);
                Notification.show("✅ Producto actualizado correctamente", 2000, Notification.Position.MIDDLE);
            }

            dialogEditar.close();
            productoEditando = null;
            todosLosProductos = productoRepository.findAll();
            grid.setItems(todosLosProductos);
        } catch (ValidationException e) {
            Notification.show("❌ Datos inválidos", 4000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification.show("❌ Error: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }

    private void ejecutarBusqueda() {
        String filtro = buscador.getValue().trim();
        if (filtro.isEmpty()) {
            Notification.show("⚠️ Ingresa un término para buscar", 2000, Notification.Position.MIDDLE);
            return;
        }

        List<Producto> filtrados = todosLosProductos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        grid.setItems(filtrados);
        Notification.show("✅ Encontrados " + filtrados.size() + " resultado(s)", 2000, Notification.Position.TOP_CENTER);
    }

    private void limpiarBusqueda() {
        buscador.clear();
        grid.setItems(todosLosProductos);
        Notification.show("🔄 Búsqueda limpiada", 1500, Notification.Position.TOP_CENTER);
    }

    private void eliminarProducto(Producto producto) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("⚠️ Confirmar Eliminación");
        confirmDialog.setWidth("450px");

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);

        content.add(new Paragraph("¿Estás seguro de que quieres **eliminar** el producto?"));
        content.add(new H3(producto.getNombre()));

        Button btnConfirmar = new Button("✅ Sí, eliminar producto", e -> {
            try {
                productoRepository.delete(producto);
                Notification.show("✅ Producto '" + producto.getNombre() + "' eliminado", 3000, Notification.Position.MIDDLE);
                todosLosProductos = productoRepository.findAll();
                grid.setItems(todosLosProductos);
            } catch (Exception ex) {
                Notification.show("❌ Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
            confirmDialog.close();
        });
        btnConfirmar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button btnCancelar = new Button("❌ Cancelar", e -> confirmDialog.close());
        btnCancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttons = new HorizontalLayout(btnConfirmar, btnCancelar);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(JustifyContentMode.END);

        content.add(buttons);
        confirmDialog.add(content);
        confirmDialog.open();
    }

    private boolean isAdministrador() {
        try {
            return currentUserService.getCurrentUsuario() != null &&
                   currentUserService.getCurrentUsuario().getTipo_id() != null &&
                   "ADMINISTRADOR".equalsIgnoreCase(currentUserService.getCurrentUsuario().getTipo_id().getNombre());
        } catch (Exception e) {
            System.err.println("Error verificando admin: " + e.getMessage());
            return false;
        }
    }
}