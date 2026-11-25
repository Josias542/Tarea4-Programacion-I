package model;

/**
 * Usuario - Extiende Persona (HERENCIA).
 * Representa un usuario registrado del sistema con credenciales de acceso.
 */
public class Usuario extends Persona {

    private int id;
    private String usuario;
    private String passwordHash;
    private byte[] salt;

    public Usuario() {
        super();
    }

    public Usuario(int id, String usuario, String nombre, String apellido,
                   String telefono, String correo, String passwordHash, byte[] salt) {
        super(nombre, apellido, telefono, correo);
        this.id = id;
        this.usuario = usuario;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    @Override
    public String getRol() {
        return "Usuario";
    }

    @Override
    public String toString() {
        return getUsuario() + " - " + getNombre() + " " + getApellido();
    }
}
