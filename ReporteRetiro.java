
package xianqui;


public class ReporteRetiro extends Reportes{
      public ReporteRetiro(Player ganador, Player retirado){
        super(ganador, retirado, "Te has retirado de la partida"); 
        
      }
      
      public ReporteGanador getGanador(){
          return new ReporteGanador(ganador,perdedor);
      }
}
