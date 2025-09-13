package matthias.tictactoe.tictactoe_game.command;

import java.util.UUID;

public record PlayerMoveCommand(
    UUID gameRoomId,
    UUID userId,
    int row,
    int col
) implements GameRoomCommand {
}
