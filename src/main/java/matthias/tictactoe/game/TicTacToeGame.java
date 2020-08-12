package matthias.tictactoe.game;

import matthias.tictactoe.game.helpers.BoardChecker;
import matthias.tictactoe.game.model.Board;
import matthias.tictactoe.game.model.GameStatus;
import matthias.tictactoe.game.model.Symbol;
import matthias.tictactoe.game.model.dto.GameData;
import matthias.tictactoe.game.services.GameEventPublisher;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.web.authentication.model.User;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.stream.Collectors;

@Component
public class TicTacToeGame {

    private final GameEventPublisher gameEventPublisher;

    private final Board board;

    private GameStatus status;
    private Symbol currentSymbol;

    private final GamePlayerManager players;

    public TicTacToeGame(GameEventPublisher gameEventPublisher) {
        this.gameEventPublisher = gameEventPublisher;

        this.players = new GamePlayerManager();
        this.board = new Board();

        this.status = GameStatus.NOT_ENOUGH_PLAYERS;
        this.currentSymbol = Symbol.X;
    }

    public void join(User player) {

        players.newPlayer(player);
        gameEventPublisher.publishPlayerJoinedEvent(players.getPlayerSymbol(player), player);

        if(players.getPlayersCount() == 2) {
            status = GameStatus.IN_PROGRESS;
            gameEventPublisher.publishGameStatusChangedEvent(status);
        }
    }

    public void leave(User player) {

        Symbol symbol = players.removePlayer(player);
        gameEventPublisher.publishPlayerLeftEvent(symbol, player);

        if(status != GameStatus.NOT_ENOUGH_PLAYERS) {
            status = GameStatus.NOT_ENOUGH_PLAYERS;
            gameEventPublisher.publishGameStatusChangedEvent(status);

            board.clear();
            gameEventPublisher.publishBoardChangedEvent(board);
        }
    }

    public void markSquare(User player, Point point) {

        if(!players.containsPlayer(player)) {
            throw new RuntimeException("Player is not in the room");
        }

        if(status != GameStatus.IN_PROGRESS) {
            throw new RuntimeException("Game is not in progress");
        }

        if(!players.getPlayer(currentSymbol).equals(player)) {
            throw new RuntimeException("Please wait your turn");
        }

        if(!board.isEmpty(point)) {
            throw new RuntimeException("This square is already marked");
        }

        board.set(point, currentSymbol);
        gameEventPublisher.publishBoardChangedEvent(board);

        if(BoardChecker.win(board)) {
            status = GameStatus.WIN;
            gameEventPublisher.publishGameStatusChangedEvent(status);
        } else if(BoardChecker.draw(board)) {
            status = GameStatus.DRAW;
            gameEventPublisher.publishGameStatusChangedEvent(status);
        } else {
            changeTour();
            gameEventPublisher.publishTurnChangedEvent(currentSymbol);
        }
    }

    public GameData getGameData() {
        GameData gameData = new GameData();

        gameData.setBoard(board.as2DimCharArray());

        gameData.setPlayers(players.getPlayers()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                            e -> e.getKey().getChar(),
                                            e -> e.getValue().getUsername())));
        gameData.setStatus(status);
        gameData.setTour(currentSymbol.getChar());

        return gameData;
    }

    private void changeTour() {
        if(this.currentSymbol == Symbol.X) {
            this.currentSymbol = Symbol.O;
        } else {
            this.currentSymbol = Symbol.X;
        }
    }

}
