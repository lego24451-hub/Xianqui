package xianqui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class PanelMenu extends JPanel {

    private static final Color COLOR_FONDO = PanelInicio.COLOR_FONDO;
    private static final Color COLOR_DORADO = PanelInicio.COLOR_DORADO;
    private static final Color COLOR_DORADO_TENUE = PanelInicio.COLOR_DORADO_TENUE;

    private final Player   jugador;
    private final AppFrame appFrame;
    private final IStorage almacenamiento = StorageManager.getInstance();

    private JLabel etiquetaInfo;

    public PanelMenu(Player jugador, AppFrame appFrame) {
        this.jugador  = jugador;
        this.appFrame = appFrame;
        setBackground(COLOR_FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 70));
        construirInterfaz();
    }

    private void construirInterfaz() {
        JLabel titulo = PanelInicio.crearEtiqueta(
            "象　棋", new Font("Serif", Font.BOLD, 44), COLOR_DORADO);

        etiquetaInfo = PanelInicio.crearEtiqueta(
            textoInfo(), new Font("SansSerif", Font.PLAIN, 12), COLOR_DORADO_TENUE);

        JLabel menu = PanelInicio.crearEtiqueta(
            "─── MENÚ PRINCIPAL ───", new Font("SansSerif", Font.BOLD, 14), COLOR_DORADO);

        JButton botonJugar    = PanelInicio.crearBoton("  1.  JUGAR XIANGQI  ");
        JButton botonCuenta   = PanelInicio.crearBoton("  2.  MI CUENTA  ");
        JButton botonReportes = PanelInicio.crearBoton("  3.  REPORTES  ");
        JButton botonLogout   = PanelInicio.crearBoton("  4.  LOG OUT  ");

        botonJugar.addActionListener   (e -> seleccionarOponente());
        botonCuenta.addActionListener  (e -> appFrame.mostrarCuenta(jugador));
        botonReportes.addActionListener(e -> appFrame.mostrarReportes(jugador));
        botonLogout.addActionListener  (e -> appFrame.mostrarInicio());

        add(titulo);
        add(Box.createVerticalStrut(4));
        add(etiquetaInfo);
        add(Box.createVerticalStrut(24));
        add(menu);
        add(Box.createVerticalStrut(16));
        add(botonJugar);
        add(Box.createVerticalStrut(8));
        add(botonCuenta);
        add(Box.createVerticalStrut(8));
        add(botonReportes);
        add(Box.createVerticalStrut(8));
        add(botonLogout);
    }

    private String textoInfo() {
        return "Bienvenido, " + jugador.getUsername()
             + "   |   Puntos: " + jugador.getPuntos();
    }

    public void actualizarPuntos() {
        etiquetaInfo.setText(textoInfo());
    }

    private void seleccionarOponente() {
        ArrayList<Player> lista = almacenamiento.obtenerTodosLosPlayers();
        lista.removeIf(p -> p.getUsername().equalsIgnoreCase(jugador.getUsername()) || !p.isActivo());

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(appFrame,
                "No hay otros jugadores registrados.\nCrea al menos un jugador más para poder jugar.",
                "Sin oponentes disponibles", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombres = lista.stream().map(Player::getUsername).toArray(String[]::new);
        String elegido = (String) JOptionPane.showInputDialog(
            appFrame, "Selecciona tu oponente:", "Nueva Partida",
            JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

        if (elegido == null) return;
        Player oponente = almacenamiento.buscarPlayer(elegido);
        appFrame.mostrarJuego(jugador, oponente);
    }

    

    public void registrarResultado(Player nombreGanador, Player nombrePerdedor, boolean fueRetiro) {
//        String mensaje = fueRetiro
//            ? "JUGADOR " + nombrePerdedor.toUpperCase() + " SE HA RETIRADO, FELICIDADES JUGADOR "
//              + nombreGanador.toUpperCase() + ", HAS GANADO 3 PUNTOS"
//            : "JUGADOR " + nombreGanador.toUpperCase() + " VENCIÓ A JUGADOR "
//              + nombrePerdedor.toUpperCase() + ", FELICIDADES HAS GANADO 3 PUNTOS";
        String message="";
        String messageGan="";
        if(fueRetiro){
            ReporteRetiro repRet= new ReporteRetiro(nombreGanador,nombrePerdedor);
            message=repRet.getMensaje();
            messageGan=repRet.getGanador().getMensaje();
        }else{
            ReporteGanador repGan= new ReporteGanador(nombreGanador,nombrePerdedor);
            messageGan=repGan.getMensaje();
            ReportePerdedor repPer= new ReportePerdedor(nombreGanador,nombrePerdedor);
            message= repPer.getMensaje();
        }

        JOptionPane.showMessageDialog(appFrame, message, "Partida Terminada",
            JOptionPane.INFORMATION_MESSAGE);
        nombreGanador.agregarPuntos(3);
        if (nombreGanador.getUsername().equalsIgnoreCase(jugador.getUsername()))
                actualizarPuntos();
        

        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String entradaGanador   = "[" + fechaHora + "] " + messageGan;
        String entradaPerdedor="[" + fechaHora + "] " + message;
        almacenamiento.guardarLog(nombreGanador.getUsername(),  entradaGanador);
        almacenamiento.guardarLog(nombrePerdedor.getUsername(), entradaPerdedor);
        
        
        appFrame.mostrarMenu(jugador);
    }
}