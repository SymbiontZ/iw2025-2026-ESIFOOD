package es.uca.esifoodteam;

import es.uca.esifoodteam.usuarios.models.TipoUsuario;
import es.uca.esifoodteam.usuarios.models.Usuario;

public final class TestData {

    private TestData() {}

    public static Usuario usuarioCliente(TipoUsuario tipo, String email) {
        Usuario u = new Usuario();
        u.setTipo_id(tipo);
        u.setNombre("Cliente IT");
        u.setEmail(email);
        u.setPass("password");
        u.setTelefono("000000000"); 
        u.setDireccion("Calle Test 1");
        u.setEsActivo(true);
        return u;
    }
}
 