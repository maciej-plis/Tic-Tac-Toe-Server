package matthias.tictactoe.game;

import matthias.tictactoe.game.helpers.BoardChecker;
import matthias.tictactoe.game.model.Board;
import matthias.tictactoe.game.model.GameStatus;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.Symbol;
import matthias.tictactoe.game.services.GameEventPublisher;
import matthias.tictactoe.game.services.GamePlayerManager;
import org.springframework.stereotype.Component;

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

    public void join(Player player) {

        players.newPlayer(player);
        gameEventPublisher.publishPlayerJoinedEvent(players.getPlayerSymbol(player), player);

        if(players.getPlayersCount() == 2) {
            status = GameStatus.IN_PROGRESS;
            gameEventPublisher.publishGameStatusChangedEvent(status);
        }
    }

    public void leave(Player player) {

        players.removePlayer(player);
        gameEventPublisher.publishPlayerLeftEvent(players.getPlayerSymbol(player), player);

        if(status == GameStatus.IN_PROGRESS) {
            status = GameStatus.NOT_ENOUGH_PLAYERS;
            gameEventPublisher.publishGameStatusChangedEvent(status);

            board.clear();
            gameEventPublisher.publishBoardChangedEvent(board);
        }
    }

    public void markSquare(Player player, int x, int y) {

        if(!players.containsPlayer(player)) {
            throw new RuntimeException("Player is not in the room");
        }

        if(status != GameStatus.IN_PROGRESS) {
            throw new RuntimeException("Game is not in progress");
        }

        if(!players.getPlayer(currentSymbol).equals(player)) {
            throw new RuntimeException("Please wait your turn");
        }

        if(!board.isEmpty(x, y)) {
            throw new RuntimeException("This square is already marked");
        }

        board.set(x, y, currentSymbol);
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

    private void changeTour() {
        if(this.currentSymbol == Symbol.X) {
            this.currentSymbol = Symbol.O;
        } else {
            this.currentSymbol = Symbol.X;
        }
    }

}
