package xianqui;

import java.util.ArrayList;
import java.util.List;

public final class PiezaCarro extends Piece {

    public PiezaCarro(Color color) {
        super(Type.CARROS, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : direcciones) {
            int f = fila + dir[0];
            int c = columna + dir[1];
            while (GameState.inBounds(f, c)) {
                if (tablero[f][c] != null) {
                    if (tablero[f][c].color != this.color)
                        movimientos.add(new int[]{f, c});
                    break;
                }
                movimientos.add(new int[]{f, c});
                f += dir[0];
                c += dir[1];
            }
        }
        return movimientos;
    }
}