package matthias.tictactoe.game.model;

import java.awt.*;
import java.util.Arrays;

public class Board {
    private final int BOARD_SIZE = 3;
    private final Symbol[][] board= new Symbol[BOARD_SIZE][BOARD_SIZE];

    public Board() {
        clear();
    }

    public void clear() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Symbol.EMPTY;
            }
        }
    }

    public void set(Point point, Symbol symbol) {
        board[point.x][point.y] = symbol;
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
