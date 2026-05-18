
package xianqui;


public class Reportes {
    private String mensaje; 
    protected Player ganador; 
    protected Player perdedor; 
    
    
    
    public Reportes(Player ganador, Player perdedor, String mensaje){
        this.ganador = ganador; 
        this.perdedor =  perdedor; 
        this.mensaje = mensaje; 
        
    }
    
    
    public String getMensaje(){
        return mensaje; 
    }
    
    
}
