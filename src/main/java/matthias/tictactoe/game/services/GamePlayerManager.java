package matthias.tictactoe.game.services;

import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("prototype")
public class GamePlayerManager {
    private final List<PlayerSymbol> availableSymbols = new ArrayList<>(Arrays.asList(PlayerSymbol.values()));
    private final Map<String, Player> players = new HashMap<>();

    public void newPlayer(String name) {
        if(containsPlayer(name)) {
            throw new RuntimeException("Player is already in the room");
        }

        if(availableSymbols.isEmpty()) {
            throw new RuntimeException("Room is full");
        }

        PlayerSymbol symbol = availableSymbols.get(0);
        availableSymbols.remove(symbol);
        Player player = new Player(symbol, name);

        this.players.put(name, player);
        GameEventPublisher.publishPlayerJoinedEvent(player);
    }

    public void removePlayer(String name) {
        if(!containsPlayer(name)) {
            throw new RuntimeException("Player is not in the room");
        }

        Player removedPlayer = players.remove(name);
        GameEventPublisher.publishPlayerLeftEvent(removedPlayer);
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public boolean containsPlayer(String name) {
        return players.containsKey(name);
    }

    public int getPlayersCount() {
        return this.players.size();
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }
}
