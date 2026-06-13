package matthias.tictactoe.tictactoe_game.game_room.command;

import java.util.UUID;

public record DeleteGameRoomCommand(
    UUID userId,
    UUID gameRoomId
) implements GameRoomCommand<Void> {
}
