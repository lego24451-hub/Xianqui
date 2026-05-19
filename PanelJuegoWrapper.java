package xianqui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PanelJuegoWrapper extends JPanel {

    private static final Color COLOR_FONDO = PanelInicio.COLOR_FONDO;
    private static final Color COLOR_DORADO  = PanelInicio.COLOR_DORADO;
    private static final Color COLOR_DORADO_TENUE = PanelInicio.COLOR_DORADO_TENUE;

    private final Player jugador;
    private final Player    oponente;
    private final AppFrame  appFrame;
    private final GameState estadoJuego;
    private final PanelXianqui panelJuego;
    private final Timer     temporizador;

    public PanelJuegoWrapper(Player jugador, Player oponente, AppFrame appFrame) {
        this.jugador = jugador;
        this.oponente = oponente;
        this.appFrame = appFrame;
        this.estadoJuego = new GameState();
        this.panelJuego  = new PanelXianqui(estadoJuego);

        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));
        construirInterfaz();

        
        temporizador = new Timer(400, e -> verificarFinJuego());
        temporizador.start();

        
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_U: panelJuego.undo();  
                    break;
                    case KeyEvent.VK_N: panelJuego.newGame();
                    break;
                }
            }
        });
    }

    private void construirInterfaz() {
       
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));

        JLabel lblTitulo = PanelInicio.crearEtiqueta(
            "象　棋", new Font("Serif", Font.BOLD, 32), COLOR_DORADO);
        JLabel lblVersus = PanelInicio.crearEtiqueta(
            jugador.getUsername() + " (Rojo)  vs  " + oponente.getUsername() + " (Negro)",
            new Font("SansSerif", Font.PLAIN, 11), COLOR_DORADO_TENUE);

        panelTitulo.add(Box.createVerticalStrut(8));
        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblVersus);
        panelTitulo.add(Box.createVerticalStrut(8));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        panelBotones.setBackground(COLOR_FONDO);

        JButton botonVolver   = PanelInicio.crearBoton("← Menú");
        JButton botonDeshacer = PanelInicio.crearBoton("Deshacer [U]");
        JButton botonNueva    = PanelInicio.crearBoton("Nueva Partida [N]");
        JButton botonRetirar  = PanelInicio.crearBotonPeligro("Retirarme");

        botonVolver.addActionListener(e -> {
            temporizador.stop();
            appFrame.mostrarMenu(jugador);
        });
        botonDeshacer.addActionListener(e -> { panelJuego.undo();    requestFocusInWindow(); });
        botonNueva.addActionListener   (e -> { panelJuego.newGame(); requestFocusInWindow(); });
        botonRetirar.addActionListener (e -> confirmarRetiro());

        panelBotones.add(botonVolver);
        panelBotones.add(botonDeshacer);
        panelBotones.add(botonNueva);
        panelBotones.add(botonRetirar);

        add(panelTitulo,  BorderLayout.NORTH);
        add(panelJuego,   BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }


    private void verificarFinJuego() {
        if (!estadoJuego.isGameOver()) return;
        temporizador.stop();
        Piece.Color colorGanador  = estadoJuego.getWinner();
        Player nombreGanador  = (colorGanador == Piece.Color.ROJO)
            ? jugador: oponente;
        Player nombrePerdedor = (colorGanador == Piece.Color.ROJO)
            ? oponente : jugador;
        notificarResultado(nombreGanador, nombrePerdedor, false);
    }

    private void confirmarRetiro() {
        int resp = JOptionPane.showConfirmDialog(
            appFrame,
            "¿Confirmas retirarte? Tu oponente ganará la partida.",
            "Confirmar retiro",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (resp != JOptionPane.YES_OPTION) return;
        temporizador.stop();
        Player ganador = estadoJuego.getTurn().equals(Piece.Color.ROJO)?oponente:jugador; 
        Player perdedor = estadoJuego.getTurn().equals(Piece.Color.NEGRO)?oponente:jugador; 
                
        notificarResultado (ganador, perdedor, true);
    }

    private void notificarResultado(Player ganador, Player perdedor, boolean retiro) {
        
        PanelMenu menu = obtenerPanelMenu();
        if (menu != null) menu.registrarResultado(ganador, perdedor, retiro);
    }

    
    private PanelMenu obtenerPanelMenu() {
        try {
            java.lang.reflect.Field f = AppFrame.class.getDeclaredField("panelMenu");
            f.setAccessible(true);
            return (PanelMenu) f.get(appFrame);
        } catch (Exception ex) {
            // Fallback: volver al menú sin actualizar
            appFrame.mostrarMenu(jugador);
            return null;
        }
    }

    
    @Override
    public boolean requestFocusInWindow() {
        return panelJuego.requestFocusInWindow();
        
    }
}