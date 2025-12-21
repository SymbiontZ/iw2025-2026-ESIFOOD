package es.uca.esifoodteam.usuarios.models;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

import es.uca.esifoodteam.establecimientos.Establecimiento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoUsuario tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "establecimiento_id")   // puede ser null si es cliente
    private Establecimiento establecimientoTrabajo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Size(max = 255)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false, length = 100)
    private String pass;

    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String telefono = "";  

    @Size(max = 500)
    @Column(length = 500, nullable = false)
    private String direccion = ""; 

    @NotNull
    @Column(nullable = false)
    private Boolean esActivo = true;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private Instant createdDate;  

    @LastModifiedDate
    @Column(name = "modified_date")
    private Instant modifiedDate;  

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)  
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", length = 50)  
    private String modifiedBy;

    // Constructor vacío (requerido por JPA)
    public Usuario() {}

    // Constructor útil
    public Usuario(Long id, TipoUsuario tipo, String nombre, String email, String pass) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.email = email;
        this.pass = pass;
        this.esActivo = true;
    }

    // Spring Security
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (tipo == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipo.getNombre().toUpperCase()));
    }

    public boolean verificarPass(String passPlana) { 
        return BCrypt.checkpw(passPlana, this.pass); 
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {  this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) {  this.email = email; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public TipoUsuario getTipo_id() { return tipo; }
    public void setTipo_id(TipoUsuario tipo) { this.tipo = tipo; }

    public Boolean getEsActivo() { return esActivo; }
    public void setEsActivo(Boolean esActivo) { this.esActivo = esActivo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Establecimiento getEstablecimientoTrabajo() { return establecimientoTrabajo; }
    public void setEstablecimientoTrabajo(Establecimiento establecimientoTrabajo) { this.establecimientoTrabajo = establecimientoTrabajo; }

    public Instant getCreatedDate() { return createdDate; }
    public void setCreatedDate(Instant createdDate) { this.createdDate = createdDate; }

    public Instant getModifiedDate() { return modifiedDate; }  
    public void setModifiedDate(Instant modifiedDate) { this.modifiedDate = modifiedDate; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }
}