package es.uca.esifoodteam.usuarios.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
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

    private TextField nombre = new TextField("Nombre");
    private TextField telefono = new TextField("Telefono");
    private TextField direccion = new TextField("Dirección");
    private EmailField email = new EmailField("Email");
    private PasswordField pass = new PasswordField("Contraseña");
    private PasswordField passConfirm = new PasswordField("Repetir contraseña");
    private Button registrar = new Button("Registrarse");

    public RegistroView(AuthService authService, TipoUsuarioRepository tipoUsuarioRepository) {
        this.authService = authService;
        this.tipoUsuarioRepository = tipoUsuarioRepository;

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);

        nombre.setRequired(true);
        email.setRequired(true);
        pass.setRequired(true);
        passConfirm.setRequired(true);

        registrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registrar.addClickListener(e -> registrarUsuario());

        content.add(
            new H1("Crear cuenta ESIFOOD"),
            nombre,
            telefono,
            direccion,
            email,
            pass,
            passConfirm,
            registrar
        );

        add(content);
    }

    private void registrarUsuario() {
        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
            Notification.show("Rellena todos los campos", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (!pass.getValue().equals(passConfirm.getValue())) {
            Notification.show("Las contraseñas no coinciden", 3000, Notification.Position.MIDDLE);
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

            Notification.show("Usuario registrado correctamente", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate("login"));
        } catch (Exception ex) {
            Notification.show("Error al registrar: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }
}