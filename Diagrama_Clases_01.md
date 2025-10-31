## 🧩 Diagrama de Clases (Mermaid)

> Las operaciones se omiten por claridad.  
> Este diagrama muestra las entidades principales, sus relaciones y una clase de asociación (`ActualizacionPedido`).

```mermaid
classDiagram
    direction LR

    %% ==== DEFINICIÓN DE CLASES ====

    %% --- Usuarios ---
    class Usuario {
      String nombre
      String email
    }
    class TipoUsuario {
      String nombre
    }

    %% --- Pedidos ---
    class Pedido {
      BigDecimal precio
      Date fecha
      Time hora
    }
    class EstadoPedido {
      String nombre
    }
    class ActualizacionPedido {
      Date fecha
      Time hora
    }
    class LineaPedido {
      BigDecimal precio_u
      int cantidad
    }

    %% --- Productos y Menús ---
    class Producto {
      String nombre
      BigDecimal precio
    }
    class Simple {}
    class Menu {}
    class TipoProducto {
      String nombre
    }
    class Ingrediente {
      String nombre
      int stock
    }

    %% --- Locales ---
    class Local {
      String nombre
      String direccion
      boolean estaAbierto
    }

    %% ==== RELACIONES ====

    %% --- Usuario ---
    Usuario "*" -- "1" TipoUsuario : tipo
    Usuario "1" -- "*" Pedido : realiza
    Usuario "*" -- "1" Local : trabaja_en

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

   
