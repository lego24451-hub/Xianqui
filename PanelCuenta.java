package xianqui;

import javax.swing.*;
import java.awt.*;

public class PanelCuenta extends JPanel {

    private static final Color COLOR_FONDO = PanelInicio.COLOR_FONDO;
    private static final Color COLOR_DORADO = PanelInicio.COLOR_DORADO;
    private static final Color COLOR_DORADO_TENUE = PanelInicio.COLOR_DORADO_TENUE;

    private final Player   player;
    private final AppFrame appFrame;
    private final IStorage almacenamiento = StorageManager.getInstance();

    public PanelCuenta(Player player, AppFrame appFrame) {
        this.player   = player;
        this.appFrame = appFrame;
        setBackground(COLOR_FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(28, 50, 28, 50));
        construirInterfaz();
    }

    private void construirInterfaz() {
        add(PanelInicio.crearEtiqueta(
            "MI CUENTA", new Font("SansSerif", Font.BOLD, 18), COLOR_DORADO));
        add(Box.createVerticalStrut(20));
        JPanel panelInfo = new JPanel(new GridLayout(0, 2, 12, 8));
        panelInfo.setBackground(COLOR_FONDO);
        panelInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0xB86E10), 1),
            "Información del jugador",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12),
            COLOR_DORADO));

        agregarInfo(panelInfo, "Username:", player.getUsername());
        agregarInfo(panelInfo, "Puntos:",String.valueOf(player.getPuntos()));
        agregarInfo(panelInfo, "Fecha de ingreso:", player.getFechaIngreso());
        agregarInfo(panelInfo, "Estado:", player.isActivo() ? "Activo" : "Inactivo");

        add(panelInfo);
        add(Box.createVerticalStrut(22));

        JButton botonVolver   = PanelInicio.crearBoton("← Volver");
        JButton botonCambiar  = PanelInicio.crearBoton("Cambiar Password");
        JButton botonEliminar = PanelInicio.crearBotonPeligro("Eliminar mi Cuenta");

        botonVolver.addActionListener  (e -> appFrame.mostrarMenu(player));
        botonCambiar.addActionListener (e -> mostrarCambiarPassword());
        botonEliminar.addActionListener(e -> mostrarEliminarCuenta());

        add(botonVolver);
        add(Box.createVerticalStrut(8));
        add(botonCambiar);
        add(Box.createVerticalStrut(10));
        add(botonEliminar);
    }

    private void mostrarCambiarPassword() {
        JDialog dialogo = new JDialog(appFrame, "Cambiar Password", true);
        JPanel panel    = PanelInicio.crearPanelFormulario();

        JPasswordField campoActual = PanelInicio.crearCampoContrasena();
        JPasswordField campoNuevo  = PanelInicio.crearCampoContrasena();
        JLabel error               = PanelInicio.crearEtiquetaError();

        PanelInicio.agregarFilaContrasena(panel, 0, "Password actual:", campoActual);
        PanelInicio.agregarFilaContrasena(panel, 1, "Nuevo password:",  campoNuevo);

        
        JLabel hint = new JLabel("Mín. 5 chars · mayúscula · minúscula · número");
        hint.setForeground(PanelInicio.COLOR_DORADO_TENUE);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 10));
        PanelInicio.agregarComponenteEnFila(panel, 2, hint);

        PanelInicio.agregarComponenteEnFila(panel, 3, error);

        JButton boton = PanelInicio.crearBoton("Confirmar");
        PanelInicio.agregarComponenteEnFila(panel, 4, boton);

        boton.addActionListener(e -> {
            String actual = new String(campoActual.getPassword());
            String nuevo  = new String(campoNuevo.getPassword());

            if (!actual.equals(player.getPassword())) {
                error.setText("Password actual incorrecto.");
                return;
            }
            String errorPassword = PanelInicio.validarPassword(nuevo);
            if (errorPassword != null) {
                error.setText(errorPassword);
                return;
            }
            player.setPassword(nuevo);
            almacenamiento.guardarPlayer(player);
            JOptionPane.showMessageDialog(dialogo, "Password cambiado exitosamente.", "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        mostrarDialogo(dialogo, panel);
    }

    private void mostrarEliminarCuenta() {
        JDialog dialogo = new JDialog(appFrame, "Eliminar Cuenta", true);
        JPanel panel    = PanelInicio.crearPanelFormulario();

        JLabel advertencia = new JLabel(
            "<html>Esta acción es irreversible.<br>Ingresa tu password para confirmar.</html>",
            SwingConstants.CENTER);
        advertencia.setForeground(new Color(0xFF5533));
        advertencia.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPasswordField campoClave = PanelInicio.crearCampoContrasena();
        JLabel error = PanelInicio.crearEtiquetaError();

        PanelInicio.agregarComponenteEnFila(panel, 0, advertencia);
        PanelInicio.agregarFilaContrasena  (panel, 1, "Password:", campoClave);
        PanelInicio.agregarComponenteEnFila(panel, 2, error);

        JButton boton = PanelInicio.crearBotonPeligro("Eliminar definitivamente");
        PanelInicio.agregarComponenteEnFila(panel, 3, boton);

        boton.addActionListener(e -> {
            String ingresado = new String(campoClave.getPassword());
            if (!ingresado.equals(player.getPassword())) {
                error.setText("Password incorrecto.");
                return;
            }
            almacenamiento.eliminarPlayer(player.getUsername());
            JOptionPane.showMessageDialog(dialogo, "Cuenta eliminada correctamente.",
                "Cuenta eliminada", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
            appFrame.mostrarInicio();
        });

        mostrarDialogo(dialogo, panel);
    }

    private void mostrarDialogo(JDialog dialogo, JPanel contenido) {
        dialogo.setContentPane(contenido);
        dialogo.setResizable(false);
        dialogo.pack();
        dialogo.setLocationRelativeTo(appFrame);
        dialogo.setVisible(true);
    }

    private void agregarInfo(JPanel panel, String clave, String valor) {
        JLabel k = new JLabel(clave);
        k.setForeground(COLOR_DORADO_TENUE);
        k.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel v = new JLabel(valor);
        v.setForeground(COLOR_DORADO);
        v.setFont(new Font("SansSerif", Font.PLAIN, 12));

        panel.add(k);
        panel.add(v);
    }
}