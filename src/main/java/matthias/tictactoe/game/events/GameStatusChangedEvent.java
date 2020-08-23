package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.GameStatus;

@Data
@AllArgsConstructor
public class GameStatusChangedEvent {
    private final GameEventType type = GameEventType.GAME_STATUS_CHANGED;
    private GameStatus gameStatus;
}
