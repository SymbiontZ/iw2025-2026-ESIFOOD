package es.uca.esifoodteam.common.config;

public class SecurityPaths {
    
    public static final String[] PUBLIC_PATHS = {
        "/",
        "/login",
        "/registro",
        "/carrito",
        "/productos/**",
        "/VAADIN/**",
        "/css/**",
        "/js/**",
        "/images/**"
    };
    
    public static final String[] PROTECTED_PATHS = {
        "/admin/**",
        "/usuarios/**",
        "/pedidos/**"
    };
    
    private SecurityPaths() {
        // Clase de constantes, no instanciar
    }
}
