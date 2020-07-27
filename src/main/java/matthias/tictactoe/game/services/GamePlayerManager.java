package matthias.tictactoe.game.services;

import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.Symbol;

import java.util.*;
import java.util.stream.Collectors;

public class GamePlayerManager {

    Map<Symbol, Player> players = new EnumMap<>(Symbol.class);

    public void newPlayer(Player player) {

        if(containsPlayer(player)) {
            throw new RuntimeException("Player is already in the room");
        }

        if(getPlayer(player.getName()) != null) {
            throw new RuntimeException("This name is already taken");
        }

        List<Symbol> availableSymbols = getAvailableSymbols();

        if(availableSymbols.isEmpty()) {
            throw new RuntimeException("Room is full");
        }

        this.players.put(availableSymbols.get(0), player);
    }

    public void removePlayer(Player player) {

        if(!containsPlayer(player)) {
            throw new RuntimeException("Player is not in the room");
        }

        players.values().remove(player);
    }

    public Player getPlayer(Symbol symbol) {
        return players.get(symbol);
    }

    public Player getPlayer(String name) {
        for(Symbol symbol : players.keySet()) {
            Player p = players.get(symbol);
            if(p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public boolean containsPlayer(Player player) {
        return players.containsValue(player);
    }

    public Symbol getPlayerSymbol(Player player) {
        for(Symbol symbol : players.keySet()) {
            if(players.get(symbol).equals(player)) {
                return symbol;
            }
        }

        return null;
    }

    public int getPlayersCount() {
        return this.players.size();
    }

    private List<Symbol> getAvailableSymbols() {
        List<Symbol> availableSymbols = new ArrayList<>(Arrays.asList(Symbol.values()));

        availableSymbols.remove(Symbol.EMPTY);
        availableSymbols.removeAll(players.keySet());

        return availableSymbols;
    }
}
