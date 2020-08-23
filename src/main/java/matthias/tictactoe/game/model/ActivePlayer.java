package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ActivePlayer {
    private PlayerSymbol symbol;

    public ActivePlayer() {
        this.symbol = PlayerSymbol.X;
    }

    public PlayerSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(PlayerSymbol symbol) {
        this.symbol = symbol;
        GameEventPublisher.publishActivePlayerChangedEvent(this);
    }

    public boolean is(PlayerSymbol symbol) {
        return this.symbol == symbol;
    }
}
