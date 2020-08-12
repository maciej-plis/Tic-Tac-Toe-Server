package matthias.tictactoe.web.game.services;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.*;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameStateEmitter {

    private final SimpMessagingTemplate template;
    private final TicTacToeGame game;

    @EventListener({PlayerJoinedEvent.class,
                    PlayerLeftEvent.class,
                    BoardChangedEvent.class,
                    GameStatusChangedEvent.class,
                    TurnChangedEvent.class})
    public void updateGameStatus() {
        template.convertAndSend("/topic/game", game.getGameData());
    }

//    @EventListener(PlayerJoinedEvent.class)
//    public void playerJoined(PlayerJoinedEvent playerJoinedEvent) {
//        Message message = MessageBuilder
//                .withPayload(new Object() {
//                    public String name = playerJoinedEvent.getPlayerName();
//                    public char symbol = playerJoinedEvent.getPlayerSymbol().getChar();
//                })
//                .setHeader("type", playerJoinedEvent.getName())
//                .build();
//
//        template.convertAndSend("/topic/game", message);
//        System.out.println("player joined " + playerJoinedEvent.getPlayerName());
//    }
//
//    @EventListener(PlayerLeftEvent.class)
//    public void playerLeft(PlayerLeftEvent playerLeftEvent) {
//        Message message = MessageBuilder
//                .withPayload(new Object() {
//                    public String name = playerLeftEvent.getPlayerName();
//                    public char symbol = playerLeftEvent.getPlayerSymbol().getChar();
//                })
//                .setHeader("type", playerLeftEvent.getName())
//                .build();
//
//        template.convertAndSend("/topic/game", message);
//        System.out.println("player left " + playerLeftEvent.getPlayerName());
//    }
//
//    @EventListener(BoardChangedEvent.class)
//    public void boardChanged(BoardChangedEvent boardChangedEvent) {
//        Message message = MessageBuilder
//                .withPayload(boardChangedEvent.getBoard().getCharBoard())
//                .setHeader("type", boardChangedEvent.getName())
//                .build();
//
//        template.convertAndSend("/topic/game", message);
//        System.out.println("board changed");
//    }
//
//    @EventListener(GameStatusChangedEvent.class)
//    public void gameStatusChanged(GameStatusChangedEvent gameStatusChangedEvent) {
//        Message message = MessageBuilder
//                .withPayload(gameStatusChangedEvent.getGameStatus().toString())
//                .setHeader("type", gameStatusChangedEvent.getName())
//                .build();
//
//        template.convertAndSend("/topic/game", message);
//        System.out.println("game status changed " + gameStatusChangedEvent.getGameStatus().toString());
//    }
//
//    @EventListener(TurnChangedEvent.class)
//    public void playerTurnChanged(TurnChangedEvent turnChangedEvent) {
//        Message message = MessageBuilder
//                .withPayload(turnChangedEvent.getSymbol().getChar())
//                .setHeader("type", turnChangedEvent.getName())
//                .build();
//
//        template.convertAndSend("/topic/game", message);
//        System.out.println("player turn changed, now moving: " + turnChangedEvent.getSymbol().toString());
//    }

}
