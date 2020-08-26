package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ActiveSymbol {
    private final GameEventPublisher gameEventPublisher;
    private PlayerSymbol symbol;

    public ActiveSymbol(GameEventPublisher gameEventPublisher) {
        this.gameEventPublisher = gameEventPublisher;
        this.symbol = PlayerSymbol.X;
    }

    public PlayerSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(PlayerSymbol symbol) {
        this.symbol = symbol;
        gameEventPublisher.publishActiveSymbolChangedEvent(this);
    }

    public boolean is(PlayerSymbol symbol) {
        return this.symbol == symbol;
    }
}
