package matthias.tictactoe.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.model.Player;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
@Scope("prototype")
public class GamePlayerManager {
    private final GameEventPublisher gameEventPublisher;
    private final Map<String, Player> players = new HashMap<>();

    public Player addPlayer(Player player) {
        if(containsPlayer(player.getName())) {
            throw new RuntimeException("Player is already in the room");
        }

        this.players.put(player.getName(), player);
        gameEventPublisher.publishPlayerJoinedEvent(player);

        return player;
    }

    public Player removePlayer(String name) {
        if(!containsPlayer(name)) {
            throw new RuntimeException("Player is not in the room");
        }

        Player removedPlayer = players.remove(name);
        gameEventPublisher.publishPlayerLeftEvent(removedPlayer);

        return removedPlayer;
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
