package es.uca.esifoodteam.common.components;

import com.vaadin.flow.component.html.*;

public class ImageLink extends Anchor{
    public ImageLink(String href, String imgSrc, String altText, String className) {
        setHref(href);
        setClassName(className);
        Image image = new Image(imgSrc, altText);
        image.setClassName("image-expand");
        add(image);
    }
}
