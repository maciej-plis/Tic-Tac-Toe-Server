package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.utils.PlayerUtils;

import java.awt.*;

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
            game.setState(new InProgressGameState(game));
        }
    }
}
