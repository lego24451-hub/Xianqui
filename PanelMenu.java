package xianqui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private static final Color COLOR_FONDO = new Color(0x0A0400);
    private static final Color COLOR_DORADO = new Color(0xD4A030);
    private static final Color COLOR_DORADO_TENUE = new Color(0x5A3A12);

    private final Player   jugadorLogueado;
    private final MenuInicio menuInicio;
    private final IStorage   almacenamiento = StorageManager.getInstance();

    
    private JLabel etiquetaInfoJugador;

    public MenuPrincipal(Player player, MenuInicio menuInicio) {
        super("Xiangqi - Menú Principal");
        this.jugadorLogueado = player;
        this.menuInicio      = menuInicio;
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
        panelRaiz.setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 70));

        JLabel etiquetaTitulo = crearEtiquetaCentrada(
            "象　棋",
            new Font("Serif", Font.BOLD, 44),
            COLOR_DORADO);

        etiquetaInfoJugador = crearEtiquetaCentrada(
            construirTextoInfo(),
            new Font("SansSerif", Font.PLAIN, 12),
            COLOR_DORADO_TENUE);

        JLabel etiquetaMenu = crearEtiquetaCentrada(
            "─── MENÚ PRINCIPAL ───",
            new Font("SansSerif", Font.BOLD, 14),
            COLOR_DORADO);

        JButton botonJugar = menuInicio.crearBoton("  1.  JUGAR XIANGQI  ");
        JButton botonCuenta = menuInicio.crearBoton("  2.  MI CUENTA  ");
        JButton botonReportes = menuInicio.crearBoton("  3.  REPORTES  ");
        JButton botonLogout= menuInicio.crearBoton("  4.  LOG OUT  ");

        botonJugar.addActionListener   (evento -> abrirJuego());
        botonCuenta.addActionListener  (evento -> new MiCuenta(jugadorLogueado, this, menuInicio).setVisible(true));
        botonReportes.addActionListener(evento -> new Reportes(jugadorLogueado, this).setVisible(true));
        botonLogout.addActionListener  (evento -> logout());

        panelRaiz.add(etiquetaTitulo);
        panelRaiz.add(Box.createVerticalStrut(4));
        panelRaiz.add(etiquetaInfoJugador);
        panelRaiz.add(Box.createVerticalStrut(24));
        panelRaiz.add(etiquetaMenu);
        panelRaiz.add(Box.createVerticalStrut(16));
        panelRaiz.add(botonJugar);
        panelRaiz.add(Box.createVerticalStrut(8));
        panelRaiz.add(botonCuenta);
        panelRaiz.add(Box.createVerticalStrut(8));
        panelRaiz.add(botonReportes);
        panelRaiz.add(Box.createVerticalStrut(8));
        panelRaiz.add(botonLogout);

        setContentPane(panelRaiz);
    }

    
    private String construirTextoInfo() {
        return "Bienvenido, " + jugadorLogueado.getUsername()
             + "   |   Puntos: " + jugadorLogueado.getPuntos();
    }

    
    public void actualizarPuntos() {
        etiquetaInfoJugador.setText(construirTextoInfo());
    }

 
    private void abrirJuego() {
        ArrayList<Player> todosLosJugadores = almacenamiento.obtenerTodosLosPlayers();
        todosLosJugadores.removeIf(jugador ->
            jugador.getUsername().equalsIgnoreCase(jugadorLogueado.getUsername())
            || !jugador.isActivo());

        if (todosLosJugadores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay otros jugadores registrados.\nCrea al menos un jugador más para poder jugar.",
                "Sin oponentes disponibles",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombresOponentes = todosLosJugadores.stream()
            .map(Player::getUsername)
            .toArray(String[]::new);

        String nombreElegido = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona tu oponente:",
            "Nueva Partida",
            JOptionPane.PLAIN_MESSAGE,
            null,
            nombresOponentes,
            nombresOponentes[0]);

        if (nombreElegido == null) return;

        Player oponente = almacenamiento.buscarPlayer(nombreElegido);
        abrirVentanaJuego(oponente);
    }

    private void abrirVentanaJuego(Player oponente) {
        GameState estadoJuego = new GameState();
        PanelXianqui panelJuego  = new PanelXianqui(estadoJuego);

        JFrame ventanaJuego = new JFrame("Xiangqi  —  "
            + jugadorLogueado.getUsername() + " (Rojo)  vs  "
            + oponente.getUsername() + " (Negro)");
        ventanaJuego.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaJuego.setResizable(false);

      
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(COLOR_FONDO);
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        JLabel etiquetaTituloJuego = crearEtiquetaCentrada(
            "象　棋", new Font("Serif", Font.BOLD, 32), COLOR_DORADO);
        JLabel etiquetaVersus = crearEtiquetaCentrada(
            jugadorLogueado.getUsername() + " (Rojo)  vs  " + oponente.getUsername() + " (Negro)",
            new Font("SansSerif", Font.PLAIN, 11),
            COLOR_DORADO_TENUE);
        panelTitulo.add(Box.createVerticalStrut(8));
        panelTitulo.add(etiquetaTituloJuego);
        panelTitulo.add(etiquetaVersus);
        panelTitulo.add(Box.createVerticalStrut(8));

        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        panelBotones.setBackground(COLOR_FONDO);

        JButton botonDeshacer  = menuInicio.crearBoton("Deshacer [U]");
        JButton botonNueva     = menuInicio.crearBoton("Nueva Partida [N]");
        JButton botonRetirar   = crearBotonPeligro("Retirarme");

        botonDeshacer.addActionListener(evento -> {
            panelJuego.undo();
            panelJuego.requestFocusInWindow();
        });
        botonNueva.addActionListener(evento -> {
            panelJuego.newGame();
            panelJuego.requestFocusInWindow();
        });

        botonRetirar.addActionListener(evento -> {
            int confirmacion = JOptionPane.showConfirmDialog(
                ventanaJuego,
                "¿Confirmas retirarte? Tu oponente ganará la partida.",
                "Confirmar retiro",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (confirmacion != JOptionPane.YES_OPTION) return;
            registrarResultado(ventanaJuego,
                oponente.getUsername(), jugadorLogueado.getUsername(), true);
        });

        panelBotones.add(botonDeshacer);
        panelBotones.add(botonNueva);
        panelBotones.add(botonRetirar);

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(COLOR_FONDO);
        panelContenedor.add(panelTitulo, BorderLayout.NORTH);
        panelContenedor.add(panelJuego, BorderLayout.CENTER);
        panelContenedor.add(panelBotones, BorderLayout.SOUTH);

        ventanaJuego.setContentPane(panelContenedor);

       
        Timer temporizadorFinJuego = new Timer(400, null);
        temporizadorFinJuego.addActionListener(eventoTimer -> {
            if (estadoJuego.isGameOver()) {
                temporizadorFinJuego.stop();
                Piece.Color colorGanador = estadoJuego.getWinner();
                String nombreGanador  = (colorGanador == Piece.Color.ROJO)
                    ? jugadorLogueado.getUsername() : oponente.getUsername();
                String nombrePerdedor = (colorGanador == Piece.Color.ROJO)
                    ? oponente.getUsername() : jugadorLogueado.getUsername();
                registrarResultado(ventanaJuego, nombreGanador, nombrePerdedor, false);
            }
        });
        temporizadorFinJuego.start();

        ventanaJuego.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evento) {
                temporizadorFinJuego.stop();
            }
        });

        
        panelJuego.addKeyListener(new java.awt.event.KeyAdapter(){
            @Override
            public void keyPressed(java.awt.event.KeyEvent evento){
                switch (evento.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_U: panelJuego.undo();    
                    break;
                    case java.awt.event.KeyEvent.VK_N: panelJuego.newGame(); 
                    break;
                }
            }
        });

        ventanaJuego.pack();
        ventanaJuego.setLocationRelativeTo(this);
        ventanaJuego.setVisible(true);
        panelJuego.requestFocusInWindow();
    }

    private void registrarResultado(JFrame ventanaJuego, String nombreGanador,
       String nombrePerdedor, boolean fueRetiro) {
        String mensaje;
        if (fueRetiro) {
            mensaje = "JUGADOR " + nombrePerdedor.toUpperCase()
                + " SE HA RETIRADO, FELICIDADES JUGADOR "
                + nombreGanador.toUpperCase() + ", HAS GANADO 3 PUNTOS";
        } else {
            mensaje = "JUGADOR " + nombreGanador.toUpperCase()
                + " VENCIÓ A JUGADOR " + nombrePerdedor.toUpperCase()
                + ", FELICIDADES HAS GANADO 3 PUNTOS";
        }

        JOptionPane.showMessageDialog(ventanaJuego, mensaje, "Partida Terminada",
            JOptionPane.INFORMATION_MESSAGE);

       
        Player playerGanador = almacenamiento.buscarPlayer(nombreGanador);
        if (playerGanador != null) {
            playerGanador.agregarPuntos(3);
            
            if (playerGanador.getUsername().equalsIgnoreCase(jugadorLogueado.getUsername())) {
                actualizarPuntos();
            }
        }

        
        String fechaHora = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String entradaLog = "[" + fechaHora + "] " + mensaje;
        almacenamiento.guardarLog(nombreGanador,  entradaLog);
        almacenamiento.guardarLog(nombrePerdedor, entradaLog);

        ventanaJuego.dispose();
    }

    void logout() {
        setVisible(false);
        dispose();
        menuInicio.setVisible(true);
    }
    static JLabel crearEtiquetaCentrada(String texto, Font fuente, Color color) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiqueta;
    }

    private JButton crearBotonPeligro(String texto) {
        JButton boton = menuInicio.crearBoton(texto);
        boton.setBackground(new Color(0x7A1414));
        boton.setForeground(new Color(0xFFCCCC));
        return boton;
    }
}