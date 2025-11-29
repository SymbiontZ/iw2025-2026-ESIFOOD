## 🧩 Diagrama de Clases (Mermaid)

> Las operaciones se omiten por claridad.  
> Este diagrama muestra las entidades principales, sus relaciones y una clase de asociación (`ActualizacionPedido`).

```mermaid
classDiagram
    direction LR

    %% ==== DEFINICIÓN DE CLASES ====

    %% --- Usuarios ---
    class Usuario {
      Long id <<PK>>
      String nombre [NOT NULL, len 1..100]
      String email [NOT NULL, UNIQUE, formato email]
    }
    class TipoUsuario {
      Long id <<PK>>
      String nombre [NOT NULL, UNIQUE]
    }

    %% --- Pedidos ---
    class Pedido {
      Long id <<PK>>
      BigDecimal precio [NOT NULL, >= 0]
      Date fecha [NOT NULL]
      Time hora [NOT NULL]
    }
    class EstadoPedido {
      Long id <<PK>>
      String nombre [NOT NULL, UNIQUE]
    }
    class ActualizacionPedido {
      Long id <<PK>>
      Date fecha [NOT NULL]
      Time hora [NOT NULL]
      String comentario [opc., len 0..255]
    }
    class LineaPedido {
      Long id <<PK>>
      BigDecimal precio_u [NOT NULL, > 0]
      int cantidad [NOT NULL, >= 1]
    }

    %% --- Productos y Menús ---
    class Producto {
      Long id <<PK>>
      String nombre [NOT NULL, len 1..100]
      BigDecimal precio [NOT NULL, >= 0]
    }
    class Simple {
    }
    class Menu {
    }
    class TipoProducto {
      Long id <<PK>>
      String nombre [NOT NULL, UNIQUE]
    }
    class Ingrediente {
      Long id <<PK>>
      String nombre [NOT NULL, UNIQUE]
      int stock [NOT NULL, >= 0]
    }

    %% --- Locales ---
    class Local {
      Long id <<PK>>
      String nombre [NOT NULL, UNIQUE]
      String direccion [NOT NULL, len 1..200]
      boolean estaAbierto [NOT NULL]
    }

    %% ==== RELACIONES ====

    %% --- Usuario ---
    Usuario "*" -- "1" TipoUsuario : tipo
    Usuario "1" -- "*" Pedido : realiza
    %% Si el Usuario incluye clientes y empleados, convendría que fuese opcional:
    Usuario "*" -- "0..1" Local : trabaja_en

    %% --- Pedido ---
    Pedido "1" -- "*" LineaPedido : contiene
    Pedido "*" -- "1" Local : pertenece_a
    Pedido "1" -- "*" ActualizacionPedido : tiene
    EstadoPedido "1" -- "*" ActualizacionPedido : registra
    note for ActualizacionPedido "Clase de asociación entre Pedido y EstadoPedido"

    %% --- Productos ---
    Producto <|-- Menu
    Producto <|-- Simple
    Simple "*" --o "1" Menu : compuesto_en
    Producto "1" -- "*" LineaPedido : aparece_en
    Producto "*" -- "*" Ingrediente : usa
    Simple "*" -- "1" TipoProducto : pertenece_a

