package matthias.tictactoe.tictactoe_game;

import org.junit.jupiter.api.Test;

import static matthias.tictactoe.tictactoe_game.Board.BoardResult.WIN_X;
import static org.junit.jupiter.api.Assertions.*;

class InProgressTest {

    private final static PlayerId PLAYER_X_ID = new PlayerId(1L);
    private final static PlayerId PLAYER_O_ID = new PlayerId(2L);

    @Test
    void shouldMakePlayerMove() {
        final var inProgressState = inProgress();

        final var result = inProgressState
            .move(PLAYER_X_ID, new Coordinates(0, 0));

        assertTrue(result instanceof InProgress);
        assertEquals(1, result.board.getMoveCount());
        assertEquals(Symbol.O, result.symbolTurn);
    }

    @Test
    void shouldMakePlayerMovesInTurns() {
        final var inProgressState = inProgress();

        final var result = inProgressState
            .move(PLAYER_X_ID, new Coordinates(0, 0))
            .move(PLAYER_O_ID, new Coordinates(0, 1))
            .move(PLAYER_X_ID, new Coordinates(0, 2))
            .move(PLAYER_O_ID, new Coordinates(1, 0))
            .move(PLAYER_X_ID, new Coordinates(1, 1))
            .move(PLAYER_O_ID, new Coordinates(1, 2));

        assertTrue(result instanceof InProgress);
        assertEquals(6, result.board.getMoveCount());
        assertEquals(Symbol.X, result.symbolTurn);
    }

    @Test
    void shouldIgnoreWhenPlayerTriesToMoveMultipleTimes() {
        final var inProgressState = inProgress();

        final var result = inProgressState
            .move(PLAYER_X_ID, new Coordinates(0, 0))
            .move(PLAYER_X_ID, new Coordinates(1, 0))
            .move(PLAYER_X_ID, new Coordinates(2, 0));

        assertTrue(result instanceof InProgress);
        assertEquals(1, result.board.getMoveCount());
        assertEquals(Symbol.O, result.symbolTurn);
    }

    @Test
    void shouldIgnoreIfNotPlayerTurn() {
        final var inProgressState = inProgress();

        final var result = inProgressState.move(PLAYER_O_ID, new Coordinates(0, 0));

        assertTrue(result instanceof InProgress);
        assertEquals(0, result.board.getMoveCount());
        assertEquals(Symbol.X, result.symbolTurn);
    }

    @Test
    void shouldConvertToFinishedWhenGameHasResult() {
        final var inProgressState = inProgress();

        final var result = inProgressState
            .move(PLAYER_X_ID, new Coordinates(0, 0))
            .move(PLAYER_O_ID, new Coordinates(1, 0))
            .move(PLAYER_X_ID, new Coordinates(0, 1))
            .move(PLAYER_O_ID, new Coordinates(1, 1))
            .move(PLAYER_X_ID, new Coordinates(0, 2));

        assertTrue(result instanceof Finished);
        assertTrue(result.board.hasResult());
        assertEquals(WIN_X, result.board.getResult());
        assertEquals(result.board.getMoveCount(), 5);
        assertEquals(result.symbolTurn, Symbol.X);
    }

    @Test
    void shouldConvertToAwaitingPlayersWhenPlayerLeave() {
        final var inProgressState = inProgress();

        final var result = inProgressState.leave(PLAYER_X_ID);

        assertTrue(result instanceof AwaitingPlayers);
        assertEquals(1, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_O_ID));
        assertEquals(inProgressState.board, result.board);
    }

    @Test
    void shouldIgnoreOtherPlayerLeave() {
        final var inProgressState = inProgress();

        final var result = inProgressState.leave(new PlayerId(99L));

        assertTrue(result instanceof InProgress);
        assertEquals(result, inProgressState);
        assertEquals(2, result.players.size());
        assertTrue(result.players.containsValue(PLAYER_X_ID));
        assertTrue(result.players.containsValue(PLAYER_O_ID));
    }

    @Test
    void shouldThrowWhenTryingToJoin() {
        final var inProgressState = inProgress();

        assertThrows(IllegalStateActionException.class, () -> inProgressState.join(PLAYER_X_ID));
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
    void shouldThrowWhenTryingToSetRematch() {
        final var inProgressState = inProgress();

        assertThrows(IllegalStateActionException.class, () -> inProgressState.rematch(PLAYER_X_ID));
    }

    private InProgress inProgress() {
        return (InProgress) new AwaitingPlayers()
            .join(PLAYER_X_ID)
            .join(PLAYER_O_ID)
            .ready(PLAYER_X_ID)
            .ready(PLAYER_O_ID);
    }
}
