package matthias.tictactoe.tictactoe_game.tictactoe_game.command;

import java.util.UUID;

public record PlayerNotReadyCommand(
    UUID userId
) implements GameCommand<Void> {
}
