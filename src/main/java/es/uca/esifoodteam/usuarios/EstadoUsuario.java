package es.uca.esifoodteam.usuarios;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "estado_usuario")
public class EstadoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String nombre;  

    // Constructor vacío
    public EstadoUsuario() {}

    // Constructor útil
    public EstadoUsuario(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
