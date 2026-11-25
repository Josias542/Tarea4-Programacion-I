package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import dao.UsuarioDAO;
import dao.UsuarioDAOMySQL;
import factory.VentanaFactory;
import model.Usuario;

/**
 * Login - Ventana de inicio de sesión (HERENCIA: extiende BaseFrame).
 * Campos: usuario y contraseña (oculta con JPasswordField).
 * Botones: Entrar y Registrarse.
 */
public class Login extends BaseFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOMySQL();

    private JTextField campoUsuario;
    private JPasswordField campoClave;
    private JButton botonEntrar;
    private JButton botonRegistrarse;

    public Login() {
        super("Inicio de Sesión");
        inicializarComponentes();
        armarLayout();
        configurarCerrar(true); // al presionar X, sale de la aplicación
        pack();
        centrar();
        setVisible(true);
    }

    private void inicializarComponentes() {
        campoUsuario = crearCampoTexto(20);
        campoClave = crearCampoContrasena(20);

        botonEntrar = crearBoton("Entrar");
        botonEntrar.addActionListener(this);

        botonRegistrarse = crearBoton("Registrarse");
        botonRegistrarse.addActionListener(this);
    }

    /**
     * Crea un panel con la etiqueta encima del campo de texto.
     */
    private JPanel crearFilaCampo(String etiquetaTexto, javax.swing.JComponent campo) {
        JPanel fila = new JPanel(new BorderLayout(0, 6));
        fila.setBackground(Color.WHITE);
        fila.setBorder(new EmptyBorder(0, 0, 4, 0));
        JLabel lbl = new JLabel(etiquetaTexto);
        lbl.setFont(FUENTE_NORMAL);
        lbl.setForeground(COLOR_TEXTO);
        fila.add(lbl, BorderLayout.NORTH);
        fila.add(campo, BorderLayout.SOUTH);
        return fila;
    }

    private void armarLayout() {
        // Formulario con dos filas (usuario y contraseña)
        JPanel formulario = new JPanel(new GridLayout(2, 1, 0, 16));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(20, 30, 20, 30));

        formulario.add(crearFilaCampo("Usuario:", campoUsuario));
        formulario.add(crearFilaCampo("Contraseña:", campoClave));

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(new EmptyBorder(0, 30, 20, 30));
        panelBotones.add(botonEntrar);
        panelBotones.add(botonRegistrarse);

        // Panel central blanco con borde
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(2, 2, 2, 2)));

        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(new EmptyBorder(24, 30, 0, 30));
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo);

        panelCentral.add(panelTitulo, BorderLayout.NORTH);
        panelCentral.add(formulario, BorderLayout.CENTER);
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        // Panel de fondo con margen
        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.setBackground(COLOR_FONDO);
        panelFondo.setBorder(new EmptyBorder(50, 60, 50, 60));
        panelFondo.add(panelCentral, BorderLayout.CENTER);

        add(panelFondo, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonEntrar) {
            entrar();
        } else if (e.getSource() == botonRegistrarse) {
            registrarse();
        }
    }

    private void entrar() {
        String usuario = campoUsuario.getText().trim();
        char[] clave = campoClave.getPassword();

        // Validación de campos vacíos
        if (usuario.isEmpty() || clave.length == 0) {
            mostrarError("Debe ingresar su usuario y contraseña, si no está registrado debe registrarse");
            return;
        }

        Usuario u = usuarioDAO.autenticar(usuario, new String(clave));
        if (u != null) {
            mostrarExito("Bienvenido, " + u.getNombre() + " " + u.getApellido());
            // Cierra el login y abre la pantalla principal (Fábrica)
            VentanaFactory.abrirPrincipalYCerrar(this);
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
        }
    }

    private void registrarse() {
        VentanaFactory.abrirRegistroYCerrar(this);
    }
}
