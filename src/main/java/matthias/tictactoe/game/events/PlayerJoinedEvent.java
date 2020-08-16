package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.Symbol;

@Data
@AllArgsConstructor
public class PlayerJoinedEvent {
    private final GameEventType type = GameEventType.PLAYER_JOINED;
    private Symbol playerSymbol;
    private String playerName;
}
