package matthias.tictactoe.game.model;

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

    public void set(Point point, Symbol symbol) {
        board[point.x][point.y] = symbol;
        gameEventPublisher.publishBoardChangedEvent(this);
    }

    public boolean isEmpty(Point point) {
        return board[point.x][point.y] == Symbol.EMPTY;
    }

    public Symbol[][] as2DimArray() {
        return board;
    }

    public Symbol[] as1DimArray() {
        return Arrays.stream(board).flatMap(row -> Arrays.stream(row)).toArray(Symbol[]::new);
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
