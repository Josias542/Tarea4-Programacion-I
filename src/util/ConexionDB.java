package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConexionDB - Patrón Singleton.
 * Mantiene una única conexión a MySQL durante la ejecución de la aplicación.
 * Los DAO obtienen la conexión desde aquí.
 */
public class ConexionDB {

    // Conexión local a MySQL (puerto 3306)
    private static final String URL =
            "jdbc:mysql://localhost:3306/tarea4_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CLAVE = "NuevaClaveFuerte1";

    private static Connection conexion = null;

    // Constructor privado: evita instanciación desde fuera (Singleton)
    private ConexionDB() {
    }

    /**
     * Devuelve la única instancia de conexión, creándola si es necesario.
     */
    public static Connection getConexion() {
        if (conexion == null) {
            try {
                conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            } catch (SQLException e) {
                System.err.println("Error al conectar con MySQL: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("No se pudo establecer la conexión con la base de datos.", e);
            }
        }
        return conexion;
    }

    /**
     * Cierra la conexión cuando la aplicación termina.
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
