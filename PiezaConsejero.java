package xianqui;

import java.util.ArrayList;
import java.util.List;


public final class PiezaConsejero extends Piece {

    public PiezaConsejero(Color color) {
        super(Type.CONSEJEROS, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        int[][] diagonales = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] d : diagonales) {
            int fd = fila + d[0];
            int cd = columna + d[1];
            if (GameState.estaEnPalacio(fd, cd, this.color)
                    && GameState.puedeMoverse(tablero, fd, cd, this.color))
                movimientos.add(new int[]{fd, cd});
        }
        return movimientos;
    }
}