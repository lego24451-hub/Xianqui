package xianqui;

import java.util.*;

public class GameState {

    public static final int FILAS = 10;
    public static final int COLUMNAS = 9;

    public static final int ROWS =FILAS;
    public static final int COLS = COLUMNAS;

    private Piece[][]  tablero;
    private Piece.Color turnoActual;
    private boolean juegoTerminado;
    private Piece.Color ganador;

    public GameState() {
        reset();
    }

    public void reset() {
        tablero = new Piece[FILAS][COLUMNAS];
        turnoActual = Piece.Color.ROJO;
        juegoTerminado = false;
        ganador = null;
        configurarTableroInicial();
    }

    private void configurarTableroInicial() {
        Piece.Type[] tiposFila = {
            Piece.Type.CARROS, Piece.Type.CABALLOS, Piece.Type.ELEFANTES,
            Piece.Type.CONSEJEROS, Piece.Type.GENERAL,Piece.Type.CONSEJEROS,
            Piece.Type.ELEFANTES,  Piece.Type.CABALLOS,Piece.Type.CARROS
        };

        for (int col = 0; col < 9; col++)
            tablero[9][col] = Piece.crear(tiposFila[col], Piece.Color.ROJO);
        tablero[7][1] = Piece.crear(Piece.Type.CANONES, Piece.Color.ROJO);
        tablero[7][7] = Piece.crear(Piece.Type.CANONES, Piece.Color.ROJO);
        for (int col : new int[]{0, 2, 4, 6, 8})
            tablero[6][col] = Piece.crear(Piece.Type.SOLDADOS, Piece.Color.ROJO);

        for (int col = 0; col < 9; col++)
            tablero[0][col] = Piece.crear(tiposFila[col], Piece.Color.NEGRO);
        tablero[2][1] = Piece.crear(Piece.Type.CANONES, Piece.Color.NEGRO);
        tablero[2][7] = Piece.crear(Piece.Type.CANONES, Piece.Color.NEGRO);
        for (int col : new int[]{0, 2, 4, 6, 8})
            tablero[3][col] = Piece.crear(Piece.Type.SOLDADOS, Piece.Color.NEGRO);
    }

    public Piece getPiece(int fila, int columna) {
        return esDentroDelTablero(fila, columna) ? tablero[fila][columna] : null;
    }

    public Piece[][] getBoard(){ 
        return tablero; 
    }
    public Piece.Color getTurn(){ 
        return turnoActual; 
    }
    public boolean isGameOver(){ 
        return juegoTerminado; 
    }
    public Piece.Color getWinner(){ 
        return ganador; 
    }

    public boolean isChecked(Piece.Color color) {
        return estaEnJaque(tablero, color);
    }

    public static boolean inBounds(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    public static boolean esDentroDelTablero(int fila, int columna) {
        return inBounds(fila, columna);
    }

    public static boolean estaEnPalacio(int fila, int columna, Piece.Color color) {
        return color == Piece.Color.ROJO
            ? (fila >= 7 && fila <= 9 && columna >= 3 && columna <= 5)
            : (fila >= 0 && fila <= 2 && columna >= 3 && columna <= 5);
    }

    public static boolean inPalace(int fila, int columna, Piece.Color color) {
        return estaEnPalacio(fila, columna, color);
    }

    public static boolean puedeMoverse(Piece[][] tablero, int fila, int columna, Piece.Color colorPropio) {
        return inBounds(fila, columna)
            && (tablero[fila][columna] == null
                || tablero[fila][columna].color != colorPropio);
    }

    public static Piece[][] copiarTablero(Piece[][] origen) {
        Piece[][] copia = new Piece[FILAS][COLUMNAS];
        for (int fila = 0; fila < FILAS; fila++)
            System.arraycopy(origen[fila], 0, copia[fila], 0, COLUMNAS);
        return copia;
    }

    public Piece[][] getBoardCopy() {
        return copiarTablero(tablero);
    }

    public void restoreBoard(Piece[][] tableroGuardado, Piece.Color turnoGuardado) {
        tablero = copiarTablero(tableroGuardado);
        turnoActual = turnoGuardado;
        juegoTerminado = false;
        ganador = null;
    }

    public List<int[]> rawMoves(Piece[][] tableroActual, int fila, int columna) {
        Piece pieza = tableroActual[fila][columna];
        if (pieza == null) return Collections.emptyList();
        return pieza.obtenerMovimientosCrudos(tableroActual, fila, columna);
    }

  
    public List<int[]> legalMoves(int fila, int columna) {
        Piece pieza = tablero[fila][columna];
        if (pieza == null) return Collections.emptyList();

        List<int[]> legales = new ArrayList<>();
        for (int[] mov : rawMoves(tablero, fila, columna)) {
            Piece[][] hipotetico = copiarTablero(tablero);
            hipotetico[mov[0]][mov[1]] = hipotetico[fila][columna];
            hipotetico[fila][columna]  = null;
            if (!estaEnJaque(hipotetico, pieza.color))
                legales.add(mov);
        }
        return legales;
    }

    public boolean estaEnJaque(Piece[][] tableroActual, Piece.Color colorPropio) {
        int filaGeneral = -1, colGeneral = -1;
        int filaGeneralEnemigo = -1, colGeneralEnemigo = -1;
        Piece.Color colorEnemigo = colorPropio.opposite();

        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                Piece p = tableroActual[fila][col];
                if (p == null) continue;
                if (p.color == colorPropio  && p.type == Piece.Type.GENERAL) {
                    filaGeneral = fila; colGeneral = col;
                }
                if (p.color == colorEnemigo && p.type == Piece.Type.GENERAL) {
                    filaGeneralEnemigo = fila; colGeneralEnemigo = col;
                }
            }
        }

        if (filaGeneral < 0) return true; 

        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                Piece p = tableroActual[fila][col];
                if (p != null && p.color == colorEnemigo) {
                    for (int[] mov : rawMoves(tableroActual, fila, col)) {
                        if (mov[0] == filaGeneral && mov[1] == colGeneral)
                            return true;
                    }
                }
            }
        }

        
        if (filaGeneralEnemigo >= 0 && colGeneral == colGeneralEnemigo) {
            boolean hayObstaculo = false;
            int filaMin = Math.min(filaGeneral, filaGeneralEnemigo);
            int filaMax = Math.max(filaGeneral, filaGeneralEnemigo);
            for (int f = filaMin + 1; f < filaMax; f++) {
                if (tableroActual[f][colGeneral] != null) { hayObstaculo = true; break; }
            }
            if (!hayObstaculo) return true;
        }
        return false;
    }

    public boolean isInCheck(Piece[][] tableroActual, Piece.Color color) {
        return estaEnJaque(tableroActual, color);
    }

    public void makeMove(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {
        tablero[filaDestino][colDestino] = tablero[filaOrigen][colOrigen];
        tablero[filaOrigen][colOrigen]   = null;
        turnoActual = turnoActual.opposite();
        if (sinMovimientosDisponibles(turnoActual)) {
            juegoTerminado = true;
            ganador = turnoActual.opposite();
        }
    }

    private boolean sinMovimientosDisponibles(Piece.Color color) {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (tablero[fila][col] != null
                        && tablero[fila][col].color == color
                        && !legalMoves(fila, col).isEmpty())
                    return false;
            }
        }
        return true;
    }
}