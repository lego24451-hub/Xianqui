package xianqui;

import java.util.ArrayList;

public interface IStorage {

    
    boolean crearPlayer(Player player);
    boolean guardarPlayer(Player player);
    Player  buscarPlayer(String username);
    ArrayList<Player> obtenerTodosLosPlayers();
    boolean eliminarPlayer(String username);

    boolean guardarLog(String username, String entradaLog);
    ArrayList<String> obtenerLogs(String username);
    boolean eliminarLogs(String username);
}