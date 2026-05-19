
package xianqui;

public class ReporteGanador extends Reportes{
      public ReporteGanador(Player ganador, Player perdedor){
        super(ganador, perdedor, "HAS GANADO LA PARTIDA: "  + ganador.getUsername()); 
      }
        
}
