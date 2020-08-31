package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.exceptions.SquareMarkingException;
import matthias.tictactoe.game.model.ActiveSymbol;
import matthias.tictactoe.game.model.GameBoard;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.utils.BoardChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@Scope(scopeName = "prototype")
public class InProgressGameState extends GameState {
    private GameBoard board;
    private ActiveSymbol active;

    public InProgressGameState(TicTacToeGame game) {
        super(game, StateType.IN_PROGRESS);
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
            game.setState(context.getBean(FinishedGameState.class, game, StateType.WIN));
        } else if(BoardChecker.isDraw(board)) {
            game.setState(context.getBean(FinishedGameState.class, game, StateType.DRAW));
        } else {
            active.next();
        }
    }

    @Override
    public void rematch(String name) {
        throw new GameException("You can vote for rematch after finished game.");
    }

    @Autowired
    public void setBoard(GameBoard board) {
        this.board = board;
    }

    @Autowired
    public void setActive(ActiveSymbol active) {
        this.active = active;
    }
}
