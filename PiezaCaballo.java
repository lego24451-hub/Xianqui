package xianqui;

import java.util.ArrayList;
import java.util.List;


public final class PiezaCaballo extends Piece {

    public PiezaCaballo(Color color) {
        super(Type.CABALLOS, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        
        int[][] definiciones = {
            {-2, -1, -1,  0}, {-2,  1, -1,  0},
            { 2, -1,  1,  0}, { 2,  1,  1,  0},
            {-1, -2,  0, -1}, { 1, -2,  0, -1},
            {-1,  2,  0,  1}, { 1,  2,  0,  1}
        };
        for (int[] d : definiciones) {
            int filaBloqueo = fila + d[2];
            int colBloqueo  = columna + d[3];
            if (GameState.inBounds(filaBloqueo, colBloqueo) && tablero[filaBloqueo][colBloqueo] != null)
                continue;
            int fd = fila + d[0];
            int cd = columna + d[1];
            if (GameState.puedeMoverse(tablero, fd, cd, this.color))
                movimientos.add(new int[]{fd, cd});
        }
        return movimientos;
    }
}