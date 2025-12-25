package es.uca.esifoodteam.usuarios.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;
import es.uca.esifoodteam.usuarios.services.UsuarioService;

@Route("perfil")
@PageTitle("Mi Perfil")
public class PerfilUsuarioView extends VerticalLayout {

    private final UsuarioService usuarioService;
    private final CurrentUserService currentUserService;

    private final BeanValidationBinder<Usuario> binder = new BeanValidationBinder<>(Usuario.class);
    private Usuario usuarioActual;

    // CAMPOS DEL FORMULARIO
    private final TextField nombreField = new TextField("Nombre completo");
    private final EmailField emailField = new EmailField("Email");
    private final TextField telefonoField = new TextField("Teléfono");
    private final TextField direccionField = new TextField("Dirección");

    public PerfilUsuarioView(UsuarioService usuarioService, CurrentUserService currentUserService) {
        this.usuarioService = usuarioService;
        this.currentUserService = currentUserService;

        addClassName("perfil-view");
        setSpacing(false);
        setPadding(false);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();

        configurarFormulario();
        cargarUsuarioActual();
        construirUI();
    }

    private void configurarFormulario() {
        nombreField.setRequired(true);
        nombreField.setWidthFull();
        emailField.setRequired(true);
        emailField.setWidthFull();
        telefonoField.setWidthFull();
        direccionField.setWidthFull();

        binder.forField(nombreField).bind(Usuario::getNombre, Usuario::setNombre);
        binder.forField(emailField).bind(Usuario::getEmail, Usuario::setEmail);
        binder.forField(telefonoField).bind(Usuario::getTelefono, Usuario::setTelefono);
        binder.forField(direccionField).bind(Usuario::getDireccion, Usuario::setDireccion);
    }

    private void cargarUsuarioActual() {
        usuarioActual = currentUserService.getCurrentUsuario();
        if (usuarioActual == null) {
            Notification.show("❌ Error: No se pudo cargar tu perfil", 5000, Notification.Position.TOP_CENTER);
            return;
        }
        binder.readBean(usuarioActual);
    }

    private void construirUI() {
        VerticalLayout containerCentral = new VerticalLayout();
        containerCentral.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        containerCentral.setJustifyContentMode(JustifyContentMode.CENTER);
        containerCentral.setWidthFull();
        containerCentral.setHeightFull();
        containerCentral.getStyle().set("padding", "2rem");

        H2 titulo = new H2("Mis Datos Personales");
        titulo.addClassName("perfil-titulo");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("20em", 2)
        );

        formLayout.setWidth("600px");
        formLayout.getStyle().set("align-self", "center");

        formLayout.addClassName("perfil-form");
        formLayout.add(nombreField, emailField, telefonoField, direccionField);

        containerCentral.add(titulo, formLayout, new Hr(), crearBotones());
        add(containerCentral);
    }

    private VerticalLayout crearBotones() {
        VerticalLayout botonesLayout = new VerticalLayout();
        botonesLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        botonesLayout.setWidthFull();
        botonesLayout.setPadding(false);
        botonesLayout.setSpacing(true);

        Button guardarBtn = new Button("💾 Guardar cambios",
                new Icon(VaadinIcon.CHECK_CIRCLE), e -> guardar());
        guardarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        guardarBtn.setWidth("250px");

        Button suprimirBtn = new Button("💀 ELIMINAR CUENTA",
                new Icon(VaadinIcon.SHIELD), e -> suprimirDatos());
        suprimirBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_LARGE);
        suprimirBtn.setWidth("280px");

        botonesLayout.add(guardarBtn, suprimirBtn);
        return botonesLayout;
    }

    private void guardar() {
        if (usuarioActual == null) return;

        try {
            binder.writeBean(usuarioActual);

            if (usuarioService.existsByEmailAndIdNot(usuarioActual.getEmail(), usuarioActual.getId())) {
                Notification.show("❌ El email ya está en uso", 5000, Notification.Position.TOP_CENTER);
                return;
            }

            usuarioService.update(usuarioActual.getId(), usuarioActual);

            ConfirmDialog confirmacion = new ConfirmDialog();
            confirmacion.setHeader("✅ ¡Datos Guardados!");

            Span textoExito = new Span(
                    "Datos actualizados correctamente\n\n" +
                    "✓ Nombre: " + usuarioActual.getNombre() + "\n" +
                    "✓ Email: " + usuarioActual.getEmail() + "\n" +
                    "✓ Teléfono: " + usuarioActual.getTelefono() + "\n" +
                    "✓ Dirección: " + usuarioActual.getDireccion()
            );
            textoExito.getElement().getStyle().set("white-space", "pre-line");
            confirmacion.setText(textoExito);

            confirmacion.setConfirmText("OK");
            confirmacion.addConfirmListener(e1 -> confirmacion.close());
            confirmacion.open();

        } catch (ValidationException e) {
            Notification.show("❌ Errores de validación", 5000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification.show("❌ Error: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }

    // === ÚNICO DIÁLOGO PARA ELIMINAR CUENTA ===
    private void suprimirDatos() {
        if (usuarioActual == null) return;

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("⚠️ ¿Eliminar tu cuenta definitivamente?");

        Span textoRgpd = new Span(
            "Esta acción es irreversible:\n\n" +
            "• Se eliminarán todos tus datos personales del sistema.\n" +
            "• No podrás volver a acceder con esta cuenta.\n\n" +
            "Usuario: " + usuarioActual.getNombre() + "\n" +
            "Email: " + usuarioActual.getEmail()
        );
        textoRgpd.getElement().getStyle().set("white-space", "pre-line");
        dialog.setText(textoRgpd);

        // Botón principal: eliminar
        dialog.setConfirmText("Sí, eliminar cuenta");
        dialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());

        // Botón cancelar visible
        dialog.setCancelText("No, conservar cuenta");
        dialog.setCancelable(true); // necesario para que se muestre el botón Cancel[web:4][web:37]

        dialog.addConfirmListener(e -> eliminarDatosDefinitivo(dialog));
        dialog.addCancelListener(e -> {
            dialog.close();
            Notification.show("✅ Eliminación CANCELADA", 3000, Notification.Position.MIDDLE);
        });

        dialog.open();
    }

    private void eliminarDatosDefinitivo(ConfirmDialog dialog) {
        try {
            usuarioService.suprimirDatosPersonales(usuarioActual.getId());

            ConfirmDialog exitoDialog = new ConfirmDialog();
            exitoDialog.setHeader("✅ ¡Cuenta eliminada correctamente!");

            Span textoExito = new Span(
                    "✓ Todos los datos personales eliminados\n" +
                    "✓ Cuenta eliminada permanentemente\n" 
            );
            textoExito.getElement().getStyle().set("white-space", "pre-line");
            exitoDialog.setText(textoExito);

            exitoDialog.setConfirmText("OK");
            exitoDialog.addConfirmListener(e -> {
                exitoDialog.close();
                dialog.close();
                getUI().ifPresent(ui -> ui.navigate("/login?cuenta_eliminada"));
            });

            exitoDialog.open();

        } catch (Exception ex) {
            Notification.show("❌ Error: " + ex.getMessage(), 5000, Notification.Position.TOP_CENTER);
        }
    }
}