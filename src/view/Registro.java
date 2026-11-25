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
import model.Usuario;
import util.PasswordUtil;

/**
 * Registro - Ventana para crear o editar un usuario (HERENCIA: extiende BaseFrame).
 * Soporta dos modos:
 *   - Nuevo: crea un usuario con los 7 campos obligatorios.
 *   - Editar: precarga los datos de un usuario existente y los actualiza.
 *
 * Los campos se distribuyen en dos columnas, con la etiqueta encima de cada uno.
 */
public class Registro extends BaseFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOMySQL();
    private final Usuario usuarioEditar; // null si es registro nuevo
    private final java.lang.Runnable onExito;

    private JTextField campoUsuario;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoTelefono;
    private JTextField campoCorreo;
    private JPasswordField campoClave;
    private JPasswordField campoConfirmar;
    private JButton botonGuardar;
    private JButton botonVolver;

    public Registro() {
        this(null, null);
    }

    public Registro(Usuario usuarioEditar, java.lang.Runnable onExito) {
        super(usuarioEditar == null ? "Registro de Usuario" : "Actualizar Usuario");
        this.usuarioEditar = usuarioEditar;
        this.onExito = onExito;
        inicializarComponentes();
        armarLayout();
        configurarCerrarRegistro();
        if (usuarioEditar != null) {
            precargarDatos();
        }
        pack();
        centrar();
        setVisible(true);
    }

    private void inicializarComponentes() {
        campoUsuario = crearCampoTexto(20);
        campoNombre = crearCampoTexto(20);
        campoApellido = crearCampoTexto(20);
        campoTelefono = crearCampoTexto(20);
        campoCorreo = crearCampoTexto(20);
        campoClave = crearCampoContrasena(20);
        campoConfirmar = crearCampoContrasena(20);

        String textoBoton = (usuarioEditar == null) ? "Registrarse" : "Actualizar";
        botonGuardar = crearBoton(textoBoton);
        botonGuardar.addActionListener(this);

        // Desde el login se vuelve al inicio; desde Principal se cancela y se vuelve a la tabla
        botonVolver = crearBoton(onExito == null ? "Volver al inicio" : "Cancelar");
        botonVolver.addActionListener(this);
    }

    /**
     * Crea un panel con la etiqueta encima del campo de texto (más legible).
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

    /**
     * Nota de ayuda debajo de los campos. Cambia según el modo:
     * en registro nuevo todos los campos son obligatorios; en edición la
     * contraseña se puede dejar vacía para conservar la actual.
     */
    private JPanel crearNotaFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel nota = new JLabel(usuarioEditar == null
                ? "Todos los campos son obligatorios."
                : "Deje la contraseña vacía para conservarla.");
        nota.setFont(FUENTE_SUBTITULO);
        nota.setForeground(COLOR_TEXTO_SUAVE);
        panel.add(nota, BorderLayout.CENTER);
        return panel;
    }

    private void armarLayout() {
        // Panel del formulario con dos columnas de campos
        JPanel formulario = new JPanel(new GridLayout(7, 2, 20, 14));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(10, 10, 10, 10));

        formulario.add(crearFilaCampo("Usuario:", campoUsuario));
        formulario.add(crearFilaCampo("Nombre:", campoNombre));

        formulario.add(crearFilaCampo("Apellido:", campoApellido));
        formulario.add(crearFilaCampo("Teléfono:", campoTelefono));

        formulario.add(crearFilaCampo("Correo electrónico:", campoCorreo));
        formulario.add(crearFilaCampo(usuarioEditar == null ? "Contraseña:" : "Contraseña (opcional):", campoClave));

        formulario.add(crearFilaCampo(usuarioEditar == null ? "Confirmar contraseña:" : "Confirmar contraseña (opcional):", campoConfirmar));
        formulario.add(crearNotaFormulario());

        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(new EmptyBorder(20, 24, 16, 24));
        JLabel lblTitulo = new JLabel(usuarioEditar == null ? "Crear Nueva Cuenta" : "Editar Usuario");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(new EmptyBorder(0, 24, 24, 24));
        panelBotones.add(botonGuardar);
        panelBotones.add(botonVolver);

        // Panel central blanco con sombra visual (borde)
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(2, 2, 2, 2)));
        panelCentral.add(panelTitulo, BorderLayout.NORTH);
        panelCentral.add(formulario, BorderLayout.CENTER);
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        // Panel de fondo con margen
        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.setBackground(COLOR_FONDO);
        panelFondo.setBorder(new EmptyBorder(30, 40, 30, 40));
        panelFondo.add(panelCentral, BorderLayout.CENTER);

        add(panelFondo, BorderLayout.CENTER);
    }

    /**
     * Al cerrar con X: si venimos del login (onExito == null), vuelve al login;
     * si venimos de la pantalla principal, solo cierra esta ventana.
     */
    private void configurarCerrarRegistro() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
                if (onExito == null) {
                    factory.VentanaFactory.abrirLogin();
                }
            }
        });
    }

    private void precargarDatos() {
        campoUsuario.setText(usuarioEditar.getUsuario());
        campoUsuario.setEnabled(false); // el usuario no se puede cambiar al editar
        campoNombre.setText(usuarioEditar.getNombre());
        campoApellido.setText(usuarioEditar.getApellido());
        campoTelefono.setText(usuarioEditar.getTelefono());
        campoCorreo.setText(usuarioEditar.getCorreo());
        // los campos de contraseña quedan vacíos (opcionales al editar)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonGuardar) {
            guardar();
        } else if (e.getSource() == botonVolver) {
            volver();
        }
    }

    private void guardar() {
        String usuario = campoUsuario.getText().trim();
        String nombre = campoNombre.getText().trim();
        String apellido = campoApellido.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String correo = campoCorreo.getText().trim();
        char[] clave = campoClave.getPassword();
        char[] confirmar = campoConfirmar.getPassword();

        // Validación: los campos de datos son obligatorios en ambos modos
        if (usuario.isEmpty()) {
            mostrarError("El campo Usuario es obligatorio.");
            return;
        }
        if (nombre.isEmpty()) {
            mostrarError("El campo Nombre es obligatorio.");
            return;
        }
        if (apellido.isEmpty()) {
            mostrarError("El campo Apellido es obligatorio.");
            return;
        }
        if (telefono.isEmpty()) {
            mostrarError("El campo Teléfono es obligatorio.");
            return;
        }
        if (correo.isEmpty()) {
            mostrarError("El campo Correo electrónico es obligatorio.");
            return;
        }

        // Validación de contraseña según el modo:
        // - Nuevo: ambos campos son obligatorios y deben coincidir.
        // - Editar: si ambos están vacíos se conserva la contraseña actual;
        //   si se escribe uno, deben escribirse ambos y coincidir.
        if (usuarioEditar == null) {
            if (clave.length == 0) {
                mostrarError("El campo Contraseña es obligatorio.");
                return;
            }
            if (confirmar.length == 0) {
                mostrarError("El campo Confirmar contraseña es obligatorio.");
                return;
            }
            if (!java.util.Arrays.equals(clave, confirmar)) {
                mostrarError("Las contraseñas no coinciden. Inténtalo de nuevo.");
                return;
            }
        } else {
            boolean claveVacia = clave.length == 0;
            boolean confirmarVacia = confirmar.length == 0;

            if (claveVacia != confirmarVacia) {
                mostrarError("Debe completar ambos campos de contraseña o dejar ambos vacíos.");
                return;
            }
            if (!claveVacia && !java.util.Arrays.equals(clave, confirmar)) {
                mostrarError("Las contraseñas no coinciden. Inténtalo de nuevo.");
                return;
            }
        }

        boolean exito;
        if (usuarioEditar == null) {
            // MODO NUEVO: verificar unicidad y registrar
            if (usuarioDAO.existeUsuario(usuario)) {
                mostrarError("El usuario ya existe. Elige otro nombre de usuario.");
                return;
            }
            if (usuarioDAO.existeCorreo(correo)) {
                mostrarError("El correo ya está registrado. Usa otro correo.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setUsuario(usuario);
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setTelefono(telefono);
            nuevo.setCorreo(correo);
            nuevo.setPasswordHash(new String(clave));
            exito = usuarioDAO.registrar(nuevo);
        } else {
            // MODO EDITAR: actualizar datos del usuario seleccionado
            Usuario u = new Usuario();
            u.setId(usuarioEditar.getId());
            u.setUsuario(usuario);
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setTelefono(telefono);
            u.setCorreo(correo);
            exito = usuarioDAO.actualizar(u);

            // Si se escribió contraseña nueva, actualizarla con hash y salt
            if (exito && clave.length > 0) {
                exito = usuarioDAO.actualizarClave(u, clave);
            }
        }

        if (exito) {
            mostrarExito(usuarioEditar == null
                    ? "Usuario registrado con éxito."
                    : "Usuario actualizado con éxito.");
            if (onExito != null) {
                // Viene de "Nuevo" o "Actualizar" en Principal: refresca la tabla y cierra esta ventana
                onExito.run();
                dispose();
            } else {
                // Viene del login: cierra esta ventana y vuelve a mostrar el login
                dispose();
                factory.VentanaFactory.abrirLogin();
            }
        } else {
            mostrarError("No se pudo guardar el usuario. Inténtalo de nuevo.");
        }
    }

    private void volver() {
        dispose();
        // Si se abrió desde Principal, solo se cierra el formulario y se vuelve a la tabla.
        if (onExito == null) {
            factory.VentanaFactory.abrirLogin();
        }
    }
}
