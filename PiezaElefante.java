package xianqui;

import java.util.ArrayList;
import java.util.List;

public final class PiezaElefante extends Piece {

    public PiezaElefante(Color color) {
        super(Type.ELEFANTES, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        
        int[][] definiciones = {
            {-2, -2, -1, -1}, {-2,  2, -1,  1},
            { 2, -2,  1, -1}, { 2,  2,  1,  1}
        };
        for (int[] d : definiciones) {
            int fd = fila + d[0];
            int cd = columna + d[1];
            if (!GameState.inBounds(fd, cd)) continue;
            // recordatorio: El elefante no puede cruzar el río
            if (this.color == Color.ROJO  && fd < 5) continue;
            if (this.color == Color.NEGRO && fd > 4) continue;
            int fb = fila + d[2];
            int cb = columna + d[3];
            if (GameState.inBounds(fb, cb) && tablero[fb][cb] != null) continue;
            if (GameState.puedeMoverse(tablero, fd, cd, this.color))
                movimientos.add(new int[]{fd, cd});
        }
        return movimientos;
    }
}