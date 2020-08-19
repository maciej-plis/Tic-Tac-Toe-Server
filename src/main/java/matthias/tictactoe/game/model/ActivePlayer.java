package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;

public class ActivePlayer {
    private Symbol symbol;

    public ActivePlayer(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        GameEventPublisher.publishActivePlayerChangedEvent(this);
    }
}
