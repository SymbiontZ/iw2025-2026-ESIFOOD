## Diagrama de Clases (Mermaid)

```mermaid
classDiagram 
        direction LR

        %% ==== DEFINICIÓN DE CLASES ====
    
        %% --- Usuarios ---
        class Usuario {
                String nombre 
                String email
                String telefono 
                String direccion
                Boolean esActivo
        }
        class TipoUsuario {
                String nombre 
        }
    
        class EstadoUsuario {
                String nombre 
        }
    
        %% --- Pedidos ---
        class Pedido {
                BigDecimal precio 
                LocalDateTime fechaHora 
                String observaciones 
        }
        class EstadoPedido {
                String nombre 
        }
        class ActualizacionPedido {
                DateTime fechaHora 
                String comentario 
        }
        class LineaPedido {
                BigDecimal precio_u 
                BigDecimal subtotal 
                Integer cantidad 
        }
    
        %% --- Productos y Menús ---
        class Producto {
                String nombre 
                String descripcion
                BigDecimal precio 
                boolean disponible
                String imagen_url
        }
        class Simple {
        }
        class Menu {
        }
        class TipoProducto {
                String nombre 
        }
        class Ingrediente {
                String nombre 
                Integer stock 
                BigDecimal precio
        }
    
        %% --- Establecimientos ---
        class Establecimiento {
                String nombre 
                String direccion 
        }
    
        %% ==== RELACIONES ====
    
        %% --- Usuario ---
        Usuario "*" -- "1" TipoUsuario : tipo
        Usuario "*" -- "1" EstadoUsuario : estado
        Usuario "1" -- "*" Pedido : realiza
    
        %% --- Pedido ---
        Pedido "1" -- "*" LineaPedido : contiene
        Pedido "1" -- "*" ActualizacionPedido : tiene_historial
        EstadoPedido "1" -- "*" ActualizacionPedido : aparece_en
    
        %% --- Productos ---
        Producto <|-- Menu
        Producto <|-- Simple
        Simple "*" --o "1" Menu : compuesto_por
        Producto "1" -- "*" LineaPedido : aparece_en
        Producto "*" -- "*" Ingrediente : usa
        Simple "*" -- "1" TipoProducto : pertenece_a
