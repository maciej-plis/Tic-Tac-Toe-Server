package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.ActivePlayer;

@Data
@AllArgsConstructor
public class ActivePlayerChangedEvent {
    private final GameEventType type = GameEventType.ACTIVE_PLAYER_CHANGED;
    private ActivePlayer activePlayer;
}
