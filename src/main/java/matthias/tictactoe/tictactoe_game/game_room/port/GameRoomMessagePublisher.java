package matthias.tictactoe.tictactoe_game.game_room.port;

import java.io.Serializable;
import java.util.UUID;

public interface GameRoomMessagePublisher {
    void publish(Serializable message);
    void publish(UUID gameRoomId, Serializable message);
}
