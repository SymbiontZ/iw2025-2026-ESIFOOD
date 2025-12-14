package es.uca.esifoodteam.common.layouts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
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
        wrapper.setSizeFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        
        contentArea = new VerticalLayout();
        contentArea.setSizeFull();
        contentArea.addClassName("content");
        
        wrapper.add(contentArea);
        wrapper.add(createFooter());
        
        // El contenido principal ocupa todo el espacio disponible
        wrapper.setFlexGrow(1, contentArea);
        
        return wrapper;
    }

    
    public void add(Component component){
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
        footer.setPadding(true);
        footer.setSpacing(true);
        footer.addClassName("footer");
        
        // Contenido del footer
        Div footerText = new Div();
        footerText.setText("© 2025 EsiFood. Todos los derechos reservados.");
        
        footer.add(footerText);
        footer.expand(footerText); // El texto ocupa el espacio disponible
        
        return footer;
    }


    
}
