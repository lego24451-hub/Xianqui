
package xianqui;


public class ReporteRetiro extends Reportes{
      public ReporteRetiro(Player ganador, Player retirado){
        super(ganador, retirado, "HAS PERDIDO POR RETIRO: " + retirado.getUsername()); 
        
      }
      
      public ReporteGanador getGanador(){
          return new ReporteGanador(ganador,perdedor);
      }
}
