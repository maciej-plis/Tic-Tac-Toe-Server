package matthias.tictactoe.game.model;

import matthias.tictactoe.game.exceptions.SquareMarkingException;
import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;

@Component
@Scope("prototype")
public class GameBoard {
    private final GameEventPublisher gameEventPublisher;
    private final int BOARD_SIZE = 3;
    private final Symbol[][] board = new Symbol[BOARD_SIZE][BOARD_SIZE];

    public GameBoard(GameEventPublisher gameEventPublisher) {
        this.gameEventPublisher = gameEventPublisher;
        clear();
    }

    public void clear() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Symbol.EMPTY;
            }
        }
        gameEventPublisher.publishBoardChangedEvent(this);
    }

    public void mark(Point point, Symbol symbol) {
        if(board[point.x][point.y] != Symbol.EMPTY) {
            throw new SquareMarkingException("This square is already marked");
        }
        if(point.x < 0 || point.x > BOARD_SIZE || point.y < 0 || point.y > BOARD_SIZE) {
            throw new SquareMarkingException(String.format("point (%d, %d) is not on the board", point.x, point.y));
        }

        board[point.x][point.y] = symbol;
        gameEventPublisher.publishBoardChangedEvent(this);
    }

    public Symbol[][] as2DimArray() {
        return board;
    }

    public Symbol[] as1DimArray() {
        return Arrays.stream(board).flatMap(Arrays::stream).toArray(Symbol[]::new);
    }

    public boolean isFull() {
        for(int i=0; i<BOARD_SIZE; i++) {
            for(int j=0; j<BOARD_SIZE; j++) {
                if(board[i][j] == Symbol.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

}
