package xianqui;

import java.util.ArrayList;
import java.util.List;


public final class PiezaCanon extends Piece {

    public PiezaCanon(Color color) {
        super(Type.CANONES, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : direcciones) {
            int f = fila + dir[0];
            int c = columna + dir[1];
            boolean saltoRealizado = false;
            while (GameState.inBounds(f, c)) {
                if (!saltoRealizado) {
                    if (tablero[f][c] != null) {
                        saltoRealizado = true;
                    } else {
                        movimientos.add(new int[]{f, c});
                    }
                } else {
                    if (tablero[f][c] != null) {
                        if (tablero[f][c].color != this.color)
                            movimientos.add(new int[]{f, c});
                        break;
                    }
                }
                f += dir[0];
                c += dir[1];
            }
        }
        return movimientos;
    }
}