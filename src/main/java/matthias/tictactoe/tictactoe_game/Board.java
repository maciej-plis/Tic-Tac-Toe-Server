package matthias.tictactoe.tictactoe_game;

import lombok.EqualsAndHashCode;

import static java.util.Arrays.stream;
import static matthias.tictactoe.tictactoe_game.Board.BoardResult.*;
import static matthias.tictactoe.tictactoe_game.Symbol.X;
import static org.apache.commons.lang3.Range.between;

@EqualsAndHashCode
class Board {

    protected final int size;
    private final Symbol[][] board;

    private BoardResult result;
    private int moveCount = 0;

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

    void set(BoardCoordinates coord, Symbol symbol) {
        if (result != null) throw new RuntimeException("Board is filled");
        if (board[coord.x][coord.y] != null) throw new RuntimeException("Invalid board coordinates");

        board[coord.x][coord.y] = symbol;
        moveCount++;

        updateResult(coord, symbol);
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

    BoardCoordinates toBoardCoordinates(Coordinates coord) {
        return new BoardCoordinates(coord.x, coord.y);
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

    private void updateResult(BoardCoordinates coord, Symbol symbol) {
        if (isWinningHorizontally(coord, symbol) ||
            isWinningVertically(coord, symbol) ||
            isWinningDiagonally(coord, symbol) ||
            isWinningAntiDiagonally(coord, symbol)) {
            result = symbol == X ? WIN_X : WIN_O;
        } else if (moveCount == size * size) {
            result = TIE;
        }
    }

    private boolean isWinningHorizontally(BoardCoordinates coord, Symbol symbol) {
        for (int i = 0; i < size; i++) {
            if (board[coord.x][i] != symbol) return false;
        }
        return true;
    }

    private boolean isWinningVertically(BoardCoordinates coord, Symbol symbol) {
        for (int i = 0; i < size; i++) {
            if (board[i][coord.y] != symbol) return false;
        }
        return true;
    }

    private boolean isWinningDiagonally(BoardCoordinates coord, Symbol symbol) {
        if (coord.x != coord.y) return false;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != symbol) return false;
        }
        return true;
    }

    private boolean isWinningAntiDiagonally(BoardCoordinates coord, Symbol symbol) {
        if (coord.x + coord.y != size - 1) return false;
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
