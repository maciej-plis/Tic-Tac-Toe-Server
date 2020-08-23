package matthias.tictactoe.game;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.helpers.BoardChecker;
import matthias.tictactoe.game.model.*;
import matthias.tictactoe.game.model.dto.GameData;
import matthias.tictactoe.game.services.GamePlayerManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.awt.*;

@RequiredArgsConstructor
@Component
@DependsOn("GameEventPublisher")
public class TicTacToeGame {
    private final GamePlayerManager players;
    private final GameBoard board;
    private final GameStatus status;
    private final ActivePlayer active;

    /**
     * adds given player to the game.
     *
     * when number of players in game is
     * equal to 2 game status changes for IN_PROGRESS
     *
     * @param player joining the game
     */
    public void join(String name) {
        players.newPlayer(name);

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
    public void leave(String name) {
        players.removePlayer(name);

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
    public void markSquare(String name, Point point) {
        Player player = players.getPlayer(name);

        if(player == null) {
            throw new RuntimeException("Player is not in the room");
        }

        if(!status.hasStatus(Status.IN_PROGRESS)) {
            throw new RuntimeException("Game is not in progress");
        }

        if(!active.is(player.getSymbol())) {
            throw new RuntimeException("Please wait your turn");
        }

        if(!board.isEmpty(point)) {
            throw new RuntimeException("This square is already marked");
        }

        board.set(point, active.getSymbol().symbol());

        if(BoardChecker.isWin(board)) {
            status.setStatus(Status.WIN);
        } else if(BoardChecker.isDraw(board)) {
            status.setStatus(Status.DRAW);
        } else {
            changeActivePlayer();
        }
    }

    /**
     * @return Game data containing:<br>
     * board - 2dim Symbol array<br>
     * players - map (Symbol = PlayerName)<br>
     * status - current game status<br>
     * activePlayer - currently active player symbol
     */
    public GameData getGameData() {
        GameData gameData = new GameData();
        gameData.setBoard(board.as2DimArray());
        gameData.setPlayers(players.getPlayers());
        gameData.setStatus(status.getStatus());
        gameData.setActivePlayer(active.getSymbol());
        return gameData;
    }

    /**
     * Changes currently active player in game.<br>
     * x -> O<br>
     * O -> X
     */
    private void changeActivePlayer() {
        if(this.active.getSymbol() == PlayerSymbol.X) {
            this.active.setSymbol(PlayerSymbol.O);
        } else {
            this.active.setSymbol(PlayerSymbol.X);
        }
    }

}
