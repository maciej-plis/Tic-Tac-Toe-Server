package matthias.tictactoe.tictactoe_game.tictactoe_game.command;

import java.util.UUID;

public record PlayerRequestRematchCommand(
    UUID userId
) implements GameCommand<Void> {
}
