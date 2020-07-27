package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerIsReadyEvent {

    private final String name = "PLAYER_READY";
    private String playerName;
}
