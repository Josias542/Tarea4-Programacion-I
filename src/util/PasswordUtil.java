package util;

import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PasswordUtil - Utilidad para hash de contraseñas.
 * Usa PBKDF2 con HMAC-SHA-256 (java.security / javax.crypto), SIN librerías externas.
 * Cada contraseña se guarda con su propio salt aleatorio, por lo que dos usuarios
 * con la misma contraseña tendrán hashes diferentes.
 */
public class PasswordUtil {

    private static final int ITERACIONES = 65536;
    private static final int LONGITUD_HASH = 256; // bits
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private PasswordUtil() {
        // Clase de utilidad: no se instancia
    }

    /**
     * Genera un salt aleatorio de 16 bytes.
     */
    public static byte[] generarSalt() {
        byte[] salt = new byte[16];
        ALEATORIO.nextBytes(salt);
        return salt;
    }

    /**
     * Calcula el hash PBKDF2 de la contraseña usando el salt dado.
     */
    public static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec espec = new PBEKeySpec(password, salt, ITERACIONES, LONGITUD_HASH);
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return fabrica.generateSecret(espec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el hash de la contraseña.", e);
        }
    }

    /**
     * Convierte bytes a cadena hexadecimal (para almacenar en la BD).
     */
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Convierte una cadena hexadecimal de vuelta a bytes.
     */
    public static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Verifica si la contraseña ingresada coincide con el hash almacenado.
     * Comparación en tiempo constante para evitar ataques de temporización.
     */
    public static boolean verificar(char[] password, byte[] salt, byte[] hashEsperado) {
        byte[] hashCalculado = hashPassword(password, salt);
        if (hashCalculado.length != hashEsperado.length) {
            return false;
        }
        int resultado = 0;
        for (int i = 0; i < hashCalculado.length; i++) {
            resultado |= hashCalculado[i] ^ hashEsperado[i];
        }
        return resultado == 0;
    }
}
