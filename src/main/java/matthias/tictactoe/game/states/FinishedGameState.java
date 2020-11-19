package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.model.*;
import matthias.tictactoe.game.utils.PlayerUtils;

import java.awt.*;
import java.util.Map;

public class FinishedGameState extends GameState {

    private PlayerSymbol lastMove;
    private GameBoard board;

    protected FinishedGameState(TicTacToeGame game, StateType type, PlayerSymbol lastMove, GameBoard board) {
        super(game, type);

        this.lastMove = lastMove;
        this.board = board;
    }

    @Override
    public void join(String name) {
        throw new GameException("Game is full.");
    }

    @Override
    public void leave(String name) {
        Player removedPlayer = playersManager.removePlayer(name);
        game.newEvent(GameEventFactory.createPlayerLeftEvent(removedPlayer));
        game.setState(new NotEnoughPlayersGameState(game));
    }

    @Override
    public void mark(String name, Point point) {
        throw new GameException("Game is not in progress.");
    }

    @Override
    public void rematch(String name) {
        if(playersManager.getPlayer(name).isReadyForRematch()) {
            throw new GameException("You already requested rematch.");
        }

        playersManager.getPlayer(name).readyForRematch(true);

        if(PlayerUtils.areEveryoneReadyForRematch(playersManager.getPlayers())) {
            PlayerUtils.untagRematchForEveryone(playersManager.getPlayers());
            game.setState(new InProgressGameState(game, lastMove));
        }
    }

    @Override
    public Map<String, Object> getGameData() {
        Map<String, Object> gameData = super.getGameData();
        gameData.put("board", board.as2DimArray());
        gameData.put("activeSymbol", lastMove);
        return gameData;
    }
}
