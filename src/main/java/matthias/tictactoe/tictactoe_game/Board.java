package matthias.tictactoe.tictactoe_game;

import lombok.EqualsAndHashCode;

import java.util.Arrays;

import static java.util.Arrays.stream;
import static matthias.tictactoe.tictactoe_game.Board.BoardResult.*;
import static matthias.tictactoe.tictactoe_game.Symbol.X;
import static org.apache.commons.lang3.Range.between;

@EqualsAndHashCode
class Board {

    protected final int size;
    private final Symbol[][] board;

    private BoardResult result;
    private int moveCount;

    private Board(int size, Symbol[][] board, BoardResult result, int moveCount) {
        this.size = size;
        this.board = board;
        this.result = result;
        this.moveCount = moveCount;
    }

    Board(int size) {
        if (size < 2 || size > 10) throw new IllegalArgumentException();
        this.size = size;
        this.board = new Symbol[size][size];
        this.result = null;
        this.moveCount = 0;
    }

    void reset() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = null;
            }
        }
        this.result = null;
        this.moveCount = 0;
    }

    void set(BoardCoordinates coords, Symbol symbol) {
        if (result != null) throw new RuntimeException("Board is filled");
        if (board[coords.x][coords.y] != null) throw new RuntimeException("Invalid board coordinates");

        board[coords.x][coords.y] = symbol;
        moveCount++;

        updateResult(coords, symbol);
    }

    boolean hasResult() {
        return result != null;
    }

    BoardResult getResult() {
        return result;
    }

    int getMoveCount() {
        return moveCount;
    }

    BoardCoordinates toBoardCoordinates(int x, int y) {
        return new BoardCoordinates(x, y);
    }

    Board deepClone() {
        return new Board(
            size,
            stream(board).map(Symbol[]::clone).toArray(Symbol[][]::new),
            result,
            moveCount
        );
    }

    Symbol[][] getInnerBoard() {
        return Arrays.stream(board)
            .map(Symbol[]::clone)
            .toArray(Symbol[][]::new);
    }

    private void updateResult(BoardCoordinates coords, Symbol symbol) {
        if (isWinningHorizontally(coords, symbol) ||
            isWinningVertically(coords, symbol) ||
            isWinningDiagonally(coords, symbol) ||
            isWinningAntiDiagonally(coords, symbol)) {
            result = symbol == X ? WIN_X : WIN_O;
        } else if (moveCount == size * size) {
            result = TIE;
        }
    }

    private boolean isWinningHorizontally(BoardCoordinates coords, Symbol symbol) {
        for (int i = 0; i < size; i++) {
            if (board[coords.x][i] != symbol) return false;
        }
        return true;
    }

    private boolean isWinningVertically(BoardCoordinates coords, Symbol symbol) {
        for (int i = 0; i < size; i++) {
            if (board[i][coords.y] != symbol) return false;
        }
        return true;
    }

    private boolean isWinningDiagonally(BoardCoordinates coords, Symbol symbol) {
        if (coords.x != coords.y) return false;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != symbol) return false;
        }
        return true;
    }

    private boolean isWinningAntiDiagonally(BoardCoordinates coords, Symbol symbol) {
        if (coords.x + coords.y != size - 1) return false;
        for (int i = 0; i < size; i++) {
            if (board[size - 1 - i][i] != symbol) return false;
        }
        return true;
    }

    class BoardCoordinates {
        final int x;
        final int y;

        private BoardCoordinates(int x, int y) {
            final var range = between(0, size - 1);
            if (!range.contains(x) || !range.contains(y)) throw new IllegalArgumentException();
            this.x = x;
            this.y = y;
        }
    }

    enum BoardResult {
        WIN_X, WIN_O, TIE
    }
}
