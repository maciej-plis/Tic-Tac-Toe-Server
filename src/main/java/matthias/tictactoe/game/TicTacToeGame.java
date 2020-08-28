package matthias.tictactoe.game;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.services.GameSymbolManager;
import matthias.tictactoe.game.utils.BoardChecker;
import matthias.tictactoe.game.model.*;
import matthias.tictactoe.game.model.dto.GameData;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.game.utils.PlayerUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TicTacToeGame {
    private final GameSymbolManager symbols;
    private final GamePlayerManager players;
    private final GameBoard board;
    private final GameStatus status;
    private final ActiveSymbol active;

    /**
     * Creates new player and adds him to the game.
     *
     * When number of players in game is
     * equal to 2 game status changes for IN_PROGRESS
     *
     * @param name of player to be added
     */
    public void join(String name) {
        Player player = createPlayer(name);
        players.addPlayer(player);

        if(players.getPlayersCount() == 2) {
            status.setStatus(Status.IN_PROGRESS);
        }
    }

    /**
     * Removes player from the game.
     *
     * When game status is different from NOT_ENOUGH_PLAYERS
     * then game status changes to it and board is cleared
     *
     * @param name of player to be removed
     */
    public void leave(String name) {
        Player removedPlayer = players.removePlayer(name);
        symbols.returnSymbol(removedPlayer.getSymbol());

        if(!status.hasStatus(Status.NOT_ENOUGH_PLAYERS)) {
            status.setStatus(Status.NOT_ENOUGH_PLAYERS);
            board.clear();
        }
    }

    /**
     * Marks square of game board with player symbol.
     *
     * @param name of player marking square
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
            changeActiveSymbol();
        }

        if(!status.hasStatus(Status.IN_PROGRESS)) {
            PlayerUtils.untagRematchForEveryone(players.getPlayers());
        }
    }


    /**
     * Tag player as ready for rematch.
     *
     * When all players are tagged game
     * status changes for IN_PROGRESS
     *
     * @param name of player wanting rematch
     */
    public void rematch(String name) {
        Player player = players.getPlayer(name);

        if(player == null) {
            throw new RuntimeException("Player is not in the room");
        }

        if(!status.hasStatus(Status.WIN) && !status.hasStatus(Status.DRAW)) {
            throw new RuntimeException("It's not time for rematch");
        }

        if(player.isReadyForRematch()) {
            throw new RuntimeException("Wait for second player to rematch");
        }

        player.rematchReady(true);

        if(PlayerUtils.areEveryoneReadyForRematch(players.getPlayers())) {
            status.setStatus(Status.IN_PROGRESS);
            changeActiveSymbol();
            board.clear();
        }
    }

    /**
     * @return Game data containing:<br>
     * board - 2dim Symbol array<br>
     * players - collection of players<br>
     * status - current game status<br>
     * activePlayer - currently active player symbol
     */
    public GameData getGameData() {
        GameData gameData = new GameData();
        gameData.setBoard(board.as2DimArray());
        gameData.setPlayers(players.getPlayers());
        gameData.setStatus(status.getStatus());
        gameData.setActiveSymbol(active.getSymbol());
        return gameData;
    }

    private Player createPlayer(String name) {
        Optional<PlayerSymbol> symbol = symbols.getFirstAvailableSymbol();

        if(symbol.isEmpty()) {
            throw new RuntimeException("Game is full!");
        }

        return new Player(symbol.get(), name);
    }

    /**
     * Changes currently active player in game.<br>
     * x -> O<br>
     * O -> X
     */
    private void changeActiveSymbol() {
        if(this.active.getSymbol() == PlayerSymbol.X) {
            this.active.setSymbol(PlayerSymbol.O);
        } else {
            this.active.setSymbol(PlayerSymbol.X);
        }
    }

}
