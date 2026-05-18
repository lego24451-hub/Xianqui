package xianqui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Player {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String username;
    private String password;
    private int puntos;
    private String fechaIngreso;
    private boolean activo;

    public Player(String username, String password) {
        this.username = username;
        this.password= password;
        this.puntos= 0;
        this.fechaIngreso = LocalDate.now().format(FORMATO_FECHA);
        this.activo = true;
    }

    
    public final String  getUsername(){ 
        return username; 
    }
    public final String  getPassword(){ 
        return password; 
    }
    public final int getPuntos(){ 
        return puntos; 
    }
    public final String  getFechaIngreso(){ return 
            fechaIngreso; 
    }
    public final boolean isActivo(){ 
        return activo; 
    }

    public final void setPassword(String nuevoPassword) {
        this.password = nuevoPassword; 
    }
    public final void setActivo(boolean estadoActivo){ 
        this.activo   = estadoActivo; 
    }
    public final void agregarPuntos(int puntosGanados){ 
        this.puntos  += puntosGanados; 
    }
}