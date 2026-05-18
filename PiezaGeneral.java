package xianqui;

import java.util.ArrayList;
import java.util.List;

public final class PiezaGeneral extends Piece {

    public PiezaGeneral(Color color) {
        super(Type.GENERAL, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        int[][] ortogonales = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : ortogonales) {
            int fd = fila + d[0];
            int cd = columna + d[1];
            if (GameState.estaEnPalacio(fd, cd, this.color)
                    && GameState.puedeMoverse(tablero, fd, cd, this.color))
                movimientos.add(new int[]{fd, cd});
        }
        return movimientos;
    }
}