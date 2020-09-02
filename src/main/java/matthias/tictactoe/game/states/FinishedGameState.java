package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.utils.PlayerUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@Scope(scopeName = "prototype")
public class FinishedGameState extends GameState {

    protected FinishedGameState(TicTacToeGame game, StateType type) {
        super(game, type);
    }

    @Override
    public void join(String name) {
        throw new GameException("Game is full.");
    }

    @Override
    public void leave(String name) {
        playersManager.removePlayer(name);
        game.setState(context.getBean(NotEnoughPlayersGameState.class, game));
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
            game.setState(context.getBean(InProgressGameState.class, game));
        }
    }
}
