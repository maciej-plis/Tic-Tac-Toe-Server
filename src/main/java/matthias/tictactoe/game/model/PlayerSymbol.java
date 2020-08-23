package matthias.tictactoe.game.model;

public enum PlayerSymbol {
    X(Symbol.X),
    O(Symbol.O);

    private Symbol symbol;

    PlayerSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol symbol() {
        return this.symbol;
    }

}
