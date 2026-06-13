package matthias.tictactoe.tictactoe_game.game_room.command;

import java.util.UUID;

public record UpdateGameRoomCommand(
    UUID gameRoomId,
    UUID userId,
    String name,
    boolean spectatingEnabled
) implements GameRoomCommand<Void> {
}
