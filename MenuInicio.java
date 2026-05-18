package xianqui;

import javax.swing.*;
import java.awt.*;

public class MenuInicio extends JFrame {

    private static final Color COLOR_FONDO = new Color(0x0A0400);
    private static final Color COLOR_DORADO = new Color(0xD4A030);
    private static final Color COLOR_DORADO_TENUE = new Color(0x5A3A12);
    private static final Color COLOR_BOTON_FONDO = new Color(0xB86E10);
    private static final Color COLOR_BOTON_TEXTO = new Color(0xFFE090);

    private final IStorage almacenamiento = StorageManager.getInstance();

    public MenuInicio() {
        super("Xiangqi - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
        pack();
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel panelRaiz = new JPanel();
        panelRaiz.setBackground(COLOR_FONDO);
        panelRaiz.setLayout(new BoxLayout(panelRaiz, BoxLayout.Y_AXIS));
        panelRaiz.setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));

        JLabel etiquetaSubtitulo = crearEtiqueta(
            "XIANGQI  ·  AJEDREZ CHINO",
            new Font("SansSerif", Font.PLAIN, 12),
            COLOR_DORADO_TENUE);
        JLabel etiquetaMenu = crearEtiqueta(
            "─── MENÚ DE INICIO ───",
            new Font("SansSerif", Font.BOLD, 14),
            COLOR_DORADO);

        JButton botonLogin= crearBoton("  1.  LOG IN  ");
        JButton botonCrear= crearBoton("  2.  CREAR PLAYER  ");
        JButton botonSalir= crearBoton("  3.  SALIR  ");

        botonLogin.addActionListener(evento -> mostrarDialogoLogin());
        botonCrear.addActionListener(evento -> mostrarDialogoCrear());
        botonSalir.addActionListener(evento -> System.exit(0));

        panelRaiz.add(Box.createVerticalStrut(4));
        panelRaiz.add(etiquetaSubtitulo);
        panelRaiz.add(Box.createVerticalStrut(30));
        panelRaiz.add(etiquetaMenu);
        panelRaiz.add(Box.createVerticalStrut(18));
        panelRaiz.add(botonLogin);
        panelRaiz.add(Box.createVerticalStrut(10));
        panelRaiz.add(botonCrear);
        panelRaiz.add(Box.createVerticalStrut(10));
        panelRaiz.add(botonSalir);

        setContentPane(panelRaiz);
    }

    
    private void mostrarDialogoLogin() {
        JDialog dialogo = crearDialogo("Log In");

        JPanel panelFormulario  = crearPanelFormulario();
        JTextField campoUsuario = crearCampoTexto();
        JPasswordField campoContrasena = crearCampoContrasena();
        JLabel etiquetaError    = crearEtiquetaError();

        agregarFilaFormulario(panelFormulario, 0, "Username:", campoUsuario);
        agregarFilaFormulario(panelFormulario, 1, "Password:", campoContrasena);
        agregarComponenteEnFila(panelFormulario, 2, etiquetaError);

        JButton botonIngresar = crearBoton("Ingresar");
        agregarComponenteEnFila(panelFormulario, 3, botonIngresar);

        Runnable accionLogin = () -> {
            String usuario= campoUsuario.getText().trim();
            String contrasena= new String(campoContrasena.getPassword());
            Player playerEncontrado = almacenamiento.buscarPlayer(usuario);
            if (playerEncontrado == null
                    || !playerEncontrado.getPassword().equals(contrasena)
                    || !playerEncontrado.isActivo()) {
                etiquetaError.setText("Usuario o password incorrecto.");
            } else {
                dialogo.dispose();
                irAMenuPrincipal(playerEncontrado);
            }
        };

        botonIngresar.addActionListener(evento -> accionLogin.run());
        campoContrasena.addActionListener(evento -> accionLogin.run());

        dialogo.setContentPane(panelFormulario);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    
    private void mostrarDialogoCrear() {
        JDialog dialogo = crearDialogo("Crear Player");

        JPanel panelFormulario  = crearPanelFormulario();
        JTextField campoUsuario = crearCampoTexto();
        JPasswordField campoContrasena = crearCampoContrasena();
        JLabel etiquetaError    = crearEtiquetaError();

        agregarFilaFormulario(panelFormulario, 0, "Username:", campoUsuario);
        agregarFilaFormulario(panelFormulario, 1, "Password (5 chars):", campoContrasena);
        agregarComponenteEnFila(panelFormulario, 2, etiquetaError);

        JButton botonCrear = crearBoton("Crear");
        agregarComponenteEnFila(panelFormulario, 3, botonCrear);

        botonCrear.addActionListener(evento -> {
            String usuario    = campoUsuario.getText().trim();
            String contrasena = new String(campoContrasena.getPassword());

            if (usuario.isEmpty()) {
                etiquetaError.setText("El username no puede estar vacío.");
                return;
            }
            if (contrasena.length() != 5) {
                etiquetaError.setText("El password debe ser exactamente 5 caracteres.");
                return;
            }
            Player nuevoPlayer = new Player(usuario, contrasena);
            if (!almacenamiento.crearPlayer(nuevoPlayer)) {
                etiquetaError.setText("El username ya existe, elige otro.");
                return;
            }
            dialogo.dispose();
            irAMenuPrincipal(nuevoPlayer);
        });

        dialogo.setContentPane(panelFormulario);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

   
    void irAMenuPrincipal(Player player) {
        setVisible(false);
        new MenuPrincipal(player, this).setVisible(true);
    }

   
    JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setBackground(COLOR_BOTON_FONDO);
        boton.setForeground(COLOR_BOTON_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(300, 44));
        return boton;
    }

    private JLabel crearEtiqueta(String texto, Font fuente, Color color) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiqueta;
    }

    private JDialog crearDialogo(String titulo) {
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setResizable(false);
        return dialogo;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        return panel;
    }

    JTextField crearCampoTexto() {
        JTextField campo = new JTextField(16);
        aplicarEstiloCampo(campo);
        return campo;
    }

    JPasswordField crearCampoContrasena() {
        JPasswordField campo = new JPasswordField(16);
        aplicarEstiloCampo(campo);
        return campo;
    }

    private void aplicarEstiloCampo(JTextField campo) {
        campo.setBackground(new Color(0x1A0E04));
        campo.setForeground(COLOR_DORADO);
        campo.setCaretColor(COLOR_DORADO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BOTON_FONDO, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    JLabel crearEtiquetaError() {
        JLabel etiqueta = new JLabel(" ");
        etiqueta.setForeground(new Color(0xFF5533));
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return etiqueta;
    }

    void agregarFilaFormulario(JPanel panel, int fila, String textoEtiqueta, JTextField campo) {
        GridBagConstraints restricciones = crearRestriccionesGBC(0, fila, 1);
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setForeground(COLOR_DORADO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(etiqueta, restricciones);
        restricciones.gridx = 1;
        panel.add(campo, restricciones);
    }

    void agregarComponenteEnFila(JPanel panel, int fila, java.awt.Component componente) {
        GridBagConstraints restricciones = crearRestriccionesGBC(0, fila, 2);
        panel.add(componente, restricciones);
    }

    private GridBagConstraints crearRestriccionesGBC(int columna, int fila, int anchoColumnas) {
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = columna;
        restricciones.gridy = fila;
        restricciones.gridwidth = anchoColumnas;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.insets= new Insets(6, 6, 6, 6);
        return restricciones;
    }
}