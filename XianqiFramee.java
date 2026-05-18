package xianqui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class XianqiFramee extends JFrame {

    public XianqiFramee() {
        super("\u8c61\u68cb \u00b7 Xiangqi - Ajedrez Chino"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GameState estadoJuego= new GameState();
        PanelXianqui panelJuego  = new PanelXianqui(estadoJuego);

        JPanel panelTitulo= construirPanelTitulo();
        JPanel panelBotones = construirPanelBotones(panelJuego);
        JPanel panelLeyenda = construirPanelLeyenda();

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setBackground(new Color(0x0A0400));
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelJuego, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        JPanel panelExterno = new JPanel(new BorderLayout());
        panelExterno.setBackground(new Color(0x0A0400));
        panelExterno.add(panelPrincipal, BorderLayout.CENTER);
        panelExterno.add(panelLeyenda, BorderLayout.SOUTH);

        setContentPane(panelExterno);

        panelJuego.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evento) {
                switch (evento.getKeyCode()) {
                    case KeyEvent.VK_U: panelJuego.undo();   
                    break;
                    case KeyEvent.VK_N: panelJuego.newGame(); 
                    break;
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        panelJuego.requestFocusInWindow();
    }

    private JPanel construirPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0A0400));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Font fuenteTitulo   = cargarFuenteCJK(Font.BOLD, 34);
        Font fuenteSubtitulo = new Font("SansSerif", Font.PLAIN, 11);

        JLabel etiquetaTitulo = new JLabel("\u8c61\u3000\u68cb", SwingConstants.CENTER); // 象　棋
        etiquetaTitulo.setFont(fuenteTitulo);
        etiquetaTitulo.setForeground(new Color(0xD4A030));
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaSubtitulo = new JLabel("XIANGQI  \u00b7  AJEDREZ CHINO", SwingConstants.CENTER);
        etiquetaSubtitulo.setFont(fuenteSubtitulo);
        etiquetaSubtitulo.setForeground(new Color(0x5A3A12));
        etiquetaSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(etiquetaTitulo);
        panel.add(etiquetaSubtitulo);
        panel.add(Box.createVerticalStrut(8));
        return panel;
    }

    private JPanel construirPanelBotones(PanelXianqui panelJuego) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        panel.setBackground(new Color(0x0A0400));

        JButton botonDeshacer  = crearBoton("Deshacer  [U]");
        JButton botonNuevaPartida = crearBoton("Nueva Partida  [N]");

        botonDeshacer.addActionListener(evento -> {
            panelJuego.undo();
            panelJuego.requestFocusInWindow();
        });
        botonNuevaPartida.addActionListener(evento -> {
            panelJuego.newGame();
            panelJuego.requestFocusInWindow();
        });

        panel.add(botonDeshacer);
        panel.add(botonNuevaPartida);
        return panel;
    }

    private JPanel construirPanelLeyenda() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x0D0500));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 14, 10, 14));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Font fuenteLeyenda = cargarFuenteCJK(Font.PLAIN, 12);

        String[][] entradasLeyenda = {
            {"General", "Consejero", " Elefante", "Caballo", "Torre",  " Canon",  "Soldado"},
            {"General", "Consejero", " Elefante", "Caballo", "Torre",  " Canon",  "Soldado"}
        };
        Color[] coloresFila = { new Color(0xC06030), new Color(0x888888) };
        String[] etiquetas  = { "Rojas: ", "Negras: " };

        for (int indiceFila = 0; indiceFila < 2; indiceFila++) {
            JPanel filaPiezas = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            filaPiezas.setBackground(new Color(0x0D0500));

            JLabel etiquetaColor = new JLabel(etiquetas[indiceFila]);
            etiquetaColor.setFont(fuenteLeyenda.deriveFont(Font.BOLD));
            etiquetaColor.setForeground(coloresFila[indiceFila]);
            filaPiezas.add(etiquetaColor);

            for (String nombrePieza : entradasLeyenda[indiceFila]) {
                JLabel etiquetaPieza = new JLabel(nombrePieza + "  ");
                etiquetaPieza.setFont(fuenteLeyenda);
                etiquetaPieza.setForeground(coloresFila[indiceFila]);
                filaPiezas.add(etiquetaPieza);
            }
            panel.add(filaPiezas);
        }
        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(cargarFuenteCJK(Font.BOLD, 13));
        boton.setBackground(new Color(0xB86E10));
        boton.setForeground(new Color(0xFFE090));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        return boton;
    }

    private static Font cargarFuenteCJK(int estilo, int tamano) {
        for (String nombreFuente : new String[]{
                "SimSun", "NSimSun", "STSong", "PingFang SC",
                "Hiragino Sans GB", "WenQuanYi Micro Hei",
                "Noto Serif CJK SC", "Dialog"}) {
            Font fuente = new Font(nombreFuente, estilo, tamano);
            if (fuente.canDisplay('\u5e25')) return fuente; 
        }
        return new Font("Serif", estilo, tamano);
    }
}