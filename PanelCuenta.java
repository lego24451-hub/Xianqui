package xianqui;

import javax.swing.*;
import java.awt.*;

public class MiCuenta extends JFrame {

    private static final Color COLOR_FONDO = new Color(0x0A0400);
    private static final Color COLOR_DORADO = new Color(0xD4A030);
    private static final Color COLOR_DORADO_TENUE = new Color(0x5A3A12);

    private final Player player;
    private final MenuPrincipal menuPrincipal;
    private final MenuInicio  menuInicio;
    private final IStorage almacenamiento = StorageManager.getInstance();

    public MiCuenta(Player player, MenuPrincipal menuPrincipal, MenuInicio menuInicio) {
        super("Mi Cuenta - " + player.getUsername());
        this.player = player;
        this.menuPrincipal = menuPrincipal;
        this.menuInicio = menuInicio;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        construirInterfaz();
        pack();
        setLocationRelativeTo(menuPrincipal);
    }

    private void construirInterfaz() {
        JPanel panelRaiz = new JPanel();
        panelRaiz.setBackground(COLOR_FONDO);
        panelRaiz.setLayout(new BoxLayout(panelRaiz, BoxLayout.Y_AXIS));
        panelRaiz.setBorder(BorderFactory.createEmptyBorder(28, 50, 28, 50));

        panelRaiz.add(MenuPrincipal.crearEtiquetaCentrada(
            "MI CUENTA",
            new Font("SansSerif", Font.BOLD, 18),
            COLOR_DORADO));
        panelRaiz.add(Box.createVerticalStrut(20));

        JPanel panelInformacion = new JPanel(new GridLayout(0, 2, 12, 8));
        panelInformacion.setBackground(COLOR_FONDO);
        panelInformacion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0xB86E10), 1),
            "Información del jugador",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            COLOR_DORADO));

        agregarInfoAlPanel(panelInformacion, "Username:", player.getUsername());
        agregarInfoAlPanel(panelInformacion, "Puntos:", String.valueOf(player.getPuntos()));
        agregarInfoAlPanel(panelInformacion, "Fecha de ingreso:", player.getFechaIngreso());
        agregarInfoAlPanel(panelInformacion, "Estado:", player.isActivo() ? "Activo" : "Inactivo");

        panelRaiz.add(panelInformacion);
        panelRaiz.add(Box.createVerticalStrut(22));

        JButton botonCambiarPassword = menuInicio.crearBoton("Cambiar Password");
        JButton botonEliminarCuenta  = new JButton("Eliminar mi Cuenta");
        aplicarEstiloBotonPeligro(botonEliminarCuenta);

        botonCambiarPassword.addActionListener(evento -> mostrarCambiarPassword());
        botonEliminarCuenta.addActionListener (evento -> mostrarEliminarCuenta());

        panelRaiz.add(botonCambiarPassword);
        panelRaiz.add(Box.createVerticalStrut(10));
        panelRaiz.add(botonEliminarCuenta);

        setContentPane(panelRaiz);
    }

    
    private void mostrarCambiarPassword() {
        JDialog dialogo = new JDialog(this, "Cambiar Password", true);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_FONDO);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JPasswordField campoPasswordActual = menuInicio.crearCampoContrasena();
        JPasswordField campoPasswordNuevo  = menuInicio.crearCampoContrasena();
        JLabel etiquetaError = menuInicio.crearEtiquetaError();

        menuInicio.agregarFilaFormulario(panelFormulario, 0, "Password actual:", campoPasswordActual);
        menuInicio.agregarFilaFormulario(panelFormulario, 1, "Nuevo password (5 chars):", campoPasswordNuevo);
        menuInicio.agregarComponenteEnFila(panelFormulario, 2, etiquetaError);

        JButton botonConfirmar = menuInicio.crearBoton("Confirmar");
        menuInicio.agregarComponenteEnFila(panelFormulario, 3, botonConfirmar);

        botonConfirmar.addActionListener(evento -> {
            String passwordActual = new String(campoPasswordActual.getPassword());
            String passwordNuevo  = new String(campoPasswordNuevo.getPassword());

            if (!passwordActual.equals(player.getPassword())) {
                etiquetaError.setText("Password actual incorrecto.");
                return;
            }
            if (passwordNuevo.length() != 5) {
                etiquetaError.setText("El nuevo password debe tener exactamente 5 caracteres.");
                return;
            }
            player.setPassword(passwordNuevo);
            almacenamiento.guardarPlayer(player);
            JOptionPane.showMessageDialog(dialogo,
                "Password cambiado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        dialogo.setContentPane(panelFormulario);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void mostrarEliminarCuenta() {
        JDialog dialogo = new JDialog(this, "Eliminar Cuenta", true);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_FONDO);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel etiquetaAdvertencia = new JLabel(
            "Esta acción es irreversible.<br>Ingresa tu password para confirmar.",
            SwingConstants.CENTER);
        etiquetaAdvertencia.setForeground(new Color(0xFF5533));
        etiquetaAdvertencia.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPasswordField campoPassword = menuInicio.crearCampoContrasena();
        JLabel etiquetaError = menuInicio.crearEtiquetaError();

        menuInicio.agregarComponenteEnFila(panelFormulario, 0, etiquetaAdvertencia);
        menuInicio.agregarFilaFormulario  (panelFormulario, 1, "Password:", campoPassword);
        menuInicio.agregarComponenteEnFila(panelFormulario, 2, etiquetaError);

        JButton botonEliminarDefinitivo = new JButton("Eliminar definitivamente");
        aplicarEstiloBotonPeligro(botonEliminarDefinitivo);
        menuInicio.agregarComponenteEnFila(panelFormulario, 3, botonEliminarDefinitivo);

        botonEliminarDefinitivo.addActionListener(evento -> {
            String passwordIngresado = new String(campoPassword.getPassword());
            if (!passwordIngresado.equals(player.getPassword())) {
                etiquetaError.setText("Password incorrecto.");
                return;
            }
            almacenamiento.eliminarPlayer(player.getUsername());
            JOptionPane.showMessageDialog(dialogo,
                "Cuenta eliminada correctamente.", "Cuenta eliminada", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
            dispose();
            menuPrincipal.logout();
        });

        dialogo.setContentPane(panelFormulario);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    private void agregarInfoAlPanel(JPanel panel, String clave, String valor) {
        JLabel etiquetaClave = new JLabel(clave);
        etiquetaClave.setForeground(COLOR_DORADO_TENUE);
        etiquetaClave.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel etiquetaValor = new JLabel(valor);
        etiquetaValor.setForeground(COLOR_DORADO);
        etiquetaValor.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panel.add(etiquetaClave);
        panel.add(etiquetaValor);
    }
    private void aplicarEstiloBotonPeligro(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setBackground(new Color(0x7A1414));
        boton.setForeground(new Color(0xFFCCCC));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(300, 44));
    }
}