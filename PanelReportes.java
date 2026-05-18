package xianqui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelReportes extends JPanel {

    private static final Color COLOR_FONDO        = PanelInicio.COLOR_FONDO;
    private static final Color COLOR_DORADO       = PanelInicio.COLOR_DORADO;
    private static final Color COLOR_DORADO_TENUE = PanelInicio.COLOR_DORADO_TENUE;

    private final Player   jugadorLogueado;
    private final AppFrame appFrame;
    private final IStorage almacenamiento = StorageManager.getInstance();

    public PanelReportes(Player jugador, AppFrame appFrame) {
        this.jugadorLogueado = jugador;
        this.appFrame        = appFrame;
        setBackground(COLOR_FONDO);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(28, 50, 28, 50));
        construirInterfaz();
    }

    private void construirInterfaz() {
        add(PanelInicio.crearEtiqueta(
            "REPORTES", new Font("SansSerif", Font.BOLD, 18), COLOR_DORADO));
        add(Box.createVerticalStrut(20));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setBackground(COLOR_FONDO);
        pestanas.setForeground(COLOR_DORADO);
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));

        pestanas.addTab("Ranking Jugadores",    construirPanelRanking());
        pestanas.addTab("Mis Últimas Partidas", construirPanelLogs());

        add(pestanas);
        add(Box.createVerticalStrut(16));

        JButton botonVolver = PanelInicio.crearBoton("← Volver");
        botonVolver.addActionListener(e -> appFrame.mostrarMenu(jugadorLogueado));
        add(botonVolver);
    }

    private JPanel construirPanelRanking() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        ArrayList<Player> jugadoresActivos = almacenamiento.obtenerTodosLosPlayers();
        jugadoresActivos.removeIf(jugador -> !jugador.isActivo());

        ordenarPorPuntosRecursivo(jugadoresActivos, jugadoresActivos.size());

        String[]   columnas = {"#", "Username", "Puntos"};
        Object[][] datos    = new Object[jugadoresActivos.size()][3];
        for (int indice = 0; indice < jugadoresActivos.size(); indice++) {
            datos[indice][0] = indice + 1;
            datos[indice][1] = jugadoresActivos.get(indice).getUsername();
            datos[indice][2] = jugadoresActivos.get(indice).getPuntos();
        }

        JTable tablaRanking = new JTable(datos, columnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        aplicarEstiloTabla(tablaRanking);

        JScrollPane scrollTabla = new JScrollPane(tablaRanking);
        scrollTabla.setBackground(COLOR_FONDO);
        scrollTabla.getViewport().setBackground(new Color(0x120800));
        scrollTabla.setPreferredSize(new Dimension(340, 220));

        panel.add(scrollTabla, BorderLayout.CENTER);
        return panel;
    }

    private void ordenarPorPuntosRecursivo(ArrayList<Player> lista, int tamano) {
        if (tamano <= 1) return;
        for (int indice = 0; indice < tamano - 1; indice++) {
            if (lista.get(indice).getPuntos() < lista.get(indice + 1).getPuntos()) {
                Player temporal = lista.get(indice);
                lista.set(indice, lista.get(indice + 1));
                lista.set(indice + 1, temporal);
            }
        }
        ordenarPorPuntosRecursivo(lista, tamano - 1);
    }

    private JPanel construirPanelLogs() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        ArrayList<String> logs = almacenamiento.obtenerLogs(jugadorLogueado.getUsername());

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setBackground(new Color(0x120800));
        areaTexto.setForeground(COLOR_DORADO);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);
        areaTexto.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        if (logs.isEmpty()) {
            areaTexto.setText("Aún no tienes partidas registradas.");
        } else {
            StringBuilder constructorTexto = new StringBuilder();
            for (String entradaLog : logs) constructorTexto.append(entradaLog).append("\n");
            areaTexto.setText(constructorTexto.toString());
            areaTexto.setCaretPosition(0);
        }

        JScrollPane scrollLogs = new JScrollPane(areaTexto);
        scrollLogs.setPreferredSize(new Dimension(380, 220));

        panel.add(scrollLogs, BorderLayout.CENTER);
        return panel;
    }

    private void aplicarEstiloTabla(JTable tabla) {
        tabla.setBackground(new Color(0x120800));
        tabla.setForeground(COLOR_DORADO);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(24);
        tabla.setGridColor(COLOR_DORADO_TENUE);
        tabla.getTableHeader().setBackground(new Color(0x2A1400));
        tabla.getTableHeader().setForeground(COLOR_DORADO);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.setSelectionBackground(new Color(0x3A2000));
        tabla.setSelectionForeground(COLOR_DORADO);
    }
}