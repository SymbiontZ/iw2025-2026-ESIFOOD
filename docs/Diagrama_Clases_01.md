## 🧩 Diagrama de Clases (Mermaid)

> Las operaciones se omiten por claridad.  
> Este diagrama muestra las entidades principales, sus relaciones y una clase de asociación (`ActualizacionPedido`).
> Se muestran las id's y FK para la claridad y desarrollo simultáneo entre los distintos miembros del equipo.

```mermaid
classDiagram
        direction LR
    
        %% ==== DEFINICIÓN DE CLASES ====
    
        %% --- Usuarios ---
        class Usuario {
                Long id <<PK>>
                Long tipo_id <<FK>>
                Long estado_id <<FK>>
                Long local_id <<FK>>
                String nombre [NOT NULL, len 1..100]
                String email [NOT NULL, UNIQUE, formato email]
                String telefono [NOT NULL, UNIQUE, len 1..20]
                String dni [NOT NULL, UNIQUE, len 1..50]
        }
        class TipoUsuario {
                Long id <<PK>>
                String nombre [NOT NULL, UNIQUE]
        }
    
        class EstadoUsuario {
                Long id <<PK>>
                String nombre [NOT NULL, UNIQUE]
        }
    
        %% --- Pedidos ---
        class Pedido {
                Long id <<PK>>
                Long estado_id <<FK>>
                Long usuario_id <<FK>>
                Long local_id <<FK>>
                List<ActualizacionPedido> actualizaciones
                List<LineaPedido> lineas
                BigDecimal precio [NOT NULL, >= 0]
                DateTime fechaHora [NOT NULL]
                String observaciones [len 0..500]
        }
        class EstadoPedido {
                Long id <<PK>>
                String nombre [NOT NULL, UNIQUE]
        }
        class ActualizacionPedido {
                Long id <<PK>>
                Long pedido_id <<FK>>
                Long estado_id <<FK>>
                DateTime fechaHora [NOT NULL]
                String comentario [opc., len 0..500]
        }
        class LineaPedido {
                Long id <<PK>>
                Long pedido_id <<FK>>
                Long producto_id <<FK>>
                BigDecimal precio_u [NOT NULL, > 0]
                BigDecimal subtotal [NOT NULL, > 0]
                int cantidad [NOT NULL, >= 1]
        }
    
        %% --- Productos y Menús ---
        class Producto {
                Long id <<PK>>
                Long local_id <<FK>>
                Set<Ingrediente> ingredientes
                List<LineaPedido> lineas
                String nombre [NOT NULL, len 1..100]
                String descripcion [len 0..500]
                BigDecimal precio [NOT NULL, >= 0]
                boolean disponible [NOT NULL]
        }
        class Simple {
                Long tipo_producto_id
        }
        class Menu {
                Set<Simple> productos
        }
        class TipoProducto {
                Long id <<PK>>
                String nombre [NOT NULL, UNIQUE]
        }
        class Ingrediente {
                Long id <<PK>>
                Set<Producto> productos
                String nombre [NOT NULL, UNIQUE]
                int stock [NOT NULL, >= 0]
        }
    
        %% --- Locales ---
        class Local {
                Long id <<PK>>
                List<Producto> productos
                String nombre [NOT NULL, UNIQUE]
                String direccion [NOT NULL, len 1..200]
                boolean estaAbierto [NOT NULL]
        }
    
        %% ==== RELACIONES ====
    
        %% --- Usuario ---
        Usuario "*" -- "1" TipoUsuario : tipo
        Usuario "*" -- "1" EstadoUsuario : estado
        Usuario "1" -- "*" Pedido : realiza
        %% Si el Usuario incluye clientes y empleados, convendría que fuese opcional:
        Usuario "*" -- "0..1" Local : trabaja_en
    
        %% --- Pedido ---
        Pedido "1" -- "*" LineaPedido : contiene
        Pedido "*" -- "1" Local : pertenece_a
        Pedido "1" -- "*" ActualizacionPedido : tiene_historial
        EstadoPedido "1" -- "*" ActualizacionPedido : aparece_en
        note for ActualizacionPedido "Historial de cambios de estado de un Pedido"
    
        %% --- Productos ---
        Producto <|-- Menu
        Producto <|-- Simple
        Simple "*" --o "1" Menu : compuesto_por
        Producto "1" -- "*" LineaPedido : aparece_en
        Producto "*" -- "*" Ingrediente : usa
        Simple "*" -- "1" TipoProducto : pertenece_a
    
