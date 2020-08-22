package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ActivePlayer {
    private Symbol symbol;

    public ActivePlayer() {
        this.symbol = Symbol.X;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        GameEventPublisher.publishActivePlayerChangedEvent(this);
    }
}
