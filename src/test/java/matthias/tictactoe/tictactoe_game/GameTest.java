package matthias.tictactoe.tictactoe_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    private final static PlayerId PLAYER_X_ID = new PlayerId(1L);
    private final static PlayerId PLAYER_O_ID = new PlayerId(2L);

    @Test
    void shouldThrowWhenReadyIsAttemptedAndPlayerNotJoined() {
        final var game = new Game();

        // expect
        assertThrows(IllegalStateActionException.class, () -> game.ready(PLAYER_X_ID));
    }

    @Test
    void shouldThrowWhenReadyIsAttemptedAndPlayerNotJoined() {
        final var game = new Game();

        // expect
        assertThrows(IllegalStateActionException.class, () -> game.ready(PLAYER_X_ID));
    }
}
