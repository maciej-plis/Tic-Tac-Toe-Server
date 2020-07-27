package matthias.tictactoe.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.Board;
import matthias.tictactoe.game.model.GameStatus;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.Symbol;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameEventPublisher {

    private final ApplicationEventPublisher publisher;


    public void publishPlayerJoinedEvent(Symbol symbol, Player player) {

        String playerName = player.getName();
        publisher.publishEvent(new PlayerJoinedEvent(symbol, playerName) );
    }

    public void publishPlayerLeftEvent(Symbol symbol, Player player) {

        String playerName = player.getName();
        publisher.publishEvent(new PlayerLeftEvent(symbol, playerName) );
    }

    public void publishGameStatusChangedEvent(GameStatus status) {
        publisher.publishEvent(new GameStatusChangedEvent(status));
    }

    public void publishBoardChangedEvent(Board board) {
        publisher.publishEvent(new BoardChangedEvent(board));
    }

    public void publishTurnChangedEvent(Symbol sign) {
        publisher.publishEvent(new TurnChangedEvent(sign));
    }
}
