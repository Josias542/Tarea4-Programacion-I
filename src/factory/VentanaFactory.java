package factory;

import view.BaseFrame;
import view.Login;
import view.Principal;
import view.Registro;

/**
 * VentanaFactory - Patrón FÁBRICA.
 * Centraliza la creación de las ventanas del sistema (Login, Registro y
 * Principal) y el control de navegación entre ellas, evitando que las vistas
 * se instancien directamente entre sí.
 */
public class VentanaFactory {

    private VentanaFactory() {
        // Clase de fábrica: no se instancia
    }

    public static Login crearLogin() {
        return new Login();
    }

    public static Registro crearRegistro() {
        return new Registro();
    }

    public static Principal crearPrincipal() {
        return new Principal();
    }

    /**
     * Abre el login como ventana principal de la aplicación.
     */
    public static void abrirLogin() {
        crearLogin().setVisible(true);
    }

    /**
     * Abre la ventana principal y cierra la ventana actual (login exitoso).
     */
    public static void abrirPrincipalYCerrar(BaseFrame actual) {
        crearPrincipal().setVisible(true);
        actual.dispose();
    }

    /**
     * Abre el registro y cierra la ventana actual (registrarse desde el login).
     */
    public static void abrirRegistroYCerrar(BaseFrame actual) {
        crearRegistro().setVisible(true);
        actual.dispose();
    }

    /**
     * Abre el login y cierra la ventana actual (cerrar sesión).
     */
    public static void abrirLoginYCerrar(BaseFrame actual) {
        crearLogin().setVisible(true);
        actual.dispose();
    }
}
