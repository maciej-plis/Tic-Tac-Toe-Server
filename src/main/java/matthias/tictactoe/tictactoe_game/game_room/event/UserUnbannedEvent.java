package matthias.tictactoe.tictactoe_game.game_room.event;

import java.io.Serializable;
import java.util.UUID;

public record UserUnbannedEvent(
    UUID gameRoomId,
    UUID userId
) implements Serializable {
}

