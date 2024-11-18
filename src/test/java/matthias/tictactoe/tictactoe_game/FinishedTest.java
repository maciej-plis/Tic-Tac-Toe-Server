package matthias.tictactoe.tictactoe_game;

import matthias.tictactoe.tictactoe_game.exception.IllegalStateActionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinishedTest {

    private final static PlayerId PLAYER_X_ID = new PlayerId(1L);
    private final static PlayerId PLAYER_O_ID = new PlayerId(2L);

    @Test
    void shouldAddPlayerRematch() {
        final var finishedState = finishedReadiness();

        final var result = finishedState.rematch(PLAYER_X_ID);

        assertTrue(result instanceof Finished);
        assertEquals(1, ((Finished) result).playersRematch.size());
        assertTrue(((Finished) result).playersRematch.contains(PLAYER_X_ID));
    }

    @Test
    void shouldConvertToInProgressWhenAllPlayersRematch() {
        final var finishedState = finishedReadiness();

        final var result = finishedState
            .rematch(PLAYER_X_ID)
            .rematch(PLAYER_O_ID);

        assertTrue(result instanceof InProgress);
        assertEquals(finishedState.players, result.players);
        assertEquals(new Board(3), result.board);
    }

    @Test
    void shouldIgnoreOtherPlayerRematch() {
        final var finishedState = finishedReadiness()
            .rematch(PLAYER_X_ID);

        final var result = finishedState.rematch(new PlayerId(99L));

        assertTrue(result instanceof Finished);
        assertEquals(1, ((Finished) result).playersRematch.size());
        assertTrue(((Finished) result).playersRematch.contains(PLAYER_X_ID));
    }

    @Test
    void shouldConvertToAwaitingPlayersWhenPlayerLeave() {
        final var finishedState = finishedReadiness();

        final var result = finishedState.leave(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_O_ID));
        assertEquals(finishedState.board, result.board);
    }

    @Test
    void shouldIgnoreOtherPlayerLeave() {
        final var finishedState = finishedReadiness();

        final var result = finishedState.leave(new PlayerId(99L));

        assertTrue(result instanceof Finished);
        assertEquals(result, finishedState);
        assertEquals(2, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_X_ID));
        assertTrue(result.players.containsValue(PLAYER_O_ID));
    }

    @Test
    void shouldThrowWhenTryingToJoin() {
        final var finishedState = finishedReadiness();

        assertThrows(IllegalStateActionException.class, () -> finishedState.join(PLAYER_X_ID));
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
        final var finishedState = finishedReadiness();

        assertThrows(IllegalStateActionException.class, () -> finishedState.move(PLAYER_X_ID, new Coordinates(0, 0)));
    }

    private Finished finishedReadiness() {
        return (Finished) new AwaitingPlayers()
            .join(PLAYER_X_ID)
            .join(PLAYER_O_ID)
            .ready(PLAYER_X_ID)
            .ready(PLAYER_O_ID)
            .move(PLAYER_X_ID, new Coordinates(0, 0))
            .move(PLAYER_O_ID, new Coordinates(1, 0))
            .move(PLAYER_X_ID, new Coordinates(0, 1))
            .move(PLAYER_O_ID, new Coordinates(1, 1))
            .move(PLAYER_X_ID, new Coordinates(0, 2));
    }
}
