package matthias.tictactoe.game;

import matthias.tictactoe.game.helpers.BoardChecker;
import matthias.tictactoe.game.model.*;
import matthias.tictactoe.game.model.dto.GameData;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.web.authentication.model.User;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.stream.Collectors;

@Component
@DependsOn("GameEventPublisher")
public class TicTacToeGame {

    private final GamePlayerManager players;
    private final GameBoard board;
    private final GameStatus status;
    private final GameTour tour;

    /**
     * constructs TicTacToeGame with defaults:<br>
     * players - empty<br>
     * board - empty<br>
     * status - NOT_ENOUGH_PLAYERS<br>
     * active player - X
     */
    public TicTacToeGame() {
        this.players = new GamePlayerManager();
        this.board = new GameBoard();
        this.status = new GameStatus(Status.NOT_ENOUGH_PLAYERS);
        this.tour = new GameTour(Symbol.X);
    }

    /**
     * adds given player to the game.
     *
     * when number of players in game is
     * equal to 2 game status changes for IN_PROGRESS
     *
     * @param player joining the game
     */
    public void join(User player) {
        players.newPlayer(player);

        if(players.getPlayersCount() == 2) {
            status.setStatus(Status.IN_PROGRESS);
        }
    }

    /**
     * removes given player from the game.
     *
     * when number of players in game drops down
     * to 1 game status changes for NOT_ENOUGH_PLAYERS
     * and board is cleared
     *
     * @param player leaving the game
     */
    public void leave(User player) {
        players.removePlayer(player);

        if(!status.hasStatus(Status.NOT_ENOUGH_PLAYERS)) {
            status.setStatus(Status.NOT_ENOUGH_PLAYERS);
            board.clear();
        }
    }

    /**
     * Marks given square of game board with player symbol.
     *
     * @param player player trying to mark square
     * @param point coordinates of board square
     */
    public void markSquare(User player, Point point) {
        if(!players.containsPlayer(player)) {
            throw new RuntimeException("Player is not in the room");
        }

        if(!status.hasStatus(Status.IN_PROGRESS)) {
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

    /**
     * @return Game data containing:<br>
     * board - 2dim Symbol array<br>
     * players - map (Symbol = PlayerName)<br>
     * status - current game status<br>
     * tour - current tour symbol
     */
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

    /**
     * Changes tour of the game.<br>
     * x -> O<br>
     * O -> X
     */
    private void changeTour() {
        if(this.tour.getTour() == Symbol.X) {
            this.tour.setTour(Symbol.O);
        } else {
            this.tour.setTour(Symbol.X);
        }
    }

}
