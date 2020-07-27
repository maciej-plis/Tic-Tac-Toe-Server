package matthias.tictactoe.game.helpers;

import matthias.tictactoe.game.model.Board;
import matthias.tictactoe.game.model.Symbol;

public class BoardChecker {

    private static int[][][] WINNING_COMBINATIONS = new int[][][]{
        {{0,0},{0,1},{0,2}}, {{1,0},{1,1},{1,2}}, {{2,0},{2,1},{2,2}}, //horizontal
        {{0,0},{1,0},{2,0}}, {{0,1},{1,1},{2,1}}, {{0,2},{1,2},{2,2}}, //vertical
        {{0,0},{1,1},{2,2}}, {{0,2},{1,1},{2,0}} // diagonal
    };

    public static boolean win(Board board) {
        return horizontalWin(board) || verticalWin(board) || diagonalWin(board) ? true : false;
    }

    public static boolean draw(Board board) {

        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if(board.get(i, j) == Symbol.EMPTY) {
                    return false;
                }
            }
        }

        if(win(board)) {
            return false;
        }

        return true;
    }

    private static boolean horizontalWin(Board board) {
        final int boardLastIndex = board.getSize()-1;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = boardLastIndex; j >= 0; j++) {
                if(board.get(i, j) == Symbol.EMPTY) {
                    break;
                }
                if(j == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean verticalWin(Board board) {
        final int boardLastIndex = board.getSize()-1;
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = boardLastIndex; j >= 0; j++) {
                if(board.get(j, i) == Symbol.EMPTY) {
                    break;
                }
                if(j == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean diagonalWin(Board board) {

        final int boardLastIndex = board.getSize()-1;
        for (int i = boardLastIndex; i >= 0; i--) {
            if(board.get(boardLastIndex-i, i) == Symbol.EMPTY) {
                break;
            }
            if(i == 0 ) {
                return true;
            }
        }

        for (int i = 0; i < board.getSize(); i++) {
            if(board.get(i, i) == Symbol.EMPTY) {
                break;
            }
            if(i == 0 ) {
                return true;
            }
        }

        return false;
    }
}
