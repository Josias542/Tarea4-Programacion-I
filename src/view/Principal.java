package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import dao.UsuarioDAO;
import dao.UsuarioDAOMySQL;
import factory.VentanaFactory;
import model.Usuario;

/**
 * Principal - Pantalla "Clientes Registrados" (HERENCIA: extiende BaseFrame).
 * Se abre tras un login exitoso y cierra la ventana de login.
 * Muestra una tabla con los usuarios y permite crear, actualizar y eliminar.
 */
public class Principal extends BaseFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOMySQL();
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private List<Usuario> usuarios; // paralelo a las filas de la tabla

    public Principal() {
        super("Clientes Registrados");
        inicializarComponentes();
        armarLayout();
        configurarCerrar(false); // al presionar X, solo cierra esta ventana
        cargarTabla();
        pack();
        centrar();
        setVisible(true);
    }

    private void inicializarComponentes() {
        String[] columnas = {"Nombre", "Apellido", "Teléfono", "Correo electrónico", "Usuario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // la tabla no se edita directamente
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setFont(FUENTE_BOTON);
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setFont(FUENTE_NORMAL);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setIntercellSpacing(new java.awt.Dimension(0, 0));
    }

    private void armarLayout() {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                new javax.swing.border.EmptyBorder(8, 8, 8, 8)));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(12, 0, 12, 0));

        JButton btnNuevo = crearBoton("Nuevo");
        btnNuevo.addActionListener(this);

        JButton btnActualizar = crearBoton("Actualizar");
        btnActualizar.addActionListener(this);

        JButton btnEliminar = crearBotonPeligro("Eliminar");
        btnEliminar.addActionListener(this);

        JButton btnCerrarSesion = crearBoton("Cerrar sesión");
        btnCerrarSesion.addActionListener(this);

        panelBotones.add(btnNuevo);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrarSesion);

        // Encabezado visible dentro de la ventana (además del título de la ventana)
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setBorder(new EmptyBorder(18, 24, 8, 24));

        JLabel lblTitulo = new JLabel("Clientes Registrados");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panelTitulo.add(lblTitulo);

        add(panelTitulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Carga los usuarios de la BD en la tabla (se llama tras cada cambio).
     */
    public void cargarTabla() {
        usuarios = usuarioDAO.listar();
        modeloTabla.setRowCount(0);
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getNombre(),
                u.getApellido(),
                u.getTelefono(),
                u.getCorreo(),
                u.getUsuario()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("Nuevo".equals(comando)) {
            nuevo();
        } else if ("Actualizar".equals(comando)) {
            actualizar();
        } else if ("Eliminar".equals(comando)) {
            eliminar();
        } else if ("Cerrar sesión".equals(comando)) {
            cerrarSesion();
        }
    }

    private void nuevo() {
        // Abre el registro de un nuevo usuario sin cerrar la principal
        new Registro(null, this::cargarTabla);
    }

    private void actualizar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un usuario de la tabla para actualizar.");
            return;
        }
        Usuario u = usuarios.get(fila);
        new Registro(u, this::cargarTabla);
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un usuario de la tabla para eliminar.");
            return;
        }
        Usuario u = usuarios.get(fila);

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar a " + u.getNombre() + " " + u.getApellido() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean exito = usuarioDAO.eliminar(u.getId());
            if (exito) {
                mostrarExito("Usuario eliminado correctamente.");
                cargarTabla(); // la tabla se refresca sola
            } else {
                mostrarError("No se pudo eliminar el usuario.");
            }
        }
    }

    private void cerrarSesion() {
        dispose();
        VentanaFactory.abrirLogin();
    }
}
