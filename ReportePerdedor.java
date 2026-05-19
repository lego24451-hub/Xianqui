
package xianqui;

public class ReportePerdedor extends Reportes{
    
    
    public ReportePerdedor(Player ganador, Player perdedor){
        super(ganador, perdedor, "HAS PERDIDO LA PARTIDA: "  + perdedor.getUsername()); 
        
        
    }
}
