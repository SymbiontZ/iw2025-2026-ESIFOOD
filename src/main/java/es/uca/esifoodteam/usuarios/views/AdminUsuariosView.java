package es.uca.esifoodteam.usuarios.views;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.TipoUsuarioRepository;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;
import es.uca.esifoodteam.usuarios.services.UsuarioService;  // ✅ AÑADIDO

@Route("admin/usuarios")
@PageTitle("Gestión Usuarios | ESIFOOD")
public class AdminUsuariosView extends MainLayout {  // ✅ CAMBIADO de VerticalLayout

    private final UsuarioService usuarioService;
    private final CurrentUserService currentUserService;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    private Grid<Usuario> grid;
    private Dialog dialogEditar;
    private Dialog dialogVer;
    private ComboBox<TipoUsuario> comboTipoUsuario;

    private BeanValidationBinder<Usuario> binder;
    private Usuario usuarioEditando = null;
    private TextField passField;

    // Buscador
    private TextField buscador;
    private Button btnBuscar;
    private Button btnLimpiar;
    private List<Usuario> todosLosUsuarios;
    private HorizontalLayout buscadorLayout;

    public AdminUsuariosView(UsuarioService usuarioService,
                             CurrentUserService currentUserService,
                             TipoUsuarioRepository tipoUsuarioRepository) {
        this.usuarioService = usuarioService;
        this.currentUserService = currentUserService;
        this.tipoUsuarioRepository = tipoUsuarioRepository;

        if (!isAdministrador()) {
            Notification.show("Acceso denegado. Solo administradores.", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("/"));
            return;
        }

        // ✅ VerticalLayout para CONTENIDO (NO principal)
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);
        content.setSizeFull();

