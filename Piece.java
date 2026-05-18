package xianqui;

import java.util.List;


public abstract class Piece {

    public enum Type {
        GENERAL, CONSEJEROS, ELEFANTES, CABALLOS, CARROS, CANONES, SOLDADOS
    }

    public enum Color {
        ROJO, NEGRO;
        public Color opposite() { 
            return this == ROJO ? NEGRO : ROJO; 
        }
    }

    public final Type type;
    public final Color color;

    protected Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

   
    public abstract List<int[]> obtenerMovimientosCrudos(Piece[][] tablero, int fila, int columna);

    public static Piece crear(Type tipo, Color color) {
        switch (tipo) {
            case CARROS:     
                return new PiezaCarro(color);
            case CANONES:   
                return new PiezaCanon(color);
            case CABALLOS:   
                return new PiezaCaballo(color);
            case ELEFANTES:  
                return new PiezaElefante(color);
            case CONSEJEROS:
                return new PiezaConsejero(color);
            case GENERAL:    
                return new PiezaGeneral(color);
            case SOLDADOS:   
                return new PiezaSoldado(color);
            default:        
                return new PiezaSoldado(color);
        }
    }
}