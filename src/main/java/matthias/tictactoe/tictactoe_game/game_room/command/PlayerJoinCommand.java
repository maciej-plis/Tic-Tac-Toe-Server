package matthias.tictactoe.tictactoe_game.game_room.command;

import java.util.UUID;

public record PlayerJoinCommand(
    UUID gameRoomId,
    UUID userId
) implements GameRoomCommand<Void> {
}
