package matthias.tictactoe.game.model;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;

import java.util.function.Consumer;

public class ActiveSymbol {
    private final Consumer<GameEvent> eventCallback;
    private PlayerSymbol symbol;

    public ActiveSymbol(Consumer<GameEvent> eventCallback) {
        this.eventCallback = eventCallback;
        this.symbol = PlayerSymbol.X;
        eventCallback.accept(GameEventFactory.createActiveSymbolChangedEvent(symbol));
    }

    public ActiveSymbol(Consumer<GameEvent> eventCallback, PlayerSymbol active) {
        this.eventCallback = eventCallback;
        this.symbol = active;
        this.next();
    }

    public PlayerSymbol getSymbol() {
        return symbol;
    }

    public void next() {
        if(symbol == PlayerSymbol.X) {
            symbol = PlayerSymbol.O;
        } else {
            symbol = PlayerSymbol.X;
        }
        eventCallback.accept(GameEventFactory.createActiveSymbolChangedEvent(symbol));
    }

    public boolean is(PlayerSymbol symbol) {
        return this.symbol == symbol;
    }
}
