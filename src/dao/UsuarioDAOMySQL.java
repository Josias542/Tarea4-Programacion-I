package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import util.ConexionDB;
import util.PasswordUtil;

/**
 * UsuarioDAOMySQL - Implementación de UsuarioDAO sobre MySQL.
 * POLIMORFISMO: implementa los métodos de la interfaz UsuarioDAO.
 * Todas las consultas usan PreparedStatement para EVITAR INYECCIÓN SQL.
 *
 * La conexión se obtiene del Singleton ConexionDB y no se cierra en cada
 * método; solo se cierran el PreparedStatement y el ResultSet.
 *
 * Columnas de la tabla usuarios:
 * idUser, UserName, Nombre, Apellido, Telefono, Email, Password
 */
public class UsuarioDAOMySQL implements UsuarioDAO {

    private static final String SQL_EXISTE_USUARIO
            = "SELECT COUNT(*) FROM usuarios WHERE UserName = ?";
    private static final String SQL_EXISTE_CORREO
            = "SELECT COUNT(*) FROM usuarios WHERE Email = ?";
    private static final String SQL_REGISTRAR
            = "INSERT INTO usuarios (UserName, Nombre, Apellido, Telefono, Email, Password) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_AUTENTICAR
            = "SELECT idUser, UserName, Nombre, Apellido, Telefono, Email, Password "
            + "FROM usuarios WHERE UserName = ? AND deleted_at IS NULL";
    private static final String SQL_LISTAR
            = "SELECT idUser, UserName, Nombre, Apellido, Telefono, Email, Password "
            + "FROM usuarios WHERE deleted_at IS NULL ORDER BY Apellido, Nombre";
    private static final String SQL_ACTUALIZAR
            = "UPDATE usuarios SET Nombre = ?, Apellido = ?, Telefono = ?, Email = ?, UserName = ? "
            + "WHERE idUser = ?";
    private static final String SQL_ACTUALIZAR_CLAVE
            = "UPDATE usuarios SET Password = ? WHERE idUser = ?";
    private static final String SQL_ELIMINAR_LOGICO
            = "UPDATE usuarios SET deleted_at = CURRENT_TIMESTAMP WHERE idUser = ?";

    @Override
    public boolean existeUsuario(String usuario) {
        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_EXISTE_USUARIO)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean existeCorreo(String correo) {
        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_EXISTE_CORREO)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean registrar(Usuario usuario) {
        if (existeUsuario(usuario.getUsuario())) {
            return false;
        }
        if (existeCorreo(usuario.getCorreo())) {
            return false;
        }

        byte[] salt = PasswordUtil.generarSalt();
        byte[] hash = PasswordUtil.hashPassword(usuario.getPasswordHash().toCharArray(), salt);

        // Guardamos salt:hash en la columna Password
        String passwordAlmacenar = PasswordUtil.toHex(salt) + ":" + PasswordUtil.toHex(hash);

        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_REGISTRAR)) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellido());
            ps.setString(4, usuario.getTelefono());
            ps.setString(5, usuario.getCorreo());
            ps.setString(6, passwordAlmacenar);

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario autenticar(String usuario, String password) {
        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_AUTENTICAR)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String passwordGuardada = rs.getString("Password");

                    // La columna Password tiene formato salt:hash
                    String[] partes = passwordGuardada.split(":");
                    if (partes.length == 2) {
                        byte[] saltBytes = PasswordUtil.fromHex(partes[0]);
                        byte[] hashBytes = PasswordUtil.fromHex(partes[1]);

                        if (PasswordUtil.verificar(password.toCharArray(), saltBytes, hashBytes)) {
                            Usuario u = new Usuario();
                            u.setId(rs.getInt("idUser"));
                            u.setUsuario(rs.getString("UserName"));
                            u.setNombre(rs.getString("Nombre"));
                            u.setApellido(rs.getString("Apellido"));
                            u.setTelefono(rs.getString("Telefono"));
                            u.setCorreo(rs.getString("Email"));
                            u.setPasswordHash(passwordGuardada);
                            u.setSalt(saltBytes);
                            return u;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("idUser"));
                u.setUsuario(rs.getString("UserName"));
                u.setNombre(rs.getString("Nombre"));
                u.setApellido(rs.getString("Apellido"));
                u.setTelefono(rs.getString("Telefono"));
                u.setCorreo(rs.getString("Email"));
                u.setPasswordHash(rs.getString("Password"));

                // Extraer salt de la columna Password (formato salt:hash)
                String[] partes = rs.getString("Password").split(":");
                if (partes.length == 2) {
                    u.setSalt(PasswordUtil.fromHex(partes[0]));
                }

                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getTelefono());
            ps.setString(4, usuario.getCorreo());
            ps.setString(5, usuario.getUsuario());
            ps.setInt(6, usuario.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la contraseña de un usuario (usado al editar y cambiar la clave).
     */
    public boolean actualizarClave(Usuario usuario, char[] nuevaClave) {
        byte[] salt = PasswordUtil.generarSalt();
        byte[] hash = PasswordUtil.hashPassword(nuevaClave, salt);
        String passwordAlmacenar = PasswordUtil.toHex(salt) + ":" + PasswordUtil.toHex(hash);

        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_ACTUALIZAR_CLAVE)) {
            ps.setString(1, passwordAlmacenar);
            ps.setInt(2, usuario.getId());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Borrado lógico: se marca deleted_at en lugar de eliminar la fila
        Connection conn = ConexionDB.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(SQL_ELIMINAR_LOGICO)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
