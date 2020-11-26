package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.model.*;
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
        playersManager.removePlayer(name);
        game.setState(new NotEnoughPlayersGameState(game));
    }

    @Override
    public void mark(String name, Point point) {
        throw new GameException("Game hasn't started yet.");
    }

    @Override
    public void rematch(String name) {
        if(playersManager.getPlayer(name).isReadyForRematch()) {
            throw new GameException("You already requested rematch.");
        }

        Player player = playersManager.getPlayer(name);
        player.readyForRematch(true);
        game.newEvent(GameEventFactory.createPlayerRequestedRematchEvent(player));

        if(PlayerUtils.areEveryoneReadyForRematch(playersManager.getPlayers())) {
            PlayerUtils.untagRematchForEveryone(playersManager.getPlayers(), game::newEvent);
            game.setState(new InProgressGameState(game));
            active.next();
        }
    }

}
