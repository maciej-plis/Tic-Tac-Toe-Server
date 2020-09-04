package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.exceptions.SquareMarkingException;
import matthias.tictactoe.game.model.ActiveSymbol;
import matthias.tictactoe.game.model.GameBoard;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.utils.BoardChecker;

import java.awt.*;
import java.util.Map;

public class InProgressGameState extends GameState {
    private GameBoard board;
    private ActiveSymbol active;

    public InProgressGameState(TicTacToeGame game) {
        super(game, StateType.IN_PROGRESS);
        board = new GameBoard(game::newEvent);
        active = new ActiveSymbol(game::newEvent);
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
        Player player = playersManager.getPlayer(name);

        if(active.getSymbol() != player.getSymbol()) {
            throw new GameException("It's not your turn.");
        }

        try {
            board.mark(point, player.getSymbol().symbol());
            game.newEvent(GameEventFactory.createBoardChangedEvent(board.as2DimArray()));
        } catch(SquareMarkingException e) {
            throw new GameException(e.getMessage());
        }

        if(BoardChecker.isWin(board)) {
            game.setState(new FinishedGameState(game, StateType.WIN));
        } else if(BoardChecker.isDraw(board)) {
            game.setState(new FinishedGameState(game, StateType.DRAW));
        } else {
            active.next();
            game.newEvent(GameEventFactory.createActiveSymbolChangedEvent(active.getSymbol()));
        }
    }

    @Override
    public void rematch(String name) {
        throw new GameException("You can vote for rematch after finished game.");
    }

    @Override
    public Map<String, Object> getGameData() {
        Map<String, Object> gameData = super.getGameData();
        gameData.put("board", board.as2DimArray());
        gameData.put("activeSymbol", active.getSymbol());
        return gameData;
    }
}
