package matthias.tictactoe.tictactoe_game.game_room.command;

import java.util.UUID;

public record UpdateGameRoomCommand(
    UUID userId,
    UUID gameRoomId,
    String name
) implements GameRoomCommand<Void> {
}
