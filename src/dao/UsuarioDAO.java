package dao;

import java.util.List;
import model.Usuario;

/**
 * UsuarioDAO - Interfaz (ABSTRACCIÓN + POLIMORFISMO).
 * Define el contrato de operaciones sobre usuarios. Cualquier implementación
 * (MySQL, archivo, en memoria) puede usarse sin cambiar el resto del sistema.
 */
public interface UsuarioDAO {

    boolean existeUsuario(String usuario);

    boolean existeCorreo(String correo);

    boolean registrar(Usuario usuario);

    /**
     * Autentica un usuario. Devuelve el Usuario si las credenciales son correctas,
     * o null si no.
     */
    Usuario autenticar(String usuario, String password);

    boolean actualizarClave(Usuario usuario, char[] nuevaClave);

    List<Usuario> listar();

    boolean actualizar(Usuario usuario);

    boolean eliminar(int id);
}
