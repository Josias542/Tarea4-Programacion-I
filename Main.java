/**
 * Main - Punto de entrada de la aplicación.
 * Lanza la ventana de Login en el hilo de eventos de Swing.
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            factory.VentanaFactory.abrirLogin();
        });
    }
}
