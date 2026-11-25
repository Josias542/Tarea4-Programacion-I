package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * BaseFrame - Clase base de todas las ventanas (HERENCIA).
 * Extiende JFrame y centraliza la configuración común (título, colores,
 * fuentes y estilos) de Login, Registro y Principal.
 */
public abstract class BaseFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    protected static final Color COLOR_PRIMARIO = new Color(0x2563EB);
    protected static final Color COLOR_PRIMARIO_HOVER = new Color(0x1D4ED8);
    protected static final Color COLOR_FONDO = new Color(0xF8FAFC);
    protected static final Color COLOR_TEXTO = new Color(0x1E293B);
    protected static final Color COLOR_TEXTO_SUAVE = new Color(0x64748B);
    protected static final Color COLOR_BOTON = new Color(0x2563EB);
    protected static final Color COLOR_BOTON_TEXTO = Color.WHITE;
    protected static final Color COLOR_PELIGRO = new Color(0xDC2626);
    protected static final Color COLOR_PELIGRO_HOVER = new Color(0xB91C1C);
    protected static final Color COLOR_BORDE = new Color(0xCBD5E1);

    protected static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    protected static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 14);
    protected static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    protected static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public BaseFrame(String titulo) {
        super(titulo);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);
    }

    /**
     * Configura el comportamiento al presionar el botón X de la ventana.
     * Si salirDelSistema es true, cierra toda la aplicación.
     */
    protected void configurarCerrar(boolean salirDelSistema) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (salirDelSistema) {
                    System.exit(0);
                } else {
                    dispose();
                }
            }
        });
    }

    protected void centrar() {
        setLocationRelativeTo(null);
    }

    protected void mostrar() {
        pack();
        centrar();
        setVisible(true);
    }

    protected JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_NORMAL);
        etiqueta.setForeground(COLOR_TEXTO);
        return etiqueta;
    }

    protected JLabel crearEtiquetaSuave(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_SUBTITULO);
        etiqueta.setForeground(COLOR_TEXTO_SUAVE);
        return etiqueta;
    }

    protected JTextField crearCampoTexto(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        campo.setPreferredSize(new Dimension(260, 38));
        return campo;
    }

    protected JPasswordField crearCampoContrasena(int columnas) {
        JPasswordField campo = new JPasswordField(columnas);
        campo.setFont(FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        campo.setPreferredSize(new Dimension(260, 38));
        return campo;
    }

    protected JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_BOTON);
        boton.setForeground(COLOR_BOTON_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Efecto hover sencillo
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PRIMARIO_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTON);
            }
        });
        return boton;
    }

    protected JButton crearBotonPeligro(String texto) {
        JButton boton = crearBoton(texto);
        boton.setBackground(COLOR_PELIGRO);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PELIGRO_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PELIGRO);
            }
        });
        return boton;
    }

    protected void mostrarError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje, "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    protected void mostrarExito(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje, "Éxito",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
