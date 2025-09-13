package matthias.tictactoe.tictactoe_game.command;

import java.util.UUID;

public record SpectatorLeaveCommand(
    UUID gameRoomId,
    UUID userId
) implements GameRoomCommand {
}
