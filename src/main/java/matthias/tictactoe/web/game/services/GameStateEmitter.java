package matthias.tictactoe.web.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.events.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameStateEmitter {
    private final SimpMessagingTemplate template;

    public void emitGameEvent(GameEvent gameEvent) {
        template.convertAndSend("/topic/game", gameEvent);
    }
}
