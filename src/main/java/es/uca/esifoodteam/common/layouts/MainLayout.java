package es.uca.esifoodteam.common.layouts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;

import es.uca.esifoodteam.common.components.Navbar;

@CssImport("./styles/producto-components.css")
@CssImport("./styles/style.css")
public class MainLayout extends AppLayout {
    private VerticalLayout contentArea;

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        addToNavbar(new Navbar());
        setContent(createContent());
    }

    private VerticalLayout createContent() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setWidthFull();
        wrapper.setHeightFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        
        contentArea = new VerticalLayout();
        contentArea.setWidthFull();
        contentArea.setPadding(false);
        contentArea.setSpacing(false);
        contentArea.addClassName("content");
        contentArea.getStyle().set("overflow-y", "auto");
        
        wrapper.add(contentArea);
        wrapper.add(createFooter());
        
        // El contenido principal ocupa todo el espacio disponible, menos el del footer
        wrapper.setFlexGrow(1, contentArea);
        wrapper.setFlexGrow(0, wrapper.getComponentAt(1)); // Footer no crece
        
        return wrapper;
    }

    /**
     * Añade un componente al área de contenido principal.
     * 
     * @param component El componente a añadir.
     */
    public void addContent(Component component){
        contentArea.add(component);
    }

    /**
     * Limpia el área de contenido principal.
     */
    public void clearContent() {
        System.out.println("Limpiando contenido");
        contentArea.removeAll();
    }

    private HorizontalLayout createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setHeight("auto");
        footer.setPadding(true);
        footer.setSpacing(true);
        footer.addClassName("footer");
        footer.setFlexGrow(0); // No crece
        
        // Contenido del footer - texto copyright
        Div footerText = new Div();
        footerText.setText("© 2025 EsiFood. Todos los derechos reservados.");
        footerText.addClassName("footer-text");
        
        // Link admin
        Anchor adminLink = new Anchor("/admin", "");
        adminLink.addClassName("footer-link");
        Icon adminIcon = new Icon(VaadinIcon.COGS);
        adminLink.add(adminIcon);
        
        footer.add(footerText, adminLink);
        footer.expand(footerText); // El texto ocupa el espacio disponible
        
        return footer;
    }


    
}
