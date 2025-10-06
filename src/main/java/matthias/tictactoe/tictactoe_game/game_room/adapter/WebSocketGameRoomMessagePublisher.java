package matthias.tictactoe.tictactoe_game.game_room.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
class WebSocketGameRoomMessagePublisher implements GameRoomMessagePublisher {

    private final SimpMessagingTemplate template;

    @Override
    public void publish(Serializable message) {
        template.convertAndSend("/topics/game-rooms", message);
    }

    @Override
    public void publish(UUID gameRoomId, Serializable message) {
        template.convertAndSend("/topics/game-rooms/" + gameRoomId, message);
    }
}
