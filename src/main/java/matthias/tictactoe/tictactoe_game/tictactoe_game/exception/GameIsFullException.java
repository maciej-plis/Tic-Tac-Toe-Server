package matthias.tictactoe.tictactoe_game.tictactoe_game.exception;

public class GameIsFullException extends RuntimeException {

    public GameIsFullException(String message) {
        super(message);
    }
}
