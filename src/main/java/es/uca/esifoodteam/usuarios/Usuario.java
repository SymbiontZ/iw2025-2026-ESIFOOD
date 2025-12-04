package es.uca.esifoodteam.usuarios;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import es.uca.esifoodteam.establecimientos.Local;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoUsuario tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id")   // puede ser null si es cliente
    private Local localTrabajo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Size(max = 255)
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 20)
    @Column(length = 20, unique = true, nullable = false)
    private String telefono = "";  

    @Size(max = 500)
    @Column(length = 500, nullable = false)
    private String direccion = ""; 

    @NotBlank
    @Column(nullable = false)
    private Boolean esActivo = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    // Constructor vacío (requerido por JPA)
    public Usuario() {}

    // Constructor útil
    public Usuario(Long id, TipoUsuario tipo, String nombre, String email, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.email = email;
        this.esActivo = true;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {  this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) {  this.email = email; }

    public TipoUsuario getTipo_id() { return tipo; }
    public void setTipo_id(TipoUsuario tipo) { this.tipo = tipo; }

    public Boolean getEsActivo() { return esActivo; }
    public void setEsActivo(Boolean esActivo) { this.esActivo = esActivo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public Local getLocalTrabajo() { return localTrabajo; }
    public void setLocalTrabajo(Local localTrabajo) { this.localTrabajo = localTrabajo; }
}