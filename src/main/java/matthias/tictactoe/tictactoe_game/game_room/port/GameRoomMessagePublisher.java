package matthias.tictactoe.tictactoe_game.game_room.port;

import java.io.Serializable;
import java.util.UUID;

@FunctionalInterface
public interface GameRoomMessagePublisher {
    void publish(UUID gameRoomId, Serializable message);
}
