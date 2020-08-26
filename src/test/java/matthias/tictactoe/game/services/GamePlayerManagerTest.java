package matthias.tictactoe.game.services;

import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class GamePlayerManagerTest {
    private GamePlayerManager gamePlayerManager;

    @Mock
    GameEventPublisher eventPublisher;

    private String name = "someName";

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.gamePlayerManager = new GamePlayerManager(eventPublisher);
    }

    @Test
    public void newPlayer_createsPlayerAndAddHimToPlayersList() {
        Player player = gamePlayerManager.newPlayer(name);

        Collection<Player> players = gamePlayerManager.getPlayers();
        assertTrue(players.contains(player));
    }

    @Test
    public void newPlayer_playerSymbolIsRemovedFromAvailableList() {
        Player player = gamePlayerManager.newPlayer(name);

        List<PlayerSymbol> availableSymbols = gamePlayerManager.getAvailableSymbols();
        assertFalse(availableSymbols.contains(player.getSymbol()));
    }

    @Test
    public void newPlayer_publishesPlayerJoinedEvent() {
        Player player = gamePlayerManager.newPlayer(name);

        verify(eventPublisher).publishPlayerJoinedEvent(player);
    }

    @Test
    public void newPlayer_whenPlayerListAlreadyContainsPlayer_throwsException() {
        gamePlayerManager.newPlayer(name);

        assertThrows(RuntimeException.class, () -> gamePlayerManager.newPlayer(name));
    }

    @Test
    public void newPlayer_whenThereIsNoAvailableSymbols_throwsException() {
        int symbolsCount = gamePlayerManager.getAvailableSymbols().size();

        for(int i=0; i<symbolsCount; i++) {
            gamePlayerManager.newPlayer(name + i);
        }

        assertThrows(RuntimeException.class, () -> gamePlayerManager.newPlayer(name + symbolsCount));
    }

    @Test
    public void removePlayer_removesPlayerFromPlayersList() {
        gamePlayerManager.newPlayer(name);

        gamePlayerManager.removePlayer(name);

        assertTrue(gamePlayerManager.getPlayers().isEmpty());
    }

    @Test
    public void removePlayer_removesPreviouslyAddedPlayer() {
        Player addedPlayer = gamePlayerManager.newPlayer(name);

        Player removedPlayer = gamePlayerManager.removePlayer(name);

        assertEquals(addedPlayer, removedPlayer);
    }

    @Test
    public void removePlayer_setsPlayerSymbolAsAvailable() {
        gamePlayerManager.newPlayer(name);

        Player removedPlayer = gamePlayerManager.removePlayer(name);

        List<PlayerSymbol> symbols = gamePlayerManager.getAvailableSymbols();
        assertTrue(symbols.contains(removedPlayer.getSymbol()));
    }

    @Test
    public void removePlayer_publishesPlayerLeftEvent() {
        gamePlayerManager.newPlayer(name);

        Player removedPlayer = gamePlayerManager.removePlayer(name);

        verify(eventPublisher).publishPlayerLeftEvent(removedPlayer);
    }

    @Test
    public void removePlayer_whenPlayerWasNotFound_throwsException() {
        assertThrows(RuntimeException.class, () -> gamePlayerManager.removePlayer(name));
    }

    @Test
    public void getPlayer_whenPlayerWasFound_returnPlayer() {
        Player addedPlayer = gamePlayerManager.newPlayer(name);

        Player foundPlayer = gamePlayerManager.getPlayer(name);

        assertEquals(foundPlayer, addedPlayer);
    }

    @Test
    public void containsPlayer_whenPlayerWasFound_returnTrue() {
        gamePlayerManager.newPlayer(name);

        assertTrue(gamePlayerManager.containsPlayer(name));
    }

    @Test
    public void containsPlayer_whenPlayerWasNotFound_returnFalse() {
        assertFalse(gamePlayerManager.containsPlayer(name));
    }

    @Test
    public void playersCount_returnsNumberOfPlayers() {
        gamePlayerManager.newPlayer(name + 1);
        gamePlayerManager.newPlayer(name + 2);

        Collection<Player> players = gamePlayerManager.getPlayers();

        assertEquals(players.size(), gamePlayerManager.getPlayersCount());
    }

    @Test
    public void getPlayers_returnsCollectionWithAllAddedPlayers() {
        List<Player> addedPlayers = new ArrayList<>();
        addedPlayers.add(gamePlayerManager.newPlayer(name + 1));
        addedPlayers.add(gamePlayerManager.newPlayer(name + 2));

        Collection<Player> players = gamePlayerManager.getPlayers();

        assertTrue(addedPlayers.containsAll(players) &&
                players.containsAll(addedPlayers));
    }

    @Test
    public void getAvailableSymbols_whenThereAreNoPlayers_returnsAllPlayerSymbols() {
        List<PlayerSymbol> expectedSymbols = Arrays.asList(PlayerSymbol.values());

        List<PlayerSymbol> symbols = gamePlayerManager.getAvailableSymbols();

        assertTrue(expectedSymbols.containsAll(symbols) &&
                    symbols.containsAll(expectedSymbols));
    }
}