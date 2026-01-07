package es.uca.esifoodteam.usuarios.views;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.repositories.TipoUsuarioRepository;
import es.uca.esifoodteam.usuarios.services.AuthService;

@Route("registro")
@PageTitle("Registro | ESIFOOD")
@AnonymousAllowed
public class RegistroView extends MainLayout {

    private final AuthService authService;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    private TextField nombre = new TextField("Nombre completo");
    private TextField telefono = new TextField("Teléfono");
    private TextField direccion = new TextField("Dirección");
    private EmailField email = new EmailField("Correo electrónico");
    private PasswordField pass = new PasswordField("Contraseña");
    private PasswordField passConfirm = new PasswordField("Repetir contraseña");
    private Button registrar = new Button("Crear cuenta");

    public RegistroView(AuthService authService, TipoUsuarioRepository tipoUsuarioRepository) {
        this.authService = authService;
        this.tipoUsuarioRepository = tipoUsuarioRepository;

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setSpacing(false);
        content.setPadding(false);
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.getStyle().set("background", "linear-gradient(to right, #F9F5F0 0%, #f5f1ed 100%)")
                           .set("padding", "3rem 1rem");

        addContent(construirUI(content));
    }

    private VerticalLayout construirUI(VerticalLayout content) {
        // Card principal
        Div card = new Div();
        card.getStyle()
            .set("max-width", "500px")
            .set("background", "white")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 12px rgba(52, 79, 31, 0.1)")
            .set("padding", "2.5rem");

        // Header con icono
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setSpacing(true);
        header.setWidthFull();
        header.setMargin(false);
        header.setPadding(false);
        
        Icon shopIcon = new Icon(VaadinIcon.SHOP);
        shopIcon.setSize("2.5rem");
        shopIcon.getStyle().set("color", "#344f1f");
        
        H1 titulo = new H1("Crear Cuenta");
        titulo.getStyle().set("margin", "0").set("color", "#344f1f").set("flex-grow", "1");
        
        header.add(shopIcon, titulo);

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("25em", 2)
        );
        formLayout.setWidth("100%");
        formLayout.setColspan(email, 2);
        formLayout.setColspan(direccion, 2);

        nombre.setRequired(true);
        nombre.setWidthFull();
        nombre.setPrefixComponent(new Icon(VaadinIcon.USER));
        
        email.setRequired(true);
        email.setWidthFull();
        email.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        
        telefono.setWidthFull();
        telefono.setPrefixComponent(new Icon(VaadinIcon.PHONE));
        
        direccion.setWidthFull();
        direccion.setPrefixComponent(new Icon(VaadinIcon.LOCATION_ARROW));
        
        pass.setRequired(true);
        pass.setWidthFull();
        pass.setPrefixComponent(new Icon(VaadinIcon.LOCK));
        
        passConfirm.setRequired(true);
        passConfirm.setWidthFull();
        passConfirm.setPrefixComponent(new Icon(VaadinIcon.LOCK));

        formLayout.add(nombre, telefono, email, pass, direccion, passConfirm);

        // Botón de registro
        registrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        registrar.setWidth("100%");
        registrar.addClickListener(e -> registrarUsuario());

        // Enlace a login
        Span linkSpan = new Span("¿Ya tienes cuenta? ");
        Anchor loginLink = new Anchor("/login", "Inicia sesión aquí");
        loginLink.getStyle().set("color", "#344f1f").set("font-weight", "500");
        
        HorizontalLayout linkContainer = new HorizontalLayout(linkSpan, loginLink);
        linkContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        linkContainer.setSpacing(false);
        linkContainer.setMargin(false);
        linkContainer.setPadding(false);
        linkContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        // Agregamos contenido a la tarjeta
        card.add(header, new Hr(), formLayout, registrar, new Hr(), linkContainer);

        content.add(card);
        return content;
    }

    private void registrarUsuario() {
        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
            Notification notification = Notification.show("Rellena todos los campos requeridos");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
            return;
        }

        if (!pass.getValue().equals(passConfirm.getValue())) {
            Notification notification = Notification.show("Las contraseñas no coinciden");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
            return;
        }

        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre.getValue());
            usuario.setEmail(email.getValue());
            usuario.setTelefono(telefono.getValue());
            usuario.setDireccion(direccion.getValue());
            usuario.setEsActivo(true);

            // Asignar tipo por defecto (por ejemplo CLIENTE)
            TipoUsuario tipoCliente = tipoUsuarioRepository
                    .findByNombre("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Tipo de usuario CLIENTE no encontrado"));
            usuario.setTipo_id(tipoCliente);

            authService.createWithPassword(usuario, pass.getValue());

            Notification notification = Notification.show("Cuenta creada correctamente. Redirigiendo...");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_CENTER);
            
            getUI().ifPresent(ui -> ui.navigate("login"));
        } catch (Exception ex) {
            Notification notification = Notification.show("Error al registrar: " + ex.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }
    }
}