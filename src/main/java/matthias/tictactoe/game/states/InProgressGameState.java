package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.exceptions.SquareMarkingException;
import matthias.tictactoe.game.model.*;
import matthias.tictactoe.game.utils.BoardChecker;

import java.awt.*;
import java.util.Map;

public class InProgressGameState extends GameState {

    protected InProgressGameState(TicTacToeGame game) {
        super(game, StateType.IN_PROGRESS);
        board.clear();
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
        Player player = playersManager.getPlayer(name);

        if(active.getSymbol() != player.getSymbol()) {
            throw new GameException("It's not your turn.");
        }

        try {
            board.mark(point, player.getSymbol().symbol());
        } catch(SquareMarkingException e) {
            throw new GameException(e.getMessage());
        }

        if(BoardChecker.isWin(board)) {
            game.setState(new FinishedGameState(game, StateType.WIN, active.getSymbol(), board));
        } else if(BoardChecker.isDraw(board)) {
            game.setState(new FinishedGameState(game, StateType.DRAW, active.getSymbol(), board));
        } else {
            active.next();
        }
    }

    @Override
    public void rematch(String name) {
        throw new GameException("You can vote for rematch after finished game.");
    }
}
