package es.uca.esifoodteam.usuarios.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import es.uca.esifoodteam.common.layouts.MainLayout;

@Route("login")
@PageTitle("Login | ESIFOOD")
@AnonymousAllowed
public class LoginView extends MainLayout implements BeforeEnterObserver {

    public LoginView() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        content.setPadding(true);
        content.setSpacing(true);

        LoginForm login = new LoginForm();
        login.setAction("login");

        Button registrarse = new Button("Crear cuenta");
        registrarse.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        registrarse.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate("registro"))
        );

        content.add(
            new H1("ESIFOOD - Iniciar sesión"),
            login,
            registrarse
        );

        add(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        boolean hasError = event.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error");
        if (hasError) {
            Notification.show("Credenciales inválidas", 3000, Notification.Position.TOP_CENTER);
        }
    }
}