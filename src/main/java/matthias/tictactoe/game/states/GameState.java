package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.model.ActiveSymbol;
import matthias.tictactoe.game.model.GameBoard;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.services.GamePlayerManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GameState {
    protected final TicTacToeGame game;
    protected final StateType type;

    protected final GamePlayerManager playersManager;
    protected final GameBoard board;
    protected final ActiveSymbol active;

    protected GameState(TicTacToeGame game, StateType type) {
        this.game = game;
        this.type = type;

        this.playersManager = game.getPlayersManager();
        this.board = game.getBoard();
        this.active = game.getActive();

        game.newEvent(GameEventFactory.createStateChangedEvent(type));
    }

    public abstract void join(String name);
    public abstract void leave(String name);
    public abstract void mark(String name, Point point);
    public abstract void rematch(String name);

    public Map<String, Object> getGameData() {
        Map<String, Object> gameData = new HashMap<>();
        gameData.put("name", game.getName());
        gameData.put("players", playersManager.getPlayers());
        gameData.put("board", board.as2DimArray());
        gameData.put("activeSymbol", active.getSymbol());
        gameData.put("state", type);
        return gameData;
    }
}
