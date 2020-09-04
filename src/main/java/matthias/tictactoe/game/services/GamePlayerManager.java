package matthias.tictactoe.game.services;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.PlayerInsertionException;
import matthias.tictactoe.game.exceptions.PlayerRemovalException;
import matthias.tictactoe.game.model.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GamePlayerManager {
    private final Consumer<GameEvent> eventCallback;
    private final Map<String, Player> players;

    public GamePlayerManager(Consumer<GameEvent> eventCallback) {
        this.eventCallback = eventCallback;
        this.players = new HashMap<>();
    }

    public Player addPlayer(Player player) {
        if(containsPlayer(player.getName())) {
            throw new PlayerInsertionException(String.format("Player list already contains player named \"%s\".", player.getName()));
        }

        this.players.put(player.getName(), player);
        eventCallback.accept(GameEventFactory.createPlayerJoinedEvent(player));

        return player;
    }

    public Player removePlayer(String name) {
        if(!containsPlayer(name)) {
            throw new PlayerRemovalException(String.format("Couldn't find player named \"%s\".", name));
        }

        Player removedPlayer = players.remove(name);
        eventCallback.accept(GameEventFactory.createPlayerLeftEvent(removedPlayer));

        return removedPlayer;
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public boolean containsPlayer(String name) {
        return players.containsKey(name);
    }

    public Collection<Player> getPlayers() {
        return this.players.values();
    }
}
