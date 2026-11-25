package model;

/**
 * Persona - Clase abstracta (ABSTRACCIÓN).
 * Define la estructura común de una persona en el sistema. No se puede
 * instanciar directamente; solo sirve como base de Usuario.
 */
public abstract class Persona {

    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;

    public Persona() {
    }

    public Persona(String nombre, String apellido, String telefono, String correo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
    }

    // ENCAPSULAMIENTO: atributos privados con getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Método abstracto que cada subclase debe implementar (POLIMORFISMO).
     */
    public abstract String getRol();

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
