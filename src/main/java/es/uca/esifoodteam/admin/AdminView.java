package es.uca.esifoodteam.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.common.layouts.MainLayout;
import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;

@Route("admin")
@PageTitle("Admin | ESIFOOD")
public class AdminView extends MainLayout implements BeforeEnterObserver {

    private final CurrentUserService currentUserService;

    public AdminView(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
        crearInterfaz();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!tienePermisos()) {
            Notification.show("❌ Acceso denegado", 3000, Notification.Position.TOP_CENTER);
            // ❌ NO usar forwardTo
            // event.forwardTo(""); 
            
            // ✅ USAR rerouteTo
            event.rerouteTo("");  // Cambia URL sin recargar completamente
        }
    }

    private boolean tienePermisos() {
        try {
            Usuario user = currentUserService.getCurrentUsuario();  // ✅ SIN var
            if (user == null || user.getTipo_id() == null) {
                return false;
            }
            String tipo = user.getTipo_id().getNombre();
            return tipo.equalsIgnoreCase("ADMINISTRADOR") || tipo.equalsIgnoreCase("ENCARGADO");
        } catch (Exception e) {
            return false;
        }
    }

    private void crearInterfaz() {
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(true);
        content.setSizeFull();
        
        H1 title = new H1("🔐 Panel de Administración");
        content.add(title);

        H2 subtitle = new H2("Selecciona una opción para gestionar:");
        content.add(subtitle);

        Button usuariosBtn = new Button("👥 Gestionar Usuarios", e -> 
            getUI().ifPresent(ui -> ui.navigate("admin/usuarios")));
        Button pedidosBtn = new Button("📦 Gestionar Pedidos", e -> 
            getUI().ifPresent(ui -> ui.navigate("admin/pedidos")));
        Button establecimientosBtn = new Button("🏪 Gestionar Establecimiento", e -> 
            getUI().ifPresent(ui -> ui.navigate("admin/establecimiento")));

        content.add(usuariosBtn, pedidosBtn, establecimientosBtn);
        addContent(content);
    }
}
