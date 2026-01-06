package es.uca.esifoodteam.usuarios.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.esifoodteam.common.layouts.MainLayout;

@Route("login")
@PageTitle("Iniciar Sesión | ESIFOOD")
@AnonymousAllowed
public class LoginView extends MainLayout implements BeforeEnterObserver {

    public LoginView() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle().set("background", "linear-gradient(135deg, #F9F5F0 0%, #f5f1ed 100%)");

        // Card de login
        Div loginCard = new Div();
        loginCard.getStyle()
                .set("background-color", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.15)")
                .set("padding", "40px")
                .set("width", "100%")
                .set("max-width", "420px");

        // Encabezado con icono
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        headerLayout.setSpacing(true);
        headerLayout.setWidthFull();

        Icon logoIcon = new Icon(VaadinIcon.SHOP);
        logoIcon.setSize("40px");
        logoIcon.getStyle().set("color", "#344f1f");

        H1 titulo = new H1("ESIFOOD");
        titulo.getStyle()
                .set("margin", "0")
                .set("color", "#344f1f")
                .set("font-size", "32px");

        headerLayout.add(logoIcon, titulo);

        // Subtítulo
        Span subtitulo = new Span("Bienvenido a tu plataforma de pedidos");
        subtitulo.getStyle()
                .set("color", "#999")
                .set("font-size", "14px")
                .set("text-align", "center");

        // Formulario de login personalizado
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);
        formLayout.setPadding(false);
        formLayout.setWidthFull();

        LoginForm login = new LoginForm();
        login.setAction("login");
        login.getElement().setAttribute("i18n.form.title", "");
        login.getElement().setAttribute("i18n.form.username", "Correo electrónico");
        login.getElement().setAttribute("i18n.form.password", "Contraseña");
        login.getElement().setAttribute("i18n.form.submit", "Iniciar Sesión");
        login.setForgotPasswordButtonVisible(false);

        formLayout.add(login);

        // Botón registro
        VerticalLayout registroLayout = new VerticalLayout();
        registroLayout.setSpacing(true);
        registroLayout.setPadding(false);
        registroLayout.setWidthFull();

        Div divisor = new Div();
        divisor.getStyle()
                .set("border-top", "1px solid #e0e0e0")
                .set("margin", "20px 0");
        registroLayout.add(divisor);

        HorizontalLayout registroTextLayout = new HorizontalLayout();
        registroTextLayout.setAlignItems(Alignment.CENTER);
        registroTextLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        registroTextLayout.setSpacing(true);
        registroTextLayout.setWidthFull();

        Span textoRegistro = new Span("¿No tienes cuenta?");
        textoRegistro.getStyle().set("color", "#666").set("font-size", "14px");

        Button registrarse = new Button("Registrarse aquí");
        registrarse.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        registrarse.getStyle()
                .set("color", "#344f1f")
                .set("font-weight", "600")
                .set("padding", "0")
                .set("text-decoration", "underline");
        registrarse.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("registro")));

        registroTextLayout.add(textoRegistro, registrarse);
        registroLayout.add(registroTextLayout);

        // Agregar todo al card
        loginCard.add(headerLayout, subtitulo, formLayout, registroLayout);
        content.add(loginCard);

        setContent(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        boolean hasError = event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error");
        if (hasError) {
            Notification notification = new Notification(
                    "Correo o contraseña incorrectos",
                    3000,
                    Notification.Position.TOP_CENTER
            );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }
}