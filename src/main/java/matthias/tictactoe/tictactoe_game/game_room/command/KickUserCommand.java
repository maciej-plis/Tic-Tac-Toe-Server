package matthias.tictactoe.tictactoe_game.game_room.command;

import java.util.UUID;

public record KickUserCommand(
    UUID gameRoomId,
    UUID actorId,
    UUID userId
) implements GameRoomCommand<Void> {
}

