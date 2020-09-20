package matthias.tictactoe.web.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameEventWsPublisher implements GameEventsFollower{
    private final SimpMessagingTemplate template;

    public void trackGameEvents(TicTacToeGame game) {
        game.followGameEvents(this);
        game.getName();
    }

    @Override
    public void eventOccurred(String gameId, GameEvent event) {
        template.convertAndSend("/topic/games/" + gameId, event);
    }
}
