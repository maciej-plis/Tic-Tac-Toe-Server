package matthias.tictactoe.game.services;

import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.ActivePlayer;
import matthias.tictactoe.game.model.GameBoard;
import matthias.tictactoe.game.model.Status;
import matthias.tictactoe.game.model.Symbol;
import matthias.tictactoe.web.authentication.model.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service("GameEventPublisher")
public class GameEventPublisher {

    private static ApplicationEventPublisher publisher;

    public GameEventPublisher(ApplicationEventPublisher eventPublisher) {
        publisher = eventPublisher;
    }

    public static void publishPlayerJoinedEvent(Symbol symbol, User player) {
        String playerName = player.getUsername();
        publisher.publishEvent(new PlayerJoinedEvent(symbol, playerName) );
    }

    public static void publishPlayerLeftEvent(Symbol symbol, User player) {
        String playerName = player.getUsername();
        publisher.publishEvent(new PlayerLeftEvent(symbol, playerName) );
    }

    public static void publishGameStatusChangedEvent(Status status) {
        publisher.publishEvent(new GameStatusChangedEvent(status));
    }

    public static void publishBoardChangedEvent(GameBoard board) {
        publisher.publishEvent(new BoardChangedEvent(board));
    }

    public static void publishActivePlayerChangedEvent(ActivePlayer active) {
        publisher.publishEvent(new ActivePlayerChangedEvent(active));
    }
}
