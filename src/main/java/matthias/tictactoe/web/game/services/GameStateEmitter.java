package matthias.tictactoe.web.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.model.Symbol;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameStateEmitter {

    private final SimpMessagingTemplate template;

    @EventListener(PlayerJoinedEvent.class)
    public void playerJoined(PlayerJoinedEvent playerJoinedEvent) {

        Object message = new Object() {
            public GameEventType type = playerJoinedEvent.getType();
            public Player player = playerJoinedEvent.getPlayer();
        };

        template.convertAndSend("/topic/game", message);
    }

    @EventListener(PlayerLeftEvent.class)
    public void playerLeft(PlayerLeftEvent playerLeftEvent) {

        Object message = new Object() {
            public GameEventType type = playerLeftEvent.getType();
            public Player player = playerLeftEvent.getPlayer();
        };

        template.convertAndSend("/topic/game", message);
    }

    @EventListener(BoardChangedEvent.class)
    public void boardChanged(BoardChangedEvent boardChangedEvent) {

        Object message = new Object() {
            public GameEventType type = boardChangedEvent.getType();
            public Symbol[][] board = boardChangedEvent.getBoard().as2DimArray();
        };

        template.convertAndSend("/topic/game", message);
    }

    @EventListener(GameStateChangedEvent.class)
    public void gameStatusChanged(GameStateChangedEvent gameStateChangedEvent) {

        Object message = new Object() {
            public GameEventType type = gameStateChangedEvent.getType();
            public StateType state = gameStateChangedEvent.getStateType();
        };

        template.convertAndSend("/topic/game", message);
    }

    @EventListener(ActiveSymbolChangedEvent.class)
    public void activeSymbolChanged(ActiveSymbolChangedEvent activeSymbolChangedEvent) {

        Object message = new Object() {
            public GameEventType type = activeSymbolChangedEvent.getType();
            public PlayerSymbol activeSymbol = activeSymbolChangedEvent.getActiveSymbol().getSymbol();
        };

        template.convertAndSend("/topic/game", message);
    }

}
