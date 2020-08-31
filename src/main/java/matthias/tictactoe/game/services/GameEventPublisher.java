package matthias.tictactoe.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishPlayerJoinedEvent(Player player) {
        publisher.publishEvent(new PlayerJoinedEvent(player) );
    }

    public void publishPlayerLeftEvent(Player player) {
        publisher.publishEvent(new PlayerLeftEvent(player) );
    }

    public void publishGameStatusChangedEvent(StateType stateType) {
        publisher.publishEvent(new GameStateChangedEvent(stateType));
    }

    public void publishBoardChangedEvent(GameBoard board) {
        publisher.publishEvent(new BoardChangedEvent(board));
    }

    public void publishActiveSymbolChangedEvent(ActiveSymbol active) {
        publisher.publishEvent(new ActiveSymbolChangedEvent(active));
    }
}
