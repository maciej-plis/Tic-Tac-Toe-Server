package matthias.tictactoe.tictactoe_game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static matthias.tictactoe.tictactoe_game.Board.BoardResult.*;
import static matthias.tictactoe.tictactoe_game.Symbol.O;
import static matthias.tictactoe.tictactoe_game.Symbol.X;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 11, 15})
    void shouldNotAllowBoardSizeBelow2OrAbove10(int size) {
        assertThrows(IllegalArgumentException.class, () -> new Board(size));
    }

    @ParameterizedTest
    @MethodSource("xWinBoardLayouts")
    void shouldHaveResultWinX(Board board) {
        assertSame(WIN_X, board.getResult());
    }

    @ParameterizedTest
    @MethodSource("oWinBoardLayouts")
    void shouldHaveResultWinO(Board board) {
        assertSame(WIN_O, board.getResult());
    }

    @ParameterizedTest
    @MethodSource("tieBoardLayouts")
    void shouldHaveResultTie(Board board) {
        assertSame(TIE, board.getResult());
    }

    @ParameterizedTest
    @MethodSource("NoResultBoardLayouts")
    void shouldNotHaveResult(Board board) {
        assertSame(null, board.getResult());
    }

    @Test
    void shouldThrowWhenTryingToSetCoordinatesThatAreAlreadySet() {
        final var board = new Board(3);
        final var coords = board.toBoardCoordinates(0, 0);
        board.set(coords, X);

        assertThrows(RuntimeException.class, () -> board.set(coords, O));
    }

    @Test
    void shouldThrowWhenTryingToSetBoardWithResult() {
        final var board = new Board(3);
        board.set(board.toBoardCoordinates(0, 0), X);
        board.set(board.toBoardCoordinates(1, 0), X);
        board.set(board.toBoardCoordinates(2, 0), X);

        assertThrows(RuntimeException.class, () -> board.set(board.toBoardCoordinates(2, 2), X));
    }

    @ParameterizedTest
    @MethodSource("valid3x3Coordinates")
    void shouldCreateBoardCoordinates(Coordinates coords) {
        final var board = new Board(3);

        assertDoesNotThrow(() -> board.toBoardCoordinates(coords));
    }

    @ParameterizedTest
    @MethodSource("invalid3x3Coordinates")
    void shouldThrowWhenTryingToCreateInvalidBoardCoordinates(Coordinates coords) {
        final var board = new Board(3);

        assertThrows(IllegalArgumentException.class, () -> board.toBoardCoordinates(coords));
    }

    static Stream<Coordinates> valid3x3Coordinates() {
        return Stream.of(
            new Coordinates(0, 0),
            new Coordinates(0, 1),
            new Coordinates(0, 2),
            new Coordinates(1, 0),
            new Coordinates(1, 1),
            new Coordinates(1, 2),
            new Coordinates(2, 0),
            new Coordinates(2, 1),
            new Coordinates(2, 2)
        );
    }

    static Stream<Coordinates> invalid3x3Coordinates() {
        return Stream.of(
            new Coordinates(-1, 0),
            new Coordinates(0, -1),
            new Coordinates(0, 3),
            new Coordinates(3, 0)
        );
    }

    static Stream<Board> xWinBoardLayouts() {
        return Stream.of(
            toBoard(new char[][]{{'x', 'x', 'x'}, {' ', ' ', ' '}, {' ', ' ', ' '}}),
            toBoard(new char[][]{{' ', ' ', ' '}, {'x', 'x', 'x'}, {' ', ' ', ' '}}),
            toBoard(new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {'x', 'x', 'x'}}),
            toBoard(new char[][]{{'x', ' ', ' '}, {'x', ' ', ' '}, {'x', ' ', ' '}}),
            toBoard(new char[][]{{' ', 'x', ' '}, {' ', 'x', ' '}, {' ', 'x', ' '}}),
            toBoard(new char[][]{{' ', ' ', 'x'}, {' ', ' ', 'x'}, {' ', ' ', 'x'}}),
            toBoard(new char[][]{{'x', ' ', ' '}, {' ', 'x', ' '}, {' ', ' ', 'x'}}),
            toBoard(new char[][]{{' ', ' ', 'x'}, {' ', 'x', ' '}, {'x', ' ', ' '}}),
            toBoard(new char[][]{{'x', ' ', ' ', ' ', ' '}, {' ', 'x', ' ', ' ', ' '}, {' ', ' ', 'x', ' ', ' '}, {' ', ' ', ' ', 'x', ' '}, {' ', ' ', ' ', ' ', 'x'}}),
            toBoard(new char[][]{{'o', 'o', 'o', 'x'}, {'o', 'o', 'o', 'x'}, {'o', 'o', 'o', 'x'}, {'x', 'x', 'x', 'x'}}),
            toBoard(new char[][]{{'x', ' '}, {' ', 'x'}})
        );
    }

    static Stream<Board> oWinBoardLayouts() {
        return Stream.of(
            toBoard(new char[][]{{'o', 'o', 'o'}, {' ', ' ', ' '}, {' ', ' ', ' '}}),
            toBoard(new char[][]{{' ', ' ', ' '}, {'o', 'o', 'o'}, {' ', ' ', ' '}}),
            toBoard(new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {'o', 'o', 'o'}}),
            toBoard(new char[][]{{'o', ' ', ' '}, {'o', ' ', ' '}, {'o', ' ', ' '}}),
            toBoard(new char[][]{{' ', 'o', ' '}, {' ', 'o', ' '}, {' ', 'o', ' '}}),
            toBoard(new char[][]{{' ', ' ', 'o'}, {' ', ' ', 'o'}, {' ', ' ', 'o'}}),
            toBoard(new char[][]{{'o', ' ', ' '}, {' ', 'o', ' '}, {' ', ' ', 'o'}}),
            toBoard(new char[][]{{' ', ' ', 'o'}, {' ', 'o', ' '}, {'o', ' ', ' '}}),
            toBoard(new char[][]{{'o', ' ', ' ', ' ', ' '}, {' ', 'o', ' ', ' ', ' '}, {' ', ' ', 'o', ' ', ' '}, {' ', ' ', ' ', 'o', ' '}, {' ', ' ', ' ', ' ', 'o'}}),
            toBoard(new char[][]{{'x', 'x', 'x', 'o'}, {'x', 'x', 'x', 'o'}, {'x', 'x', 'x', 'o'}, {'o', 'o', 'o', 'o'}}),
            toBoard(new char[][]{{'o', ' '}, {' ', 'o'}})
        );
    }

    static Stream<Board> tieBoardLayouts() {
        return Stream.of(
            toBoard(new char[][]{{'x', 'o', 'x'}, {'x', 'x', 'o'}, {'o', 'x', 'o'}}),
            toBoard(new char[][]{{'x', 'o', 'x'}, {'x', 'o', 'x'}, {'o', 'x', 'o'}}),
            toBoard(new char[][]{{'x', 'o', 'x'}, {'o', 'x', 'x'}, {'o', 'x', 'o'}})
        );
    }

    static Stream<Board> NoResultBoardLayouts() {
        return Stream.of(
            toBoard(new char[][]{{'x', 'o', ' '}, {'x', 'x', 'o'}, {'o', 'x', 'o'}}),
            toBoard(new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}}),
            toBoard(new char[][]{{' ', 'o', 'o'}, {'o', 'x', 'o'}, {'x', 'o', 'x'}})
        );
    }

    private static Board toBoard(char[][] boardValues) {
        final var board = new Board(boardValues.length);
        for (int i = 0; i < boardValues.length; i++) {
            for (int j = 0; j < boardValues[i].length; j++) {
                final var symbol = charToSymbol(boardValues[i][j]);
                if (symbol != null) board.set(board.toBoardCoordinates(i, j), symbol);
            }
        }
        return board;
    }

    private static Symbol charToSymbol(char c) {
        if (c == 'x') return X;
        else if (c == 'o') return O;
        else if (c == ' ') return null;
        throw new IllegalArgumentException();
    }
}
