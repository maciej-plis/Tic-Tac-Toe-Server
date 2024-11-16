package matthias.tictactoe.tictactoe_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwaitingPlayersTest {

    private final static PlayerId PLAYER_X_ID = new PlayerId(1L);
    private final static PlayerId PLAYER_O_ID = new PlayerId(2L);

    @Test
    void shouldAddPlayer() {
        final var awaitingPlayersState = new AwaitingPlayers();

        final var result = awaitingPlayersState.join(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_X_ID));
    }

    @Test
    void shouldAddPlayerOnlyOnce() {
        final var awaitingPlayersState = new AwaitingPlayers()
            .join(PLAYER_X_ID);

        final var result = awaitingPlayersState.join(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_X_ID));
    }

    @Test
    void shouldConvertToAwaitingPlayerReadinessAfterSecondPlayerJoined() {
        final var awaitingPlayersState = new AwaitingPlayers()
            .join(PLAYER_X_ID);

        final var result = awaitingPlayersState.join(PLAYER_O_ID);

        assertTrue(result instanceof AwaitingPlayerReadiness);
        assertEquals(awaitingPlayersState.players, result.players);
        assertEquals(awaitingPlayersState.board, result.board);
    }

    @Test
    void shouldRemovePlayerOne() {
        final var awaitingPlayersState = new AwaitingPlayers()
            .join(PLAYER_X_ID)
            .join(PLAYER_O_ID);

        final var result = awaitingPlayersState.leave(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_O_ID));
    }

    @Test
    void shouldRemoveBothPlayers() {
        final var awaitingPlayersState = new AwaitingPlayers()
            .join(PLAYER_X_ID)
            .join(PLAYER_O_ID);

        final var result = awaitingPlayersState
            .leave(PLAYER_X_ID)
            .leave(PLAYER_O_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertTrue(result.players.isEmpty());
    }

    @Test
    void shouldIgnoreOtherPlayerLeave() {
        final var awaitingPlayerReadinessState = new AwaitingPlayers();

        final var result = awaitingPlayerReadinessState.leave(new PlayerId(99L));

        assertTrue(result instanceof AwaitingPlayers);
        assertTrue(result.players.isEmpty());
    }

    @Test
    void shouldChangePlayerSymbolToX() {
        final var awaitingPlayerReadinessState = new AwaitingPlayers()
            .join(PLAYER_X_ID);

        final var result = awaitingPlayerReadinessState.changeSymbol(PLAYER_X_ID, Symbol.X);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsKey(Symbol.X));
        assertTrue(result.players.containsValue(PLAYER_X_ID));
    }

    @Test
    void shouldChangePlayerSymbolToO() {
        final var awaitingPlayerReadinessState = new AwaitingPlayers()
            .join(PLAYER_X_ID);

        final var result = awaitingPlayerReadinessState.changeSymbol(PLAYER_X_ID, Symbol.O);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsKey(Symbol.O));
        assertTrue(result.players.containsValue(PLAYER_X_ID));
    }

    @Test
    void shouldThrowWhenTryingToSetReady() {
        final var awaitingPlayersState = new AwaitingPlayers();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayersState.ready(PLAYER_X_ID));
    }

    @Test
    void shouldThrowWhenTryingToSetNotReady() {
        final var awaitingPlayersState = new AwaitingPlayers();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayersState.notReady(PLAYER_X_ID));
    }

    @Test
    void shouldThrowWhenTryingToMove() {
        final var awaitingPlayersState = new AwaitingPlayers();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayersState.move(PLAYER_X_ID, new Coordinates(0, 0)));
    }

    @Test
    void shouldThrowWhenTryingToSetRematch() {
        final var awaitingPlayersState = new AwaitingPlayers();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayersState.rematch(PLAYER_X_ID));
    }
}
