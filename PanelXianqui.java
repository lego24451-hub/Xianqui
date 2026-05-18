package xianqui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PanelXianqui extends JPanel implements MouseListener {

    private static final int TAMANO_CELDA = 48;  
    private static final int MARGEN = 36;  
    private static final int ALTURA_ESTADO = 72; 

    private static final int ANCHO_TABLERO = MARGEN * 2 + 8 * TAMANO_CELDA;
    private static final int ALTO_TABLERO  = MARGEN * 2 + 9 * TAMANO_CELDA;

    private static final Color COLOR_FONDO = new Color(0x0A0400);
    private static final Color COLOR_FONDO_TABLERO =new Color(0xDDB84A);
    private static final Color COLOR_MARCO_TABLERO = new Color(0xC07820);
    private static final Color COLOR_LINEA = new Color(0x7A4010);
    private static final Color COLOR_GRADIENTE_ROJO_ALTO = new Color(0xFF, 0xFD, 0xE0);
    private static final Color COLOR_GRADIENTE_ROJO_BAJO = new Color(0xD8, 0xA8, 0x38);
    private static final Color COLOR_GRADIENTE_NEGRO_ALTO = new Color(0x55, 0x44, 0x33);
    private static final Color COLOR_GRADIENTE_NEGRO_BAJO = new Color(0x18, 0x0B, 0x03);
    private static final Color COLOR_ICONO_ROJO = new Color(0x96, 0x26, 0x08);
    private static final Color COLOR_ICONO_NEGRO = new Color(0xDD, 0xCA, 0x8C);
    private static final Color COLOR_BORDE_ROJO = new Color(0x96, 0x26, 0x08);
    private static final Color COLOR_BORDE_NEGRO = new Color(0x70, 0x50, 0x38);
    private static final Color COLOR_BORDE_SELECCION  = new Color(0x00, 0xEE, 0x00);
    private static final Color COLOR_BORDE_OBJETIVO = new Color(0xFF, 0xAA, 0x00);
    private static final Color COLOR_PUNTO_MOVIMIENTO = new Color(40, 190, 40, 155);
    private static final Color COLOR_ANILLO_MOVIMIENTO = new Color(20, 140, 20, 200);

    private final GameState estadoJuego;
    private int filaSeleccionada  = -1;
    private int columnaSeleccionada = -1;
    private List<int[]> movimientosValidos = new ArrayList<>();
    private final Deque<Object[]> historialMovimientos = new ArrayDeque<>();
    private final List<Piece> piezasCapturadasPorRojo  = new ArrayList<>();
    private final List<Piece> piezasCapturadasPorNegro = new ArrayList<>();

    private Font fuentePieza;
    private Font fuenteInterfaz;
    private Font fuenteRio;

    public PanelXianqui(GameState estadoJuego) {
        this.estadoJuego = estadoJuego;
        setPreferredSize(new Dimension(ANCHO_TABLERO, ALTO_TABLERO + ALTURA_ESTADO));
        setBackground(COLOR_FONDO);
        setFocusable(true);
        addMouseListener(this);
        inicializarFuentes();
    }

    private void inicializarFuentes() {
        String[] candidatosFuente = {
            "SimSun", "NSimSun", "STSong", "PingFang SC",
            "Hiragino Sans GB", "WenQuanYi Micro Hei",
            "Noto Serif CJK SC", "Dialog", "Serif"
        };
        Font fuenteEncontrada = new Font("Serif", Font.BOLD, 22);
        for (String nombreFuente : candidatosFuente) {
            Font fuente = new Font(nombreFuente, Font.BOLD, 22);
            if (fuente.canDisplay('帅')) { fuenteEncontrada = fuente; 
            break; 
            }
        }
        fuentePieza = fuenteEncontrada;
        fuenteRio = fuenteEncontrada.deriveFont(Font.BOLD, 20f);
        fuenteInterfaz = fuenteEncontrada.deriveFont(Font.BOLD, 15f);
    }

    private int coordenadaX(int columna) 
    { return MARGEN + columna * TAMANO_CELDA; 
    }
    private int coordenadaY(int fila)   
    { return MARGEN + fila    * TAMANO_CELDA; 
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        super.paintComponent(grafico);
        Graphics2D grafico2D = (Graphics2D) grafico;
        grafico2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        grafico2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        grafico2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

        dibujarFondoTablero(grafico2D);
        dibujarLineasTablero(grafico2D);
        dibujarTextoRio(grafico2D);
        dibujarMarcadoresPosicion(grafico2D);
        dibujarPiezas(grafico2D);
        dibujarBarraEstado(grafico2D);
    }

    private void dibujarFondoTablero(Graphics2D grafico2D) {
        grafico2D.setColor(COLOR_MARCO_TABLERO);
        grafico2D.fillRoundRect(MARGEN - 8, MARGEN - 8, 8 * TAMANO_CELDA + 16, 9 * TAMANO_CELDA + 16, 10, 10);

        grafico2D.setColor(COLOR_FONDO_TABLERO);
        grafico2D.fillRoundRect(MARGEN - 2, MARGEN - 2, 8 * TAMANO_CELDA + 4, 9 * TAMANO_CELDA + 4, 6, 6);

        grafico2D.setColor(new Color(140, 90, 0, 18));
        grafico2D.setStroke(new BasicStroke(1f));
        for (int xTextura = MARGEN; xTextura < MARGEN + 8 * TAMANO_CELDA; xTextura += 3) {
            grafico2D.drawLine(xTextura, MARGEN, xTextura, MARGEN + 9 * TAMANO_CELDA);
        }
    }

    private void dibujarLineasTablero(Graphics2D grafico2D) {
        grafico2D.setColor(COLOR_LINEA);

        for (int fila = 0; fila < 10; fila++) {
            grafico2D.setStroke(new BasicStroke(fila == 0 || fila == 9 ? 2.5f : 1.2f));
            grafico2D.drawLine(coordenadaX(0), coordenadaY(fila), coordenadaX(8), coordenadaY(fila));
        }

        grafico2D.setStroke(new BasicStroke(2.5f));
        grafico2D.drawLine(coordenadaX(0), coordenadaY(0), coordenadaX(0), coordenadaY(9));
        grafico2D.drawLine(coordenadaX(8), coordenadaY(0), coordenadaX(8), coordenadaY(9));

        grafico2D.setStroke(new BasicStroke(1.2f));
        for (int columna = 1; columna <= 7; columna++) {
            grafico2D.drawLine(coordenadaX(columna), coordenadaY(0), coordenadaX(columna), coordenadaY(4));
            grafico2D.drawLine(coordenadaX(columna), coordenadaY(5), coordenadaX(columna), coordenadaY(9));
        }

        grafico2D.setStroke(new BasicStroke(1.3f));
        
        grafico2D.drawLine(coordenadaX(3), coordenadaY(0), coordenadaX(5), coordenadaY(2));
        grafico2D.drawLine(coordenadaX(5), coordenadaY(0), coordenadaX(3), coordenadaY(2));
       
        grafico2D.drawLine(coordenadaX(3), coordenadaY(7), coordenadaX(5), coordenadaY(9));
        grafico2D.drawLine(coordenadaX(5), coordenadaY(7), coordenadaX(3), coordenadaY(9));
    }

    private void dibujarTextoRio(Graphics2D grafico2D) {
        grafico2D.setColor(COLOR_LINEA);
        grafico2D.setFont(fuenteRio);
    }

    private void dibujarMarcadoresPosicion(Graphics2D grafico2D) {
        grafico2D.setColor(COLOR_LINEA);
        grafico2D.setStroke(new BasicStroke(1.5f));
        int[][] posiciones = {
            {7, 1}, {7, 7}, {2, 1}, {2, 7},
            {6, 0}, {6, 2}, {6, 4}, {6, 6}, {6, 8},
            {3, 0}, {3, 2}, {3, 4}, {3, 6}, {3, 8}
        };
        int tamanoEsquina = 6;
        for (int[] posicion : posiciones) {
            int xCentro = coordenadaX(posicion[1]);
            int yCentro = coordenadaY(posicion[0]);
            boolean tieneLado = posicion[1] > 0;
            boolean tieneDerecho = posicion[1] < 8;
            boolean tieneArriba = posicion[0] > 0;
            boolean tieneAbajo = posicion[0] < 9;

            if (tieneLado  && tieneArriba) {
                grafico2D.drawLine(xCentro - tamanoEsquina, yCentro, xCentro - 
                        tamanoEsquina, yCentro - tamanoEsquina);
                grafico2D.drawLine(xCentro, yCentro - tamanoEsquina, xCentro - 
                        tamanoEsquina, yCentro - tamanoEsquina);
            }
            if (tieneDerecho && tieneArriba) {
                grafico2D.drawLine(xCentro + tamanoEsquina, yCentro, xCentro + 
                        tamanoEsquina, yCentro - tamanoEsquina);
                grafico2D.drawLine(xCentro, yCentro - tamanoEsquina, xCentro + 
                        tamanoEsquina, yCentro - tamanoEsquina);
            }
            if (tieneLado  && tieneAbajo) {
                grafico2D.drawLine(xCentro - tamanoEsquina, yCentro, xCentro - 
                        tamanoEsquina, yCentro + tamanoEsquina);
                grafico2D.drawLine(xCentro, yCentro + tamanoEsquina, xCentro - 
                        tamanoEsquina, yCentro + tamanoEsquina);
            }
            if (tieneDerecho && tieneAbajo) {
                grafico2D.drawLine(xCentro + tamanoEsquina, yCentro, xCentro + 
                        tamanoEsquina, yCentro + tamanoEsquina);
                grafico2D.drawLine(xCentro, yCentro + tamanoEsquina, xCentro + 
                        tamanoEsquina, yCentro + tamanoEsquina);
            }
        }
    }
    private void dibujarPiezas(Graphics2D grafico2D) {
        Piece[][] tablero = estadoJuego.getBoard();
        int diametroPieza = TAMANO_CELDA - 10;

        for (int fila = 0; fila < GameState.FILAS; fila++) {
            for (int columna = 0; columna < GameState.COLUMNAS; columna++) {
                int xCentro = coordenadaX(columna);
                int yCentro = coordenadaY(fila);
                boolean estaSeleccionada = (fila == filaSeleccionada && columna == columnaSeleccionada);
                boolean esObjetivo = esObjetivo(fila, columna);
                Piece pieza = tablero[fila][columna];

                if (esObjetivo && pieza == null) {
                    grafico2D.setColor(COLOR_PUNTO_MOVIMIENTO);
                    grafico2D.fillOval(xCentro - 9, yCentro - 9, 18, 18);
                    grafico2D.setColor(COLOR_ANILLO_MOVIMIENTO);
                    grafico2D.setStroke(new BasicStroke(2f));
                    grafico2D.drawOval(xCentro - 9, yCentro - 9, 18, 18);
                }

                if (pieza == null) continue;

                boolean esRoja = pieza.color == Piece.Color.ROJO;

                grafico2D.setColor(new Color(0, 0, 0, 75));
                grafico2D.fillOval(xCentro - diametroPieza / 2 + 3,
                                   yCentro - diametroPieza / 2 + 4,
                                   diametroPieza, diametroPieza);

                GradientPaint gradiente = esRoja
                    ? new GradientPaint(xCentro - diametroPieza / 3, yCentro - diametroPieza / 3,
                                        COLOR_GRADIENTE_ROJO_ALTO,
                                        xCentro + diametroPieza / 3, yCentro + diametroPieza / 3,
                                        COLOR_GRADIENTE_ROJO_BAJO)
                    : new GradientPaint(xCentro - diametroPieza / 3, yCentro - diametroPieza / 3,
                                        COLOR_GRADIENTE_NEGRO_ALTO,
                                        xCentro + diametroPieza / 3, yCentro + diametroPieza / 3,
                                        COLOR_GRADIENTE_NEGRO_BAJO);
                grafico2D.setPaint(gradiente);
                grafico2D.fillOval(xCentro - diametroPieza / 2, yCentro - diametroPieza / 2,
                                   diametroPieza, diametroPieza);

                if (estaSeleccionada || esObjetivo) {
                    Color colorAura = estaSeleccionada
                        ? new Color(0, 238, 0, 55)
                        : new Color(255, 170, 0, 55);
                    grafico2D.setColor(colorAura);
                    grafico2D.setStroke(new BasicStroke(7f));
                    grafico2D.drawOval(xCentro - diametroPieza / 2 - 3,
                                       yCentro - diametroPieza / 2 - 3,
                                       diametroPieza + 6, diametroPieza + 6);
                }

                Color colorBorde = estaSeleccionada ? COLOR_BORDE_SELECCION
                    : esObjetivo ? COLOR_BORDE_OBJETIVO
                    : (esRoja ? COLOR_BORDE_ROJO : COLOR_BORDE_NEGRO);
                grafico2D.setColor(colorBorde);
                grafico2D.setStroke(new BasicStroke(estaSeleccionada || esObjetivo ? 3f : 2.5f));
                grafico2D.drawOval(xCentro - diametroPieza / 2, yCentro - diametroPieza / 2,
                                   diametroPieza, diametroPieza);

             
                grafico2D.setColor(esRoja
                    ? new Color(0xC0, 0x88, 0x28, 70)
                    : new Color(0xFF, 0xFF, 0xFF, 18));
                grafico2D.setStroke(new BasicStroke(1f));
                grafico2D.drawOval(xCentro - diametroPieza / 2 + 4,
                                   yCentro - diametroPieza / 2 + 4,
                                   diametroPieza - 8, diametroPieza - 8);

                Color colorIcono = esRoja ? COLOR_ICONO_ROJO : COLOR_ICONO_NEGRO;
                Representation.draw(grafico2D, pieza.type, colorIcono, xCentro, yCentro, diametroPieza);
            }
        }
    }

    
    private void dibujarBarraEstado(Graphics2D grafico2D) {
        int yInicio = ALTO_TABLERO + 4;

        grafico2D.setColor(new Color(0, 0, 0, 130));
        grafico2D.fillRoundRect(MARGEN, yInicio, 8 * TAMANO_CELDA, ALTURA_ESTADO - 6, 10, 10);

        grafico2D.setFont(fuenteInterfaz);
        FontMetrics medidaFuente = grafico2D.getFontMetrics();
        String textoEstado;
        Color colorEstado;

        if (estadoJuego.isGameOver()) {
            Piece.Color colorGanador = estadoJuego.getWinner();
            textoEstado  = colorGanador == Piece.Color.ROJO ? "Rojo gana!" : "Negro gana!";
            colorEstado  = colorGanador == Piece.Color.ROJO
                ? new Color(0xFF, 0x80, 0x55)
                : new Color(0xCC, 0xCC, 0xCC);
        } else if (estadoJuego.isChecked(estadoJuego.getTurn())) {
            textoEstado = "JAQUE!  " + (estadoJuego.getTurn() == Piece.Color.ROJO ? "红方将军" : "黑方将");
            colorEstado = new Color(0xFF, 0x50, 0x50);
        } else {
            textoEstado = "Turno: " + (estadoJuego.getTurn() == Piece.Color.ROJO ? "Rojo" : "Negro");
            colorEstado = estadoJuego.getTurn() == Piece.Color.ROJO
                ? new Color(0xFF, 0x90, 0x55)
                : new Color(0xBB, 0xBB, 0xBB);
        }

        grafico2D.setColor(colorEstado);
        grafico2D.drawString(textoEstado,
            (ANCHO_TABLERO - medidaFuente.stringWidth(textoEstado)) / 2,
            yInicio + 20);

        int tamanoIcono= 22;
        int yFilaRojo= yInicio + 32;
        int yFilaNegro = yInicio + 56;

        grafico2D.setFont(new Font("SansSerif", Font.BOLD, 10));
        grafico2D.setColor(new Color(0xFF, 0x80, 0x55));
        grafico2D.drawString("Rojo:",  MARGEN + 2, yFilaRojo  + 4);
        grafico2D.setColor(new Color(0xAA, 0xAA, 0xAA));
        grafico2D.drawString("Negro:", MARGEN + 2, yFilaNegro + 4);

        int xInicioCapturadas = MARGEN + 40;
        int anchoMaximoCapturadas = 8 * TAMANO_CELDA - 44;

        dibujarPiezasCapturadas(grafico2D, piezasCapturadasPorRojo,
            xInicioCapturadas, yFilaRojo,  tamanoIcono, anchoMaximoCapturadas);
        dibujarPiezasCapturadas(grafico2D, piezasCapturadasPorNegro,
            xInicioCapturadas, yFilaNegro, tamanoIcono, anchoMaximoCapturadas);
    }

    private void dibujarPiezasCapturadas(Graphics2D grafico2D, List<Piece> listaPiezas,
            int xInicio, int yCentro, int tamanoIcono, int anchoMaximo) {
        if (listaPiezas.isEmpty()) {
            grafico2D.setFont(new Font("SansSerif", Font.PLAIN, 10));
            grafico2D.setColor(new Color(0x50, 0x40, 0x28));
            grafico2D.drawString("—", xInicio, yCentro + 4);
            return;
        }

        Map<Piece.Type, Integer> conteoPorTipo = new java.util.LinkedHashMap<>();
        for (Piece pieza : listaPiezas) conteoPorTipo.merge(pieza.type, 1, Integer::sum);

        int xActual = xInicio;
        int diametroIcono = tamanoIcono - 2;
        boolean sonNegras = listaPiezas.get(0).color == Piece.Color.NEGRO;
        Color colorIcono= sonNegras ? COLOR_ICONO_NEGRO : COLOR_ICONO_ROJO;

        for (Map.Entry<Piece.Type, Integer> entrada : conteoPorTipo.entrySet()) {
            if (xActual + tamanoIcono > xInicio + anchoMaximo)
                break;

            boolean sonRojas = !sonNegras;
            GradientPaint gradiente = sonRojas
                ? new GradientPaint(xActual, yCentro - diametroIcono / 2,
              COLOR_GRADIENTE_ROJO_ALTO,
                         xActual, yCentro + diametroIcono / 2,
                           COLOR_GRADIENTE_ROJO_BAJO)
                : new GradientPaint(xActual, yCentro - diametroIcono / 2,
                                    COLOR_GRADIENTE_NEGRO_ALTO,
                                    xActual, yCentro + diametroIcono / 2,
                                    COLOR_GRADIENTE_NEGRO_BAJO);
            grafico2D.setPaint(gradiente);
            grafico2D.fillOval(xActual - diametroIcono / 2, yCentro - diametroIcono / 2,
                               diametroIcono, diametroIcono);
            grafico2D.setColor(sonRojas ? COLOR_BORDE_ROJO : COLOR_BORDE_NEGRO);
            grafico2D.setStroke(new BasicStroke(1.5f));
            grafico2D.drawOval(xActual - diametroIcono / 2, yCentro - diametroIcono / 2,
                               diametroIcono, diametroIcono);

            Representation.draw(grafico2D, entrada.getKey(), colorIcono, xActual, yCentro, diametroIcono);

            if (entrada.getValue() > 1) {
                grafico2D.setFont(new Font("SansSerif", Font.BOLD, 9));
                grafico2D.setColor(new Color(0xFF, 0xEE, 0x55));
                grafico2D.drawString("×" + entrada.getValue(),
                    xActual + diametroIcono / 2 - 2,
                    yCentro - diametroIcono / 2 + 1);
            }

            xActual += tamanoIcono + 3;
        }
    }
    @Override
    public void mouseClicked(MouseEvent evento) {
        if (estadoJuego.isGameOver()) 
            return;
        requestFocusInWindow();

        int columna = Math.round((float)(evento.getX() - MARGEN) / TAMANO_CELDA);
        int fila = Math.round((float)(evento.getY() - MARGEN) / TAMANO_CELDA);

        if (!GameState.inBounds(fila, columna)) { deseleccionar(); repaint(); 
        return; 
        }

        Piece pieza = estadoJuego.getPiece(fila, columna);

        if (filaSeleccionada >= 0) {
            if (esObjetivo(fila, columna)) {
                historialMovimientos.push(new Object[]{ estadoJuego.getBoardCopy(), estadoJuego.getTurn() });

                Piece piezaCapturada = estadoJuego.getPiece(fila, columna);
                if (piezaCapturada != null) {
                    if (estadoJuego.getTurn() == Piece.Color.ROJO)
                        piezasCapturadasPorRojo.add(piezaCapturada);
                    else
                        piezasCapturadasPorNegro.add(piezaCapturada);
                }
                estadoJuego.makeMove(filaSeleccionada, columnaSeleccionada, fila, columna);
                deseleccionar();
                repaint();
                return;
            }
            if (pieza == null || pieza.color != estadoJuego.getTurn()) {
                deseleccionar(); repaint(); return;
            }
        }

        if (pieza != null && pieza.color == estadoJuego.getTurn()) {
            filaSeleccionada  = fila;
            columnaSeleccionada= columna;
            movimientosValidos = estadoJuego.legalMoves(fila, columna);
        }
        repaint();
    }
    public void undo() {
        if (historialMovimientos.isEmpty()) return;
        Object[] estadoGuardado = historialMovimientos.pop();
        estadoJuego.restoreBoard((Piece[][]) estadoGuardado[0], (Piece.Color) estadoGuardado[1]);
        recalcularPiezasCapturadas();
        deseleccionar();
        repaint();
    }

    public void newGame() {
        estadoJuego.reset();
        historialMovimientos.clear();
        piezasCapturadasPorRojo.clear();
        piezasCapturadasPorNegro.clear();
        deseleccionar();
        repaint();
    }

    private void recalcularPiezasCapturadas() {
        piezasCapturadasPorRojo.clear();
        piezasCapturadasPorNegro.clear();

        Piece[][] tablero = estadoJuego.getBoard();
        int[] conteoActual  = new int[Piece.Type.values().length * 2];
        int[] conteoInicial = contarPiezasIniciales();

        for (int fila = 0; fila < GameState.FILAS; fila++) {
            for (int columna = 0; columna < GameState.COLUMNAS; columna++) {
                if (tablero[fila][columna] != null)
                    conteoActual[calcularIndice(tablero[fila][columna])]++;
            }
        }

        for (Piece.Type tipo : Piece.Type.values()) {
            int rojasFaltantes  = conteoInicial[calcularIndice(tipo, Piece.Color.ROJO)]
                                 - conteoActual[calcularIndice(tipo, Piece.Color.ROJO)];
            int negrasFaltantes = conteoInicial[calcularIndice(tipo, Piece.Color.NEGRO)]
                                 - conteoActual[calcularIndice(tipo, Piece.Color.NEGRO)];
            for (int indice = 0; indice < rojasFaltantes;  indice++)
                piezasCapturadasPorNegro.add(Piece.crear(tipo, Piece.Color.ROJO));
            for (int indice = 0; indice < negrasFaltantes; indice++)
                piezasCapturadasPorRojo.add(Piece.crear(tipo, Piece.Color.NEGRO));
        }
    }

    private int[] contarPiezasIniciales() {
        int[] conteo = new int[Piece.Type.values().length * 2];
        int[] cantidadRojas  = {1, 2, 2, 2, 2, 2, 5}; 
        int[] cantidadNegras = {1, 2, 2, 2, 2, 2, 5};
        Piece.Type[] tipos   = Piece.Type.values();
        for (int indice = 0; indice < tipos.length; indice++) {
            conteo[calcularIndice(tipos[indice], Piece.Color.ROJO)]  = cantidadRojas[indice];
            conteo[calcularIndice(tipos[indice], Piece.Color.NEGRO)] = cantidadNegras[indice];
        }
        return conteo;
    }

    private int calcularIndice(Piece pieza) {
        return calcularIndice(pieza.type, pieza.color);
    }

    private int calcularIndice(Piece.Type tipo, Piece.Color color) {
        return tipo.ordinal() * 2 + (color == Piece.Color.ROJO ? 0 : 1);
    }

    
    private boolean esObjetivo(int fila, int columna) {
        for (int[] movimiento : movimientosValidos)
            if (movimiento[0] == fila && movimiento[1] == columna) return true;
        return false;
    }

    private void deseleccionar() {
        filaSeleccionada    = -1;
        columnaSeleccionada = -1;
        movimientosValidos.clear();
    }

   
    @Override public void mousePressed(MouseEvent evento){
    }
    @Override public void mouseReleased(MouseEvent evento){
    }
    @Override public void mouseEntered(MouseEvent evento){
    }
    @Override public void mouseExited(MouseEvent evento){
    }
}