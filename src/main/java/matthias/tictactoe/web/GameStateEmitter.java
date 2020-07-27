package matthias.tictactoe.web;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.events.*;
import matthias.tictactoe.game.model.Symbol;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GameStateEmitter {

    private final SimpMessagingTemplate template;

    @EventListener(PlayerJoinedEvent.class)
    public void playerJoined(PlayerJoinedEvent playerJoinedEvent) {
        Message message = MessageBuilder
                .withPayload(new Object() {
                    public String name = playerJoinedEvent.getPlayerName();
                    public char symbol = playerJoinedEvent.getPlayerSymbol().getChar();
                })
                .setHeader("type", playerJoinedEvent.getName())
                .build();

        template.convertAndSend("/topic/game", message);
        System.out.println("player joined " + playerJoinedEvent.getPlayerName());
    }

    @EventListener(PlayerLeftEvent.class)
    public void playerLeft(PlayerLeftEvent playerLeftEvent) {
        Message message = MessageBuilder
                .withPayload(new Object() {
                    public String name = playerLeftEvent.getPlayerName();
                    public char symbol = playerLeftEvent.getPlayerSymbol().getChar();
                })
                .setHeader("type", playerLeftEvent.getName())
                .build();

        template.convertAndSend("/topic/game", message);
        System.out.println("player left " + playerLeftEvent.getPlayerName());
    }

    @EventListener(BoardChangedEvent.class)
    public void boardChanged(BoardChangedEvent boardChangedEvent) {
        Message message = MessageBuilder
                .withPayload(boardChangedEvent.getBoard().getBoard())
                .setHeader("type", boardChangedEvent.getName())
                .build();

        template.convertAndSend("/topic/game", message);
        System.out.println("board changed");
    }

    @EventListener(GameStatusChangedEvent.class)
    public void gameStatusChanged(GameStatusChangedEvent gameStatusChangedEvent) {
        Message message = MessageBuilder
                .withPayload(gameStatusChangedEvent.getGameStatus().toString())
                .setHeader("type", gameStatusChangedEvent.getName())
                .build();

        template.convertAndSend("/topic/game", message);
        System.out.println("game status changed " + gameStatusChangedEvent.getGameStatus().toString());
    }

    @EventListener(TurnChangedEvent.class)
    public void playerTurnChanged(TurnChangedEvent turnChangedEvent) {
        Message message = MessageBuilder
                .withPayload(turnChangedEvent.getSymbol().getChar())
                .setHeader("type", turnChangedEvent.getName())
                .build();

        template.convertAndSend("/topic/game", message);
        System.out.println("player turn changed, now moving: " + turnChangedEvent.getSymbol().toString());
    }

}
