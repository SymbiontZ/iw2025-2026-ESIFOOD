package es.uca.esifoodteam.establecimientos;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.esifoodteam.usuarios.models.Usuario;
import es.uca.esifoodteam.usuarios.services.CurrentUserService;

@Route("admin/establecimiento")
@PageTitle("Gestión Establecimiento | ESIFOOD")
public class AdminEstablecimientoView extends VerticalLayout {

    private final EstablecimientoService establecimientoService;
    private final EstablecimientoRepository establecimientoRepository;
    private final CurrentUserService currentUserService;
    
    private Establecimiento establecimiento;
    private TextField nombreField;
    private TextField direccionField;
    private HorizontalLayout botonesLayout;
    private boolean modoEdicion = false;
    private Div infoDiv; // ✅ Reemplazo del Dialog

    public AdminEstablecimientoView(EstablecimientoService establecimientoService,
                                  EstablecimientoRepository establecimientoRepository,
                                  CurrentUserService currentUserService) {
        this.establecimientoService = establecimientoService;
        this.establecimientoRepository = establecimientoRepository;
        this.currentUserService = currentUserService;

        if (!tienePermisos()) {
            Notification.show("Acceso denegado", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.navigate("/"));
            return;
        }

        cargarDatos();
        crearInterfaz();
    }

    private void cargarDatos() {
        List<Establecimiento> lista = establecimientoRepository.findAll();
        if (!lista.isEmpty()) {
            establecimiento = lista.get(0);
        }
    }

    private void crearInterfaz() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        H2 header = new H2("🏪 Establecimiento");
        add(header);

        nombreField = new TextField("Nombre");
        nombreField.setValue(establecimiento != null ? establecimiento.getNombre() : "");
        nombreField.setWidthFull();
        nombreField.setEnabled(false);
        add(nombreField);

        direccionField = new TextField("Dirección");
        direccionField.setValue(establecimiento != null ? establecimiento.getDireccion() : "");
        direccionField.setWidthFull();
        direccionField.setEnabled(false);
        add(direccionField);

        botonesLayout = new HorizontalLayout();
        botonesLayout.setWidthFull();
        botonesLayout.setJustifyContentMode(JustifyContentMode.START);
        actualizarBotones();
        add(botonesLayout);

        // ✅ Div oculto para info (reemplazo del Dialog)
        infoDiv = new Div();
        infoDiv.setVisible(false);
        infoDiv.addClassName("info-section");
        infoDiv.getStyle()
            .set("background", "var(--lumo-contrast-5pct)")
            .set("padding", "20px")
            .set("border-radius", "8px")
            .set("border", "1px solid var(--lumo-contrast-10pct)")
            .set("margin-top", "20px");
        add(infoDiv);
    }

    private void actualizarBotones() {
        botonesLayout.removeAll();
        
        if (modoEdicion) {
            Button btnGuardar = new Button("💾 Guardar", e -> guardar());
            btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button btnCancelar = new Button("❌ Cancelar", e -> cancelar());
            btnCancelar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            
            botonesLayout.add(btnGuardar, btnCancelar);
        } else {
            Button btnEditar = new Button("✏️ Editar", e -> editar());
            btnEditar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            
            Button btnVer = new Button("📋 Ver info", e -> mostrarInfo());
            btnVer.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            
            botonesLayout.add(btnEditar, btnVer);
        }
    }

    private void editar() {
        modoEdicion = true;
        nombreField.setEnabled(true);
        direccionField.setEnabled(true);
        actualizarBotones();
        infoDiv.setVisible(false); // Ocultar info
    }

    private void guardar() {
        if (establecimiento == null) {
            Notification.show("No hay establecimiento", 3000, Notification.Position.MIDDLE);
            return;
        }

        String nombre = nombreField.getValue().trim();
        String direccion = direccionField.getValue().trim();

        if (nombre.isEmpty() || direccion.isEmpty()) {
            Notification.show("Nombre y dirección obligatorios", 3000, Notification.Position.MIDDLE);
            return;
        }

        establecimiento.setNombre(nombre);
        establecimiento.setDireccion(direccion);
        establecimientoService.save(establecimiento);
        
        Notification.show("✅ Guardado correctamente", 2000, Notification.Position.MIDDLE);
        
        modoEdicion = false;
        nombreField.setEnabled(false);
        direccionField.setEnabled(false);
        actualizarBotones();
    }

    private void cancelar() {
        modoEdicion = false;
        if (establecimiento != null) {
            nombreField.setValue(establecimiento.getNombre());
            direccionField.setValue(establecimiento.getDireccion());
        }
        nombreField.setEnabled(false);
        direccionField.setEnabled(false);
        actualizarBotones();
    }

    private void mostrarInfo() {
        if (establecimiento == null) {
            Notification.show("No hay datos de establecimiento", 3000, Notification.Position.MIDDLE);
            return;
        }

        // ✅ Div con componentes Vaadin NATIVOS (SIN HTML)
        infoDiv.removeAll();
        
        H3 titulo = new H3("🏪 " + establecimiento.getNombre());
        Paragraph dir = new Paragraph("📍 Dirección: " + establecimiento.getDireccion());
        
        H3 auditoria = new H3("📋 Auditoría");
        Paragraph modificadoPor = new Paragraph("✏️ Modificado por: " + (establecimiento.getModifiedBy() != null ? establecimiento.getModifiedBy() : "-"));
        Paragraph ultimaMod = new Paragraph("🔄 Última modificación: " + (establecimiento.getModifiedDate() != null ? establecimiento.getModifiedDate().toString() : "-"));

        infoDiv.add(titulo, dir, auditoria, modificadoPor, ultimaMod);
        infoDiv.setVisible(true);
        
        // Scroll suave al final
        infoDiv.getElement().scrollIntoView();
    }

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
}