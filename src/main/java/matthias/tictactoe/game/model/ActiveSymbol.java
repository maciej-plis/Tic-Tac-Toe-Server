package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ActiveSymbol {
    private PlayerSymbol symbol;

    public ActiveSymbol() {
        this.symbol = PlayerSymbol.X;
    }

    public PlayerSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(PlayerSymbol symbol) {
        this.symbol = symbol;
        GameEventPublisher.publishActiveSymbolChangedEvent(this);
    }

    public boolean is(PlayerSymbol symbol) {
        return this.symbol == symbol;
    }
}
