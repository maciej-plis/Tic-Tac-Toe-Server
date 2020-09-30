package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.services.GamePlayerManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GameState {
    protected final StateType type;
    protected final TicTacToeGame game;
    protected final GamePlayerManager playersManager;

    protected GameState(TicTacToeGame game, StateType type) {
        this.game = game;
        this.type = type;
        this.playersManager = game.getPlayersManager();
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
        gameData.put("state", type);
        return gameData;
    }
}
