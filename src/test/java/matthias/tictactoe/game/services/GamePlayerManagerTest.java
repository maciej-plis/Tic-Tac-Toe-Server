package matthias.tictactoe.game.services;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.PlayerInsertionException;
import matthias.tictactoe.game.exceptions.PlayerRemovalException;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GamePlayerManagerTest {
    private GamePlayerManager gamePlayerManager;

    @Mock
    Consumer<GameEvent> eventCallback;

    private Player insertedPlayer = new Player(PlayerSymbol.X, "someName");

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.gamePlayerManager = new GamePlayerManager(eventCallback);
    }

    @Test
    public void newPlayer_addsPlayerToPlayersList() {
        gamePlayerManager.addPlayer(insertedPlayer);

        Collection<Player> players = gamePlayerManager.getPlayers();
        assertTrue(players.contains(insertedPlayer));
    }

    @Test
    public void newPlayer_publishesPlayerJoinedEvent() {
        gamePlayerManager.addPlayer(insertedPlayer);

        verify(eventCallback).accept(ArgumentMatchers.any(GameEvent.class));
    }

    @Test
    public void newPlayer_whenPlayerListAlreadyContainsPlayer_throwsPlayerInsertionException() {
        gamePlayerManager.addPlayer(insertedPlayer);

        assertThrows(PlayerInsertionException.class, () -> gamePlayerManager.addPlayer(insertedPlayer));
    }

    @Test
    public void removePlayer_removesPlayerFromPlayersList() {
        gamePlayerManager.addPlayer(insertedPlayer);

        gamePlayerManager.removePlayer(insertedPlayer.getName());

        assertFalse(gamePlayerManager.getPlayers().contains(insertedPlayer));
    }

    @Test
    public void removePlayer_publishesPlayerLeftEvent() {
        gamePlayerManager.addPlayer(insertedPlayer);

        gamePlayerManager.removePlayer(insertedPlayer.getName());

        verify(eventCallback, times(2)).accept(ArgumentMatchers.any(GameEvent.class));
    }

    @Test
    public void removePlayer_whenPlayerWasNotFound_throwsPlayerRemovalException() {
        assertThrows(PlayerRemovalException.class, () -> gamePlayerManager.removePlayer(insertedPlayer.getName()));
    }

    @Test
    public void getPlayer_whenPlayerWasFound_returnPlayer() {
        gamePlayerManager.addPlayer(insertedPlayer);

        Player foundPlayer = gamePlayerManager.getPlayer(insertedPlayer.getName());

        assertEquals(insertedPlayer, foundPlayer);
    }

    @Test
    public void containsPlayer_whenPlayerWasFound_returnTrue() {
        gamePlayerManager.addPlayer(insertedPlayer);

        assertTrue(gamePlayerManager.containsPlayer(insertedPlayer.getName()));
    }

    @Test
    public void containsPlayer_whenPlayerWasNotFound_returnFalse() {
        assertFalse(gamePlayerManager.containsPlayer(insertedPlayer.getName()));
    }

    @Test
    public void getPlayers_returnsCollectionWithAllAddedPlayers() {
        List<Player> addedPlayers = new ArrayList<>();
        addedPlayers.add(gamePlayerManager.addPlayer(insertedPlayer));
        addedPlayers.add(gamePlayerManager.addPlayer(new Player(PlayerSymbol.O, "someOtherName")));

        Collection<Player> players = gamePlayerManager.getPlayers();

        assertTrue(addedPlayers.containsAll(players) &&
                players.containsAll(addedPlayers));
    }
}