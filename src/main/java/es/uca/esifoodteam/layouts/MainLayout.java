package es.uca.esifoodteam.layouts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.*;

public class MainLayout extends AppLayout {
    private VerticalLayout contentArea;

    public MainLayout() {
        setPrimarySection(Section.NAVBAR);
        addToNavbar(createHeader());
        setContent(createContent());
    }

    private Component createHeader() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.addClassName("navbar");

        return navbar;
    }

    private Component createContent() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSizeFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        
        contentArea = new VerticalLayout();
        contentArea.setSizeFull();
        contentArea.addClassName("content-area");
        
        wrapper.add(contentArea);
        wrapper.add(createFooter());
        
        // El contenido principal ocupa todo el espacio disponible
        wrapper.setFlexGrow(1, contentArea);
        
        return wrapper;
    }

    
    public void add(Component component){
        contentArea.add(component);
    }

    private Component createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setPadding(true);
        footer.setSpacing(true);
        footer.addClassName("footer");
        footer.getStyle().set("background-color", "#f5f5f5");
        footer.getStyle().set("border-top", "1px solid #ddd");
        
        // Contenido del footer
        Div footerText = new Div();
        footerText.setText("© 2025 EsiFood. Todos los derechos reservados.");
        
        footer.add(footerText);
        footer.expand(footerText); // El texto ocupa el espacio disponible
        
        return footer;
    }


    
}
