package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.ActiveSymbol;

@Data
@AllArgsConstructor
public class ActiveSymbolChangedEvent {
    private final GameEventType type = GameEventType.ACTIVE_SYMBOL_CHANGED;
    private ActiveSymbol activeSymbol;
}
