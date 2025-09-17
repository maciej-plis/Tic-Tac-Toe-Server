package matthias.tictactoe.tictactoe_game.tictactoe_game.command;

import java.util.UUID;

public record PlayerMoveCommand(
    UUID userId,
    int row,
    int col
) implements GameCommand<Void> {
}
