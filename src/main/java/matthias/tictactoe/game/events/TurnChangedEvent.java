package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.Symbol;

@Data
@AllArgsConstructor
public class TurnChangedEvent {

    private final String name = "PLAYER_TURN_CHANGED";
    private Symbol symbol;
}
