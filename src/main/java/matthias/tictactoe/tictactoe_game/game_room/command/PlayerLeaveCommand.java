package matthias.tictactoe.tictactoe_game.game_room.command;

import java.util.UUID;

public record PlayerLeaveCommand(
    UUID userId
) implements GameRoomCommand<Void> {
}
