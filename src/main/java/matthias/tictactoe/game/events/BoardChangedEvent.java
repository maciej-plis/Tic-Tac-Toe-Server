package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.Board;

@Data
@AllArgsConstructor
public class BoardChangedEvent {

    private final String name = "BOARD_CHANGED";
    private Board board;
}
