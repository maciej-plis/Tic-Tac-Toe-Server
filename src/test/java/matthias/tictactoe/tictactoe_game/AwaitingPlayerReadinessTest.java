package matthias.tictactoe.tictactoe_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AwaitingPlayerReadinessTest {

    private final static PlayerId PLAYER_X_ID = new PlayerId(1L);
    private final static PlayerId PLAYER_O_ID = new PlayerId(2L);

    @Test
    void shouldAddPlayerReady() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        final var result = awaitingPlayerReadinessState.ready(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayerReadiness);
        assertEquals(1, ((AwaitingPlayerReadiness) result).playersReady.size());
        assertTrue(((AwaitingPlayerReadiness) result).playersReady.contains(PLAYER_X_ID));
    }

    @Test
    void shouldConvertToInProgressWhenAllPlayersReady() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        final var result = awaitingPlayerReadinessState
            .ready(PLAYER_X_ID)
            .ready(PLAYER_O_ID);

        assertTrue(result instanceof InProgress);
        assertEquals(awaitingPlayerReadinessState.players, result.players);
        assertEquals(awaitingPlayerReadinessState.board, result.board);
    }

    @Test
    void shouldIgnoreOtherPlayerReady() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness()
            .ready(PLAYER_X_ID);

        final var result = awaitingPlayerReadinessState.ready(new PlayerId(99L));

        assertTrue(result instanceof AwaitingPlayerReadiness);
        assertEquals(1, ((AwaitingPlayerReadiness) result).playersReady.size());
        assertTrue(((AwaitingPlayerReadiness) result).playersReady.contains(PLAYER_X_ID));
    }

    @Test
    void shouldRemovePlayerReady() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness()
            .ready(PLAYER_X_ID);

        final var result = awaitingPlayerReadinessState.notReady(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayerReadiness);
        assertTrue(((AwaitingPlayerReadiness) result).playersReady.isEmpty());
    }

    @Test
    void shouldIgnoreOtherPlayerNotReady() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness()
            .ready(PLAYER_X_ID);

        final var result = awaitingPlayerReadinessState.notReady(new PlayerId(99L));

        assertTrue(result instanceof AwaitingPlayerReadiness);
        assertEquals(1, ((AwaitingPlayerReadiness) result).playersReady.size());
        assertTrue(((AwaitingPlayerReadiness) result).playersReady.contains(PLAYER_X_ID));
    }

    @Test
    void shouldConvertToAwaitingPlayersWhenPlayerLeave() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        final var result = awaitingPlayerReadinessState.leave(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_O_ID));
        assertEquals(awaitingPlayerReadinessState.board, result.board);
    }

    @Test
    void shouldIgnoreOtherPlayerLeave() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        final var result = awaitingPlayerReadinessState.leave(new PlayerId(99L));

        assertTrue(result instanceof AwaitingPlayerReadiness);
        assertEquals(result, awaitingPlayerReadinessState);
        assertEquals(2, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_X_ID));
        assertTrue(result.players.containsValue(PLAYER_O_ID));
    }

    @Test
    void shouldThrowWhenTryingToJoin() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayerReadinessState.join(PLAYER_X_ID));
    }

    @Test
    void shouldThrowWhenTryingToMove() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayerReadinessState.move(PLAYER_X_ID, new Coordinates(0, 0)));
    }

    @Test
    void shouldThrowWhenTryingToSetRematch() {
        final var awaitingPlayerReadinessState = awaitingPlayerReadiness();

        assertThrows(IllegalStateActionException.class, () -> awaitingPlayerReadinessState.rematch(PLAYER_X_ID));
    }

    private AwaitingPlayerReadiness awaitingPlayerReadiness() {
        return (AwaitingPlayerReadiness) new AwaitingPlayers()
            .join(PLAYER_X_ID)
            .join(PLAYER_O_ID);
    }
}
