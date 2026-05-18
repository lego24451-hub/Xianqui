package xianqui;

import javax.swing.*;
import java.awt.*;

public class PanelInicio extends JPanel {

    
    static final Color COLOR_FONDO  = new Color(0x0A0400);
    static final Color COLOR_DORADO  = new Color(0xD4A030);
    static final Color COLOR_DORADO_TENUE  = new Color(0x5A3A12);
    static final Color COLOR_BOTON_FONDO = new Color(0xB86E10);
    static final Color COLOR_BOTON_TEXTO = new Color(0xFFE090);

    private final AppFrame  appFrame;
    private final IStorage  almacenamiento = StorageManager.getInstance();

    public PanelInicio(AppFrame appFrame) {
        almacenamiento.crearPlayer(new Player("bot","1"));
        almacenamiento.crearPlayer(new Player("pla","1"));
        this.appFrame = appFrame;
        setBackground(COLOR_FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));
        construirInterfaz();
    }

   
    static String validarPassword(String password) {
        if (password.length() < 5)
            return "El password debe tener al menos 5 caracteres.";
        if (!password.chars().anyMatch(Character::isUpperCase))
            return "El password debe tener al menos una letra mayúscula.";
        if (!password.chars().anyMatch(Character::isLowerCase))
            return "El password debe tener al menos una letra minúscula.";
        if (!password.chars().anyMatch(Character::isDigit))
            return "El password debe tener al menos un número.";
        return null;
    }

    private void construirInterfaz() {
        JLabel etiquetaSubtitulo = crearEtiqueta(
            "XIANGQI  ·  AJEDREZ CHINO",
            new Font("SansSerif", Font.PLAIN, 12),
            COLOR_DORADO_TENUE);
        JLabel etiquetaMenu = crearEtiqueta(
            "─── MENÚ DE INICIO ───",
            new Font("SansSerif", Font.BOLD, 14),
            COLOR_DORADO);

        JButton botonLogin = crearBoton("  1.  LOG IN  ");
        JButton botonCrear = crearBoton("  2.  CREAR PLAYER  ");
        JButton botonSalir = crearBoton("  3.  SALIR  ");

        botonLogin.addActionListener(e -> mostrarDialogoLogin());
        botonCrear.addActionListener(e -> mostrarDialogoCrear());
        botonSalir.addActionListener(e -> System.exit(0));

        add(Box.createVerticalStrut(4));
        add(etiquetaSubtitulo);
        add(Box.createVerticalStrut(30));
        add(etiquetaMenu);
        add(Box.createVerticalStrut(18));
        add(botonLogin);
        add(Box.createVerticalStrut(10));
        add(botonCrear);
        add(Box.createVerticalStrut(10));
        add(botonSalir);
    }
    private void mostrarDialogoLogin() {
        JDialog dialogo = crearDialogo("Log In");

        JPanel panelFormulario  = crearPanelFormulario();
        JTextField campoUsuario = crearCampoTexto();
        JPasswordField campoClave   = crearCampoContrasena();
        JLabel etiquetaError = crearEtiquetaError();

        agregarFilaFormulario(panelFormulario, 0, "Username:", campoUsuario);
        agregarFilaContrasena(panelFormulario, 1, "Password:", campoClave);
        agregarComponenteEnFila(panelFormulario, 2, etiquetaError);

        JButton botonIngresar = crearBoton("Ingresar");
        agregarComponenteEnFila(panelFormulario, 3, botonIngresar);

        Runnable accion = () -> {
            String usuario    = campoUsuario.getText().trim();
            String contrasena = new String(campoClave.getPassword());
            Player found      = almacenamiento.buscarPlayer(usuario);
            if (found == null || !found.getPassword().equals(contrasena) || !found.isActivo()) {
                etiquetaError.setText("Usuario o password incorrecto.");
            } else {
                dialogo.dispose();
                appFrame.mostrarMenu(found);
            }
        };

        botonIngresar.addActionListener(e -> accion.run());
        campoClave.addActionListener(e -> accion.run());

        mostrarDialogo(dialogo, panelFormulario);
    }

    private void mostrarDialogoCrear() {
        JDialog dialogo = crearDialogo("Crear Player");

        JPanel panelFormulario = crearPanelFormulario();
        JTextField campoUsuario = crearCampoTexto();
        JPasswordField campoClave = crearCampoContrasena();
        JLabel etiquetaError  = crearEtiquetaError();

        agregarFilaFormulario(panelFormulario, 0, "Username:", campoUsuario);
        agregarFilaContrasena(panelFormulario, 1, "Password:", campoClave);

        
        JLabel hint = new JLabel("Mín. 5 chars · mayúscula · minúscula · número");
        hint.setForeground(COLOR_DORADO_TENUE);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 10));
        agregarComponenteEnFila(panelFormulario, 2, hint);

        agregarComponenteEnFila(panelFormulario, 3, etiquetaError);

        JButton botonCrear = crearBoton("Crear");
        agregarComponenteEnFila(panelFormulario, 4, botonCrear);

        botonCrear.addActionListener(e -> {
            String usuario    = campoUsuario.getText().trim();
            String contrasena = new String(campoClave.getPassword());
            if (usuario.isEmpty()) {
                etiquetaError.setText("El username no puede estar vacío.");
                return;
            }
            String errorPassword = validarPassword(contrasena);
            if (errorPassword != null) {
                etiquetaError.setText(errorPassword);
                return;
            }
            Player nuevo = new Player(usuario, contrasena);
            if (!almacenamiento.crearPlayer(nuevo)) {
                etiquetaError.setText("El username ya existe, elige otro.");
                return;
            }
            dialogo.dispose();
            appFrame.mostrarMenu(nuevo);
        });

        mostrarDialogo(dialogo, panelFormulario);
    }

    private void mostrarDialogo(JDialog dialogo, JPanel contenido) {
        dialogo.setContentPane(contenido);
        dialogo.pack();
        dialogo.setLocationRelativeTo(appFrame);
        dialogo.setVisible(true);
    }

    static JButton crearBoton(String texto) {
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

    static JButton crearBotonPeligro(String texto) {
        JButton boton = crearBoton(texto);
        boton.setBackground(new Color(0x7A1414));
        boton.setForeground(new Color(0xFFCCCC));
        return boton;
    }

    static JLabel crearEtiqueta(String texto, Font fuente, Color color) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiqueta;
    }

    static JLabel crearEtiquetaError() {
        JLabel etiqueta = new JLabel(" ");
        etiqueta.setForeground(new Color(0xFF5533));
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 11));
        return etiqueta;
    }

    static JTextField crearCampoTexto() {
        JTextField campo = new JTextField(16);
        aplicarEstiloCampo(campo);
        return campo;
    }

    static JPasswordField crearCampoContrasena() {
        JPasswordField campo = new JPasswordField(16);
        aplicarEstiloCampo(campo);
        return campo;
    }

    private static void aplicarEstiloCampo(JTextField campo) {
        campo.setBackground(new Color(0x1A0E04));
        campo.setForeground(COLOR_DORADO);
        campo.setCaretColor(COLOR_DORADO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BOTON_FONDO, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    static JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        return panel;
    }

    static void agregarFilaFormulario(JPanel panel, int fila, String texto, JTextField campo) {
        GridBagConstraints gbc = crearGBC(0, fila, 1);
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(COLOR_DORADO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(etiqueta, gbc);
        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    static void agregarFilaContrasena(JPanel panel, int fila, String texto, JPasswordField campo) {
        // Label izquierdo
        GridBagConstraints gbcLabel = crearGBC(0, fila, 1);
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(COLOR_DORADO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(etiqueta, gbcLabel);

        
        JPanel contenedor = new JPanel(new BorderLayout(4, 0));
        contenedor.setBackground(COLOR_FONDO);

        JButton botonOjo = new JButton("👁");
        botonOjo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        botonOjo.setBackground(COLOR_BOTON_FONDO);
        botonOjo.setForeground(COLOR_BOTON_TEXTO);
        botonOjo.setFocusPainted(false);
        botonOjo.setBorderPainted(false);
        botonOjo.setOpaque(true);
        botonOjo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonOjo.setMargin(new Insets(2, 6, 2, 6));
        botonOjo.setToolTipText("Mostrar/ocultar contraseña");

        
        final char[] echoOriginal = { campo.getEchoChar() };
        botonOjo.addActionListener(e -> {
            if (campo.getEchoChar() == 0) {
                campo.setEchoChar(echoOriginal[0]);
                botonOjo.setToolTipText("Mostrar contraseña");
            } else {
                campo.setEchoChar((char) 0);
                botonOjo.setToolTipText("Ocultar contraseña");
            }
        });

        contenedor.add(campo,    BorderLayout.CENTER);
        contenedor.add(botonOjo, BorderLayout.EAST);

        GridBagConstraints gbcCampo = crearGBC(1, fila, 1);
        panel.add(contenedor, gbcCampo);
    }

    static void agregarComponenteEnFila(JPanel panel, int fila, Component componente) {
        panel.add(componente, crearGBC(0, fila, 2));
    }

    private static GridBagConstraints crearGBC(int col, int fila, int ancho) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = fila;
        gbc.gridwidth = ancho;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        return gbc;
    }

    private JDialog crearDialogo(String titulo) {
        JDialog d = new JDialog(appFrame, titulo, true);
        d.setResizable(false);
        return d;
    }
}