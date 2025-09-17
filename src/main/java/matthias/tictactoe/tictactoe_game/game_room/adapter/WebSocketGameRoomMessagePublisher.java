package matthias.tictactoe.tictactoe_game.game_room.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
class WebSocketGameRoomMessagePublisher implements GameRoomMessagePublisher {

    @Override
    public void publish(UUID gameRoomId, Serializable message) {

    }
}