        crearUI(content);  // ✅ Pasar content al método
        add(content);      // ✅ Añadir al MainLayout
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        cargarGrid();
    }

    private void cargarGrid() {
        todosLosUsuarios = usuarioService.findAll();
        grid.setItems(todosLosUsuarios);
    }

    private void crearUI(VerticalLayout content) {  // ✅ Recibe content como parámetro
        H2 header = new H2("👥 Gestión de Usuarios");
        header.addClassName("admin-header");

        Button btnAgregar = new Button("➕ Agregar Nuevo Usuario", new Icon(VaadinIcon.PLUS_CIRCLE));
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        btnAgregar.addClickListener(e -> abrirDialogCrear());

        crearBuscadorConBotones();

        HorizontalLayout controles = new HorizontalLayout(btnAgregar, buscadorLayout);
        // ✅ SIN JustifyContentMode
        controles.setWidthFull();
        controles.setPadding(true);
        controles.setSpacing(true);

        grid = new Grid<>(Usuario.class, false);
        configurarGrid();

        crearDialogEditar();
        crearDialogVer();

        content.add(header, controles, grid, dialogEditar, dialogVer);
    }

    private void crearBuscadorConBotones() {
        buscador = new TextField();
        buscador.setPlaceholder("Buscar por nombre o email...");
        buscador.setWidth("300px");

        btnBuscar = new Button("🔍 Buscar", e -> ejecutarBusqueda());
        btnBuscar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnLimpiar = new Button("🗑️ Limpiar", e -> limpiarBusqueda());
        btnLimpiar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        buscador.addKeyPressListener(Key.ENTER, e -> ejecutarBusqueda());

        buscadorLayout = new HorizontalLayout(buscador, btnBuscar, btnLimpiar);
        buscadorLayout.setSpacing(true);
        // ✅ SIN Alignment
    }

    private void configurarGrid() {
        grid.addClassNames("usuarios-grid");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("500px");

        grid.addColumn(Usuario::getNombre).setHeader("Nombre").setSortable(true).setAutoWidth(true);
        grid.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true).setAutoWidth(true);
        grid.addColumn(Usuario::getTelefono).setHeader("Teléfono").setSortable(true).setAutoWidth(true);
        grid.addColumn(usuario -> usuario.getTipo_id() != null ? usuario.getTipo_id().getNombre() : "-")
                .setHeader("Tipo").setSortable(true).setAutoWidth(true);

        grid.addColumn(Usuario::getEsActivo).setHeader("Activo").setFlexGrow(0).setWidth("80px");
        grid.addColumn(usuario -> usuario.getPass() != null && !usuario.getPass().isEmpty()
                ? "🔐 Configurada" : "❌ Pendiente")
                .setHeader("Contraseña").setSortable(false).setFlexGrow(0).setWidth("120px");

        grid.addComponentColumn(this::crearBotonesAccion);
    }

    private HorizontalLayout crearBotonesAccion(Usuario usuario) {
        HorizontalLayout acciones = new HorizontalLayout();

        Button btnVer = new Button(new Icon(VaadinIcon.EYE));
        btnVer.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnVer.addClickListener(e -> abrirDialogVer(usuario));

        Button btnEditar = new Button(new Icon(VaadinIcon.EDIT));
        btnEditar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_TERTIARY);
        btnEditar.addClickListener(e -> abrirDialogEditar(usuario));

        Button btnEliminar = new Button(new Icon(VaadinIcon.TRASH));
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        btnEliminar.addClickListener(e -> eliminarUsuario(usuario));

        acciones.add(btnVer, btnEditar, btnEliminar);
        acciones.setSpacing(true);
        return acciones;
    }

    private void crearDialogEditar() {
        dialogEditar = new Dialog();
        dialogEditar.setHeaderTitle("✏️ Editar Usuario");
        dialogEditar.setWidth("600px");
        dialogEditar.setHeight("auto");

        TextField nombreField = new TextField("Nombre completo");
        TextField emailField = new TextField("Email");
        TextField telefonoField = new TextField("Teléfono");
        TextField direccionField = new TextField("Dirección");

        passField = new TextField("🔐 Contraseña (solo creación)");
        passField.setValue("");
        passField.setHelperText("Mínimo 8 caracteres");

        comboTipoUsuario = new ComboBox<>("Tipo Usuario");
        comboTipoUsuario.setItemLabelGenerator(TipoUsuario::getNombre);
        comboTipoUsuario.setItems(tipoUsuarioRepository.findAll());

        Checkbox activoField = new Checkbox("Activo");

        binder = new BeanValidationBinder<>(Usuario.class);
        binder.bind(nombreField, "nombre");
        binder.bind(emailField, "email");
        binder.bind(telefonoField, "telefono");
        binder.bind(direccionField, "direccion");
        binder.bind(comboTipoUsuario, "tipo_id");
        binder.bind(activoField, "esActivo");

        Button btnGuardar = new Button("💾 Guardar", e -> guardarUsuario());
        Button btnCancelar = new Button("❌ Cancelar", e -> {
            dialogEditar.close();
            usuarioEditando = null;
        });

        VerticalLayout form = new VerticalLayout(
                nombreField, emailField, telefonoField, direccionField,
                passField, comboTipoUsuario, activoField,
                new HorizontalLayout(btnGuardar, btnCancelar)
        );
        form.setSpacing(true);
        form.setPadding(true);

        dialogEditar.add(form);
    }

    private void crearDialogVer() {
        dialogVer = new Dialog();
        dialogVer.setHeaderTitle("👁️ Ver Usuario");
        dialogVer.setWidth("450px");
        dialogVer.setHeight("auto");
    }

    private void abrirDialogCrear() {
        usuarioEditando = null;

        comboTipoUsuario.setItems(tipoUsuarioRepository.findAll());

        binder.readBean(new Usuario());
        passField.setVisible(true);
        passField.setValue("");
        passField.focus();

        dialogEditar.setHeaderTitle("➕ Nuevo Usuario");
        dialogEditar.open();
    }

    private void abrirDialogEditar(Usuario usuario) {
        usuarioEditando = usuario;

        comboTipoUsuario.setItems(tipoUsuarioRepository.findAll());

        binder.readBean(usuario);
        passField.setVisible(false);

        dialogEditar.setHeaderTitle("✏️ Editar " + usuario.getNombre());
        dialogEditar.open();
    }

    private void abrirDialogVer(Usuario usuario) {
        dialogVer.removeAll();

        VerticalLayout detalles = new VerticalLayout();
        detalles.setSpacing(true);
        detalles.setPadding(true);
        
        detalles.add(new H3(usuario.getNombre() + " (" + usuario.getEmail() + ")"));
        detalles.add(new Paragraph("Dirección: " + (usuario.getDireccion().isEmpty() ? "-" : usuario.getDireccion())));
        detalles.add(new Paragraph("Teléfono: " + (usuario.getTelefono().isEmpty() ? "-" : usuario.getTelefono())));
        detalles.add(new Paragraph("Tipo: " + (usuario.getTipo_id() != null ? usuario.getTipo_id().getNombre() : "-")));
        detalles.add(new Paragraph("Estado: " + (usuario.getEsActivo() ? "✅ Activo" : "❌ Inactivo")));
        detalles.add(new Paragraph("Contraseña: " +
                (usuario.getPass() != null && !usuario.getPass().isEmpty() ? "🔐 Configurada" : "❌ Pendiente")));

        detalles.add(new H3("📋 Auditoría"));

        detalles.add(new Paragraph("Creado por: " + 
                (usuario.getCreatedBy() != null ? usuario.getCreatedBy() : "❌ No disponible")));
        
        detalles.add(new Paragraph("Fecha creación: " + 
                (usuario.getCreatedDate() != null ? 
                        usuario.getCreatedDate().toString() : "❌ No disponible")));
        
        detalles.add(new Paragraph("Modificado por: " + 
                (usuario.getModifiedBy() != null ? usuario.getModifiedBy() : "❌ No disponible")));
        
        detalles.add(new Paragraph("Última modificación: " + 
                (usuario.getModifiedDate() != null ? 
                        usuario.getModifiedDate().toString() : "❌ No disponible")));

        dialogVer.add(detalles);
        dialogVer.open();
    }

    private void guardarUsuario() {
        try {
            if (usuarioEditando == null) {
                Usuario nuevoUsuario = new Usuario();
                binder.writeBean(nuevoUsuario);

                String passPlana = passField.getValue().trim();
                if (passPlana == null || passPlana.isEmpty()) {
                    Notification.show("❌ La contraseña es obligatoria", 3000, Notification.Position.MIDDLE);
                    return;
                }
                if (passPlana.length() < 8) {
                    Notification.show("❌ La contraseña debe tener al menos 8 caracteres", 3000, Notification.Position.MIDDLE);
                    return;
                }

                nuevoUsuario.setPass(BCrypt.hashpw(passPlana, BCrypt.gensalt()));

                if (nuevoUsuario.getTipo_id() == null) {
                    Notification.show("❌ Selecciona un tipo de usuario", 3000, Notification.Position.MIDDLE);
                    return;
                }

                usuarioService.create(nuevoUsuario);
                Notification.show("✅ Usuario creado correctamente", 3000, Notification.Position.MIDDLE);
            } else {
                binder.writeBean(usuarioEditando);

                if (usuarioEditando.getTipo_id() == null) {
                    Notification.show("❌ Selecciona un tipo de usuario", 3000, Notification.Position.MIDDLE);
                    return;
                }

                usuarioService.update(usuarioEditando.getId(), usuarioEditando);
                Notification.show("✅ Usuario actualizado correctamente", 2000, Notification.Position.MIDDLE);
            }

            dialogEditar.close();
            usuarioEditando = null;
            todosLosUsuarios = usuarioService.findAll();
            grid.setItems(todosLosUsuarios);
        } catch (ValidationException e) {
            Notification.show("❌ Datos inválidos: " + e.getMessage(), 4000, Notification.Position.MIDDLE);
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

        List<Usuario> filtrados = todosLosUsuarios.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                        u.getEmail().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());

        grid.setItems(filtrados);
        Notification.show("✅ Encontrados " + filtrados.size() + " resultado(s)", 2000, Notification.Position.TOP_CENTER);
    }

    private void limpiarBusqueda() {
        buscador.clear();
        grid.setItems(todosLosUsuarios);
        Notification.show("🔄 Búsqueda limpiada", 1500, Notification.Position.TOP_CENTER);
    }

    private void eliminarUsuario(Usuario usuario) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("⚠️ Confirmar Desactivación");
        confirmDialog.setWidth("450px");

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);

        content.add(new Paragraph("¿Estás seguro de que quieres **desactivar** al usuario?"));
        content.add(new H3(usuario.getNombre()));
        content.add(new Paragraph("Email: " + usuario.getEmail()));
        content.add(new Paragraph("Esta acción es **irreversible**"));
        content.add(new Paragraph("El usuario no podrá iniciar sesión hasta ser reactivado."));

        Button btnConfirmar = new Button("✅ Sí, desactivar usuario", e -> {
            try {
                usuarioService.delete(usuario.getId());
                Notification.show("✅ Usuario '" + usuario.getNombre() + "' desactivado", 3000, Notification.Position.MIDDLE);
                todosLosUsuarios = usuarioService.findAll();
                grid.setItems(todosLosUsuarios);
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
        // ✅ SIN JustifyContentMode
        content.add(buttons);
        confirmDialog.add(content);
        confirmDialog.open();
    }

    private boolean isAdministrador() {
        try {
            Usuario currentUser = currentUserService.getCurrentUsuario();
            return currentUser != null &&
                   currentUser.getTipo_id() != null &&
                   "ADMINISTRADOR".equalsIgnoreCase(currentUser.getTipo_id().getNombre());
        } catch (Exception e) {
            System.err.println("Error verificando admin: " + e.getMessage());
            return false;
        }
    }
}