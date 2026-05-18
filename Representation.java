package xianqui;

import java.awt.*;
import java.awt.geom.*;

public class Representation {

    public static void draw(Graphics2D grafico2D, Piece.Type tipo, Color colorIcono,
          int xCentro, int yCentro, int diametro) {
        AffineTransform transformacionGuardada = grafico2D.getTransform();
        Stroke trazoGuardado = grafico2D.getStroke();
        Color colorGuardado = grafico2D.getColor();
        Object antialiasGuardado = grafico2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        grafico2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grafico2D.translate(xCentro, yCentro);
        grafico2D.scale(diametro / 50.0, diametro / 50.0);
        grafico2D.setColor(colorIcono);
        grafico2D.setStroke(new BasicStroke(1.2f));

        switch (tipo) {
            case 
                    CARROS:     dibujarCarro     (grafico2D, colorIcono);
                    break;
            case 
                    CABALLOS:   dibujarCaballo   (grafico2D, colorIcono); 
                    break;
            case 
                    ELEFANTES:  dibujarElefante  (grafico2D, colorIcono);
                    break;
            case 
                    CONSEJEROS: dibujarConsejero (grafico2D, colorIcono); 
                    break;
            case 
                    GENERAL:    dibujarGeneral   (grafico2D, colorIcono); 
                    break;
            case 
                    CANONES:    dibujarCannon    (grafico2D, colorIcono); 
                    break;
            case 
                    SOLDADOS:   dibujarSoldado   (grafico2D, colorIcono); 
                    break;
        }

        grafico2D.setTransform(transformacionGuardada);
        grafico2D.setStroke(trazoGuardado);
        grafico2D.setColor(colorGuardado);
        if (antialiasGuardado != null)
            grafico2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasGuardado);
    }

    private static void dibujarCarro(Graphics2D grafico2D, Color color) {
        rellenarRedondeado(grafico2D, -13, 10, 26, 8, 4);
        grafico2D.fillRect(-10, -12, 20, 22);
        grafico2D.fillRect(-10, -22, 6, 11);
        grafico2D.fillRect(-3,  -22, 6, 11);
        grafico2D.fillRect(4,   -22, 6, 11);
        dibujarSombra(grafico2D, color, () -> grafico2D.fillRect(-2, -8, 4, 12));
    }

    private static void dibujarCaballo(Graphics2D grafico2D, Color color) {
        rellenarRedondeado(grafico2D, -11, 10, 22, 8, 4);

        Path2D cuello = new Path2D.Float();
        cuello.moveTo(-7, 10);
        cuello.lineTo(-6, -2);
        cuello.quadTo(-4, -12, 2, -14);
        cuello.lineTo(6, -14);
        cuello.lineTo(6, -2);
        cuello.quadTo(3, 0, 1, 10);
        cuello.closePath();
        grafico2D.fill(cuello);

        grafico2D.fillOval(2, -24, 16, 12);
        rellenarTriangulo(grafico2D, 4, -24, 8, -31, 12, -24);
        grafico2D.fillOval(16, -20, 6, 7);

        grafico2D.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D crinera = new Path2D.Float();
        crinera.moveTo(-2, -14);
        crinera.curveTo(-5, -20, -3, -26, 2, -24);
        grafico2D.draw(crinera);
        grafico2D.setStroke(new BasicStroke(1.2f));

        dibujarSombra(grafico2D, color, () -> grafico2D.fillOval(8, -22, 4, 4));
    }

    private static void dibujarElefante(Graphics2D grafico2D, Color color) {
        rellenarRedondeado(grafico2D, -13, 10, 26, 8, 4);

        Path2D cuerpo = new Path2D.Float();
        cuerpo.moveTo(-11, 10);
        cuerpo.lineTo(-7, -2);
        cuerpo.lineTo(6, -2);
        cuerpo.lineTo(10, 10);
        cuerpo.closePath();
        grafico2D.fill(cuerpo);

        grafico2D.fillOval(-8, -18, 14, 14);
        grafico2D.fillOval(-14, -16, 8, 9);

        Path2D trompa = new Path2D.Float();
        trompa.moveTo(4, -10);
        trompa.curveTo(14, -8, 17, 0, 13, 8);
        trompa.curveTo(11, 12, 7, 10, 7, 6);
        trompa.curveTo(9, 2, 11, -4, 4, -6);
        trompa.closePath();
        grafico2D.fill(trompa);

        Path2D colmillo = new Path2D.Float();
        colmillo.moveTo(4, -6);
        colmillo.quadTo(12, -3, 16, 4);
        colmillo.lineTo(14, 6);
        colmillo.quadTo(9, 0, 4, -4);
        colmillo.closePath();
        Color colorMarfil = mezclarConBlanco(color, 0.75f);
        grafico2D.setColor(colorMarfil);
        grafico2D.fill(colmillo);
        grafico2D.setColor(color);

        dibujarSombra(grafico2D, color, () -> grafico2D.fillOval(-3, -16, 4, 4));
    }
    private static void dibujarConsejero(Graphics2D grafico2D, Color color) {
        rellenarRedondeado(grafico2D, -13, 10, 26, 8, 4);

        Path2D falda = new Path2D.Float();
        falda.moveTo(-12, 10);
        falda.lineTo(-7, 1);
        falda.lineTo(-3, 0);
        falda.lineTo(3, 0);
        falda.lineTo(7, 1);
        falda.lineTo(12, 10);
        falda.closePath();
        grafico2D.fill(falda);

        rellenarRedondeado(grafico2D, -4, -7, 8, 7, 3);
        grafico2D.fillOval(-8, -20, 16, 14);
        grafico2D.fillOval(-4, -27, 8, 9);
        rellenarTriangulo(grafico2D, -5, -27, -7, -33, -1, -27);
        rellenarTriangulo(grafico2D, -1, -27, 0, -35, 1, -27);
        rellenarTriangulo(grafico2D, 1, -27, 7, -33, 5, -27);
    }

    private static void dibujarGeneral(Graphics2D grafico2D, Color color) {
        rellenarRedondeado(grafico2D, -13, 10, 26, 8, 4);

        Path2D cuerpo = new Path2D.Float();
        cuerpo.moveTo(-12, 10);
        cuerpo.lineTo(-8, 0);
        cuerpo.lineTo(8, 0);
        cuerpo.lineTo(12, 10);
        cuerpo.closePath();
        grafico2D.fill(cuerpo);

        rellenarRedondeado(grafico2D, -5, -8, 10, 8, 3);
        grafico2D.fillOval(-8, -20, 16, 14);
        grafico2D.fillRect(-10, -23, 20, 5);
        rellenarTriangulo(grafico2D, -10, -23, -7, -30, -3, -23);
        rellenarTriangulo(grafico2D, -2,  -23,  0, -33,  2, -23);
        rellenarTriangulo(grafico2D,  3,  -23,  7, -30, 10, -23);
        grafico2D.fillRect(-1, -36, 2, 7);
        grafico2D.fillRect(-3, -33, 6, 2);
    }

    private static void dibujarCannon(Graphics2D grafico2D, Color color) {
        grafico2D.fillOval(-20, 3, 14, 14);
        dibujarSombra(grafico2D, color, () -> grafico2D.fillOval(-17, 6, 8, 8));
        grafico2D.fillOval(6, 3, 14, 14);
        dibujarSombra(grafico2D, color, () -> grafico2D.fillOval(9, 6, 8, 8));
        grafico2D.fillRect(-12, 8, 24, 4);
        grafico2D.fillRoundRect(-10, 2, 20, 7, 4, 4);
        grafico2D.fillRoundRect(-16, -8, 26, 10, 5, 5);
        grafico2D.fillRoundRect(8, -10, 8, 14, 3, 3);
        grafico2D.fillOval(-20, -10, 10, 14);
        grafico2D.fillOval(-4, -12, 6, 6);
    }

    private static void dibujarSoldado(Graphics2D grafico2D, Color color) {
        rellenarRedondeado(grafico2D, -12, 10, 24, 8, 4);

        Path2D cuerpo = new Path2D.Float();
        cuerpo.moveTo(-10, 10);
        cuerpo.lineTo(-6, 1);
        cuerpo.lineTo(6, 1);
        cuerpo.lineTo(10, 10);
        cuerpo.closePath();
        grafico2D.fill(cuerpo);

        rellenarRedondeado(grafico2D, -4, -7, 8, 8, 2);
        grafico2D.fillOval(-9, -22, 18, 17);
    }

    private static void rellenarRedondeado(Graphics2D grafico2D,
            int xOrigen, int yOrigen, int ancho, int alto, int arco) {
        grafico2D.fillRoundRect(xOrigen, yOrigen, ancho, alto, arco, arco);
    }

    private static void rellenarTriangulo(Graphics2D grafico2D,
            int x1, int y1, int x2, int y2, int x3, int y3) {
        Path2D triangulo = new Path2D.Float();
        triangulo.moveTo(x1, y1);
        triangulo.lineTo(x2, y2);
        triangulo.lineTo(x3, y3);
        triangulo.closePath();
        grafico2D.fill(triangulo);
    }

    private static void dibujarSombra(Graphics2D grafico2D, Color colorBase, Runnable accionDibujo) {
        Color colorPrevio = grafico2D.getColor();
        grafico2D.setColor(new Color(
            Math.max(0, colorBase.getRed()   - 80),
            Math.max(0, colorBase.getGreen() - 60),
            Math.max(0, colorBase.getBlue()  - 40),
            180));
        accionDibujo.run();
        grafico2D.setColor(colorPrevio);
    }

    private static Color mezclarConBlanco(Color color, float proporcion) {
        return new Color(
            Math.min(255, (int)(color.getRed()   + (255 - color.getRed())   * proporcion)),
            Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * proporcion)),
            Math.min(255, (int)(color.getBlue()  + (255 - color.getBlue())  * proporcion)),
            200);
    }
}