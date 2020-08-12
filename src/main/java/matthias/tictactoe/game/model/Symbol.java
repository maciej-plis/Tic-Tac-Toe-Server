package matthias.tictactoe.game.model;

public enum Symbol {

    X('X'),
    O('O'),
    EMPTY(' ');

    private char character;

    Symbol(char c) {
        this.character = c;
    }

    public char getChar() {
        return this.character;
    }

    @Override
    public String toString() {
        return "" + character;
    }
}
