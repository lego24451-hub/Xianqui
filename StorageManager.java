package xianqui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StorageManager implements IStorage {

   
    private static StorageManager instancia;

    private StorageManager() {}

    public static StorageManager getInstance() {
        if (instancia == null) instancia = new StorageManager();
        return instancia;
    }

    private final ArrayList<Player>listaPlayers = new ArrayList<>();
    private final HashMap<String, ArrayList<String>> mapaLogs = new HashMap<>();

    @Override
    public boolean crearPlayer(Player player) {
        if (usernameExisteRecursivo(player.getUsername(), 0)) return false;
        listaPlayers.add(player);
        return true;
    }

    private boolean usernameExisteRecursivo(String username, int indice) {
        if (indice >= listaPlayers.size()) return false;
        if (listaPlayers.get(indice).getUsername().equalsIgnoreCase(username)) return true;
        return usernameExisteRecursivo(username, indice + 1);
    }

    @Override
    public boolean guardarPlayer(Player player) {
        int posicion = encontrarIndiceRecursivo(player.getUsername(), 0);
        if (posicion >= 0) {
            listaPlayers.set(posicion, player);
        } else {
            listaPlayers.add(player);
        }
        return true;
    }

    private int encontrarIndiceRecursivo(String username, int indice) {
        if (indice >= listaPlayers.size()) return -1;
        if (listaPlayers.get(indice).getUsername().equalsIgnoreCase(username)) return indice;
        return encontrarIndiceRecursivo(username, indice + 1);
    }

    @Override
    public Player buscarPlayer(String username) {
        int posicion = encontrarIndiceRecursivo(username, 0);
        return posicion >= 0 ? listaPlayers.get(posicion) : null;
    }

    @Override
    public ArrayList<Player> obtenerTodosLosPlayers() {
        return new ArrayList<>(listaPlayers);
    }

    @Override
    public boolean eliminarPlayer(String username) {
        int posicion = encontrarIndiceRecursivo(username, 0);
        if (posicion < 0) return false;
        listaPlayers.remove(posicion);
        eliminarLogs(username);
        return true;
    }

    @Override
    public boolean guardarLog(String username, String entradaLog) {
        mapaLogs.computeIfAbsent(username.toLowerCase(), clave -> new ArrayList<>()).add(entradaLog);
        return true;
    }

    @Override
    public ArrayList<String> obtenerLogs(String username) {
        ArrayList<String> listaLogs = mapaLogs.getOrDefault(username.toLowerCase(), new ArrayList<>());
        ArrayList<String> copiaLogs = new ArrayList<>(listaLogs);
        Collections.reverse(copiaLogs);
        return copiaLogs;
    }

    @Override
    public boolean eliminarLogs(String username) {
        mapaLogs.remove(username.toLowerCase());
        return true;
    }
}