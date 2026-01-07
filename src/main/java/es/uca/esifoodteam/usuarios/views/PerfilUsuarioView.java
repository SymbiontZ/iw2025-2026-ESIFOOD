package es.uca.esifoodteam.usuarios.views;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;
import es.uca.esifoodteam.usuarios.services.UsuarioService;

@Route("perfil")
@PageTitle("Mi Perfil | ESIFOOD")
public class PerfilUsuarioView extends MainLayout {

    private final UsuarioService usuarioService;
    private final CurrentUserService currentUserService;

    private final BeanValidationBinder<Usuario> binder = new BeanValidationBinder<>(Usuario.class);
    private Usuario usuarioActual;

    // CAMPOS DEL FORMULARIO
    private final TextField nombreField = new TextField("Nombre completo");
    private final EmailField emailField = new EmailField("Correo electrónico");
    private final TextField telefonoField = new TextField("Teléfono");
    private final TextField direccionField = new TextField("Dirección");

    public PerfilUsuarioView(UsuarioService usuarioService, CurrentUserService currentUserService) {
        this.usuarioService = usuarioService;
        this.currentUserService = currentUserService;

        VerticalLayout content = new VerticalLayout();
        content.addClassName("perfil-view");
        content.setSpacing(false);
        content.setPadding(false);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setSizeFull();
        content.getStyle().set("background", "linear-gradient(to right, #F9F5F0 0%, #f5f1ed 100%)");

        configurarFormulario();
        cargarUsuarioActual();
        content.add(construirUI());
        addContent(content);
    }

    private void configurarFormulario() {
        nombreField.setRequired(true);
        nombreField.setWidthFull();
        nombreField.setPrefixComponent(new Icon(VaadinIcon.USER));
        
        emailField.setRequired(true);
        emailField.setWidthFull();
        emailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE));
        
        telefonoField.setWidthFull();
        telefonoField.setPrefixComponent(new Icon(VaadinIcon.PHONE));
        
        direccionField.setWidthFull();
        direccionField.setPrefixComponent(new Icon(VaadinIcon.LOCATION_ARROW));

        binder.forField(nombreField).bind(Usuario::getNombre, Usuario::setNombre);
        binder.forField(emailField).bind(Usuario::getEmail, Usuario::setEmail);
        binder.forField(telefonoField).bind(Usuario::getTelefono, Usuario::setTelefono);
        binder.forField(direccionField).bind(Usuario::getDireccion, Usuario::setDireccion);
    }

    private void cargarUsuarioActual() {
        usuarioActual = currentUserService.getCurrentUsuario();
        if (usuarioActual == null) {
            Notification notification = Notification.show("No se pudo cargar tu perfil");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
            return;
        }
        binder.readBean(usuarioActual);
    }

    private VerticalLayout construirUI() {
        VerticalLayout containerCentral = new VerticalLayout();
        containerCentral.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        containerCentral.setJustifyContentMode(JustifyContentMode.START);
        containerCentral.setWidthFull();
        containerCentral.setHeightFull();
        containerCentral.getStyle().set("padding", "3rem 1rem");

        // Card principal
        Div card = new Div();
        card.setWidthFull();
        card.getStyle()
            .set("max-width", "600px")
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
        
        Icon userIcon = new Icon(VaadinIcon.USER_CARD);
        userIcon.setSize("2.5rem");
        userIcon.getStyle().set("color", "#344f1f");
        
        H1 titulo = new H1("Mi Perfil");
        titulo.getStyle().set("margin", "0").set("color", "#344f1f").set("flex-grow", "1");
        
        header.add(userIcon, titulo);

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("25em", 2)
        );
        formLayout.setWidth("100%");
        formLayout.setColspan(emailField, 2);
        formLayout.setColspan(direccionField, 2);
        formLayout.add(nombreField, telefonoField, emailField, direccionField);

        // Agregamos contenido a la tarjeta
        card.add(header, new Hr(), formLayout, new Hr(), crearBotones());

        containerCentral.add(card);
        return containerCentral;
    }

    private VerticalLayout crearBotones() {
        VerticalLayout botonesLayout = new VerticalLayout();
        botonesLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        botonesLayout.setWidthFull();
        botonesLayout.setPadding(false);
        botonesLayout.setSpacing(true);

        Button guardarBtn = new Button("Guardar cambios", new Icon(VaadinIcon.CHECK_CIRCLE));
        guardarBtn.addClickListener(e -> guardar());
        guardarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        guardarBtn.setWidth("100%");

        Button suprimirBtn = new Button("Eliminar cuenta", new Icon(VaadinIcon.TRASH));
        suprimirBtn.addClickListener(e -> suprimirDatos());
        suprimirBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_LARGE);
        suprimirBtn.setWidth("100%");

        botonesLayout.add(guardarBtn, suprimirBtn);
        return botonesLayout;
    }

    private void guardar() {
        if (usuarioActual == null) return;

        try {
            binder.writeBean(usuarioActual);

            if (usuarioService.existsByEmailAndIdNot(usuarioActual.getEmail(), usuarioActual.getId())) {
                Notification notification = Notification.show("El email ya está en uso");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
                return;
            }

            usuarioService.update(usuarioActual.getId(), usuarioActual);

            ConfirmDialog confirmacion = new ConfirmDialog();
            confirmacion.setHeader("Datos Guardados");

            Div contenido = new Div();
            contenido.getStyle().set("padding", "1rem 0");
            
            Span textoExito = new Span(
                    "Los datos se han actualizado correctamente:\n\n" +
                    "• Nombre: " + usuarioActual.getNombre() + "\n" +
                    "• Email: " + usuarioActual.getEmail() + "\n" +
                    "• Teléfono: " + usuarioActual.getTelefono() + "\n" +
                    "• Dirección: " + usuarioActual.getDireccion()
            );
            textoExito.getElement().getStyle().set("white-space", "pre-line");
            contenido.add(textoExito);
            confirmacion.setText(contenido);

            confirmacion.setConfirmText("Cerrar");
            confirmacion.addConfirmListener(e1 -> confirmacion.close());
            confirmacion.open();

        } catch (ValidationException e) {
            Notification notification = Notification.show("Errores de validación en el formulario");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification notification = Notification.show("Error: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }
    }

    private void suprimirDatos() {
        if (usuarioActual == null) return;

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Eliminar cuenta");

        Div contenido = new Div();
        contenido.getStyle().set("padding", "1rem 0");
        
        Span textoRgpd = new Span(
            "Esta acción es irreversible y eliminará permanentemente:\n\n" +
            "• Todos tus datos personales\n" +
            "• Tu historial de pedidos\n" +
            "• Tu acceso a la plataforma\n\n" +
            "Usuario: " + usuarioActual.getNombre() + "\n" +
            "Email: " + usuarioActual.getEmail()
        );
        textoRgpd.getElement().getStyle().set("white-space", "pre-line").set("color", "#d32f2f");
        contenido.add(textoRgpd);
        dialog.setText(contenido);

        dialog.setConfirmText("Sí, eliminar definitivamente");
        dialog.setConfirmButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());

        dialog.setCancelText("Cancelar");
        dialog.setCancelable(true);

        dialog.addConfirmListener(e -> eliminarDatosDefinitivo(dialog));
        dialog.addCancelListener(e -> dialog.close());

        dialog.open();
    }

    private void eliminarDatosDefinitivo(ConfirmDialog dialog) {
        try {
            usuarioService.suprimirDatosPersonales(usuarioActual.getId());

            ConfirmDialog exitoDialog = new ConfirmDialog();
            exitoDialog.setHeader("Cuenta eliminada");

            Div contenido = new Div();
            contenido.getStyle().set("padding", "1rem 0");
            
            Span textoExito = new Span(
                    "Tu cuenta y todos los datos asociados han sido eliminados.\n\n" +
                    "Serás redirigido al inicio de sesión."
            );
            textoExito.getElement().getStyle().set("white-space", "pre-line");
            contenido.add(textoExito);
            exitoDialog.setText(contenido);

            exitoDialog.setConfirmText("OK");
            exitoDialog.addConfirmListener(e -> {
                exitoDialog.close();
                dialog.close();
                getUI().ifPresent(ui -> ui.navigate("/login"));
            });

            exitoDialog.open();

        } catch (Exception ex) {
            Notification notification = Notification.show("Error: " + ex.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }
    }
}