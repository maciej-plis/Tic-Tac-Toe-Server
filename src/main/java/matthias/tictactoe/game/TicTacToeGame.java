package matthias.tictactoe.game;

import matthias.tictactoe.game.helpers.BoardChecker;
import matthias.tictactoe.game.model.*;
import matthias.tictactoe.game.model.dto.GameData;
import matthias.tictactoe.game.services.GameEventPublisher;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.web.authentication.model.User;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.stream.Collectors;

@Component
public class TicTacToeGame {

    private final GamePlayerManager players;
    private final GameBoard board;
    private final GameStatus status;
    private final GameTour tour;

    public TicTacToeGame(GameEventPublisher gameEventPublisher) {
        this.players = new GamePlayerManager(gameEventPublisher);
        this.board = new GameBoard(gameEventPublisher);
        this.status = new GameStatus(Status.NOT_ENOUGH_PLAYERS, gameEventPublisher);
        this.tour = new GameTour(Symbol.X, gameEventPublisher);
    }

    public void join(User player) {
        players.newPlayer(player);

        if(players.getPlayersCount() == 2) {
            status.setStatus(Status.IN_PROGRESS);
        }
    }

    public void leave(User player) {
        players.removePlayer(player);

        if(!status.equals(Status.NOT_ENOUGH_PLAYERS)) {
            status.setStatus(Status.NOT_ENOUGH_PLAYERS);
            board.clear();
        }
    }

    public void markSquare(User player, Point point) {
        if(!players.containsPlayer(player)) {
            throw new RuntimeException("Player is not in the room");
        }

        if(!status.equals(Status.IN_PROGRESS)) {
            throw new RuntimeException("Game is not in progress");
        }

        if(!players.getPlayer(tour.getTour()).equals(player)) {
            throw new RuntimeException("Please wait your turn");
        }

        if(!board.isEmpty(point)) {
            throw new RuntimeException("This square is already marked");
        }

        board.set(point, tour.getTour());

        if(BoardChecker.isWin(board)) {
            status.setStatus(Status.WIN);
        } else if(BoardChecker.isDraw(board)) {
            status.setStatus(Status.DRAW);
        } else {
            changeTour();
        }
    }

    public GameData getGameData() {
        GameData gameData = new GameData();
        gameData.setBoard(board.as2DimArray());
        gameData.setPlayers(players.getPlayers()
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                            e -> e.getKey(),
                                            e -> e.getValue().getUsername())));
        gameData.setStatus(status.getStatus());
        gameData.setTour(tour.getTour());
        return gameData;
    }

    private void changeTour() {
        if(this.tour.getTour() == Symbol.X) {
            this.tour.setTour(Symbol.O);
        } else {
            this.tour.setTour(Symbol.X);
        }
    }

}
