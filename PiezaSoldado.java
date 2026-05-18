package xianqui;

import java.util.ArrayList;
import java.util.List;

public final class PiezaSoldado extends Piece {

    public PiezaSoldado(Color color) {
        super(Type.SOLDADOS, color);
    }

    @Override
    public final List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna) {
        List<int[]> movimientos = new ArrayList<>();
        int avance  = (this.color == Color.ROJO) ? -1 : 1;
        boolean hasCruzadoRio = (this.color == Color.ROJO) ? fila < 5 : fila > 4;

        if (GameState.puedeMoverse(tablero, fila + avance, columna, this.color))
            movimientos.add(new int[]{fila + avance, columna});

        if (hasCruzadoRio) {
            if (GameState.puedeMoverse(tablero, fila, columna - 1, this.color))
                movimientos.add(new int[]{fila, columna - 1});
            if (GameState.puedeMoverse(tablero, fila, columna + 1, this.color))
                movimientos.add(new int[]{fila, columna + 1});
        }
        return movimientos;
    }
}