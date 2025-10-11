package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Component
class GameRoomManager {

    private final Map<UUID, GameRoom> gameRooms = new HashMap<>();

    void save(GameRoom gameRoom) {
        gameRooms.put(gameRoom.getId(), gameRoom);
    }

    GameRoom getById(UUID gameRoomId) {
        return findById(gameRoomId).orElseThrow(() -> new GameRoomNotFoundException("Game room with id '" + gameRoomId + "' not found"));
    }

    Optional<GameRoom> findById(UUID gameRoomId) {
        return ofNullable(gameRooms.get(gameRoomId));
    }

    void deleteById(UUID gameRoomId) {
        gameRooms.remove(gameRoomId);
    }
}
