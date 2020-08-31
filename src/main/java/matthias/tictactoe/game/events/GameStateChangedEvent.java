package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.states.GameState;

@Data
@AllArgsConstructor
public class GameStateChangedEvent {
    private final GameEventType type = GameEventType.GAME_STATE_CHANGED;
    private StateType stateType;
}
