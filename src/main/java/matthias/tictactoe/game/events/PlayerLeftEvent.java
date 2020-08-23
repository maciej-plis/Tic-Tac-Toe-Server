package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.Player;

@Data
@AllArgsConstructor
public class PlayerLeftEvent {
    private final GameEventType type = GameEventType.PLAYER_LEFT;
    private Player player;
}
