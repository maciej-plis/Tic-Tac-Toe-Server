package matthias.tictactoe.game.services;

import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service("GameEventPublisher")
public class GameEventPublisher {

    private static ApplicationEventPublisher publisher;

    public GameEventPublisher(ApplicationEventPublisher eventPublisher) {
        publisher = eventPublisher;
    }

    public static void publishPlayerJoinedEvent(Player player) {
        publisher.publishEvent(new PlayerJoinedEvent(player) );
    }

    public static void publishPlayerLeftEvent(Player player) {
        publisher.publishEvent(new PlayerLeftEvent(player) );
    }

    public static void publishGameStatusChangedEvent(Status status) {
        publisher.publishEvent(new GameStatusChangedEvent(status));
    }

    public static void publishBoardChangedEvent(GameBoard board) {
        publisher.publishEvent(new BoardChangedEvent(board));
    }

    public static void publishActiveSymbolChangedEvent(ActiveSymbol active) {
        publisher.publishEvent(new ActiveSymbolChangedEvent(active));
    }
}
