package xianqui;

import javax.swing.*;
import java.awt.*;


public class AppFrame extends JFrame { //todo mi window
    

    public static final String CARD_INICIO = "inicio";
    public static final String CARD_MENU  = "menu";
    public static final String CARD_JUEGO = "juego";
    public static final String CARD_CUENTA = "cuenta";
    public static final String CARD_REPORTES  = "reportes";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);

   
    private final PanelInicio panelInicio;
    private PanelMenu panelMenu;       
    private PanelJuegoWrapper panelJuego;   
    private PanelCuenta panelCuenta;     
    private PanelReportes  panelReportes;  

    private static AppFrame instancia;

    public static AppFrame getInstance() {
        if (instancia == null) instancia = new AppFrame();
        return instancia;
    }

     AppFrame() {
        super("Xiangqi - Ajedrez Chino");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        panelInicio = new PanelInicio(this);
        cardPanel.add(panelInicio, CARD_INICIO);

        
        cardPanel.add(new JPanel(), CARD_MENU);
        cardPanel.add(new JPanel(), CARD_JUEGO);
        cardPanel.add(new JPanel(), CARD_CUENTA);
        cardPanel.add(new JPanel(), CARD_REPORTES);

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, CARD_INICIO);

        pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    

    public void mostrarInicio() {
        cardLayout.show(cardPanel, CARD_INICIO);
        repack();
    }

    public void mostrarMenu(Player player) {
        panelMenu = new PanelMenu(player, this);
        cardPanel.add(panelMenu, CARD_MENU);
        cardLayout.show(cardPanel, CARD_MENU);
        repack();
    }

    public void mostrarJuego(Player jugador, Player oponente) {
        panelJuego = new PanelJuegoWrapper(jugador, oponente, this);
        cardPanel.add(panelJuego, CARD_JUEGO);
        cardLayout.show(cardPanel, CARD_JUEGO);
        repack();
        panelJuego.requestFocusInWindow();
    }

    public void mostrarCuenta(Player player) {
        panelCuenta = new PanelCuenta(player, this);
        cardPanel.add(panelCuenta, CARD_CUENTA);
        cardLayout.show(cardPanel, CARD_CUENTA);
        repack();
    }

    public void mostrarReportes(Player player) {
        panelReportes = new PanelReportes(player,this);
        cardPanel.add(panelReportes, CARD_REPORTES);
        cardLayout.show(cardPanel, CARD_REPORTES);
        repack();
    }

  
    public void actualizarPuntosMenu() {
        if (panelMenu != null) panelMenu.actualizarPuntos();
    }

    
    private void repack() {
        pack();
        setLocationRelativeTo(null);
    }
}