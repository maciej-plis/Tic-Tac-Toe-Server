package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.services.GameEventPublisher;
import matthias.tictactoe.game.services.GamePlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.awt.*;

public abstract class GameState {
    protected ApplicationContext context;
    protected GameEventPublisher eventPublisher;

    protected final StateType type;
    protected final TicTacToeGame game;
    protected final GamePlayerManager playersManager;

    protected GameState(TicTacToeGame game, StateType type) {
        this.game = game;
        this.type = type;
        this.playersManager = game.getPlayersManager();
    }

    public abstract void join(String name);
    public abstract void leave(String name);
    public abstract void mark(String name, Point point);
    public abstract void rematch(String name);

    public StateType getType() {
        return type;
    }

    @Autowired
    public final void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public final void setEventPublisher(GameEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
