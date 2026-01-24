package matthias.tictactoe.game.services;

import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.PlayerInsertionException;
import matthias.tictactoe.game.exceptions.PlayerRemovalException;
import matthias.tictactoe.game.model.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
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

        log.info("Adding player {}", player.getName());
        this.players.put(player.getName(), player);
        eventCallback.accept(GameEventFactory.createPlayerJoinedEvent(player));

        return player;
    }

    public Player removePlayer(String name) {
        if(!containsPlayer(name)) {
            throw new PlayerRemovalException(String.format("Couldn't find player named \"%s\".", name));
        }

        log.info("Removing player {}", name);
        Player removedPlayer = players.remove(name);
        eventCallback.accept(GameEventFactory.createPlayerLeftEvent(removedPlayer));

        return removedPlayer;
    }

    public Player getPlayer(String name) {
        log.info("Getting player {}", name);
        return players.get(name);
    }

    public boolean containsPlayer(String name) {
        log.info("Checking if player exists {}", name);
        return players.containsKey(name);
    }

    public Collection<Player> getPlayers() {
        log.info("Getting all players");
        return this.players.values();
    }
}
