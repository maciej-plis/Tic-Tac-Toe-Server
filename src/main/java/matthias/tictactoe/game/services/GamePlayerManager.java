package matthias.tictactoe.game.services;

import matthias.tictactoe.game.model.Symbol;
import matthias.tictactoe.web.authentication.model.User;

import java.util.*;

public class GamePlayerManager {
    private final GameEventPublisher publisher;
    Map<Symbol, User> players = new EnumMap<>(Symbol.class);

    public GamePlayerManager(GameEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void newPlayer(User player) {
        if(containsPlayer(player)) {
            throw new RuntimeException("Player is already in the room");
        }

        List<Symbol> availableSymbols = getAvailableSymbols();

        if(availableSymbols.isEmpty()) {
            throw new RuntimeException("Room is full");
        }

        this.players.put(availableSymbols.get(0), player);
        this.publisher.publishPlayerJoinedEvent(availableSymbols.get(0), player);
    }

    public Symbol removePlayer(User player) {
        if(!containsPlayer(player)) {
            throw new RuntimeException("Player is not in the room");
        }

        Symbol symbol = getPlayerSymbol(player);
        players.values().remove(player);
        this.publisher.publishPlayerLeftEvent(symbol, player);

        return symbol;
    }

    public User getPlayer(Symbol symbol) {
        return players.get(symbol);
    }

    public boolean containsPlayer(User player) {
        return players.containsValue(player);
    }

    public Symbol getPlayerSymbol(User player) {
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

    public Map<Symbol, User> getPlayers() {
        return this.players;
    }

    private List<Symbol> getAvailableSymbols() {
        List<Symbol> availableSymbols = new ArrayList<>(Arrays.asList(Symbol.values()));

        availableSymbols.remove(Symbol.EMPTY);
        availableSymbols.removeAll(players.keySet());

        return availableSymbols;
    }
}
