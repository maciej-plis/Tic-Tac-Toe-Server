package matthias.tictactoe.game.model;

public class Board {

    private final int BOARD_SIZE = 3;

    private final Symbol[][] board;

    public Board() {
        board = new Symbol[BOARD_SIZE][BOARD_SIZE];
        clear();
    }

    public void clear() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Symbol.EMPTY;
            }
        }
    }

    public void set(int x, int y, Symbol symbol) {
        board[x][y] = symbol;
    }

    public Symbol get(int x, int y) {
        return board[x][y];
    }

    public boolean isEmpty(int x, int y) {
        return board[x][y] == Symbol.EMPTY;
    }

    public Symbol[][] getBoard() {
        return board;
    }

    public int getSize() {
        return this.BOARD_SIZE;
    }

}
