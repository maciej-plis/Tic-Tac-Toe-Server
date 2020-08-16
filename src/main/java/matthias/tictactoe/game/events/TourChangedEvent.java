package matthias.tictactoe.game.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import matthias.tictactoe.game.model.Symbol;

@Data
@AllArgsConstructor
public class TourChangedEvent {
    private final GameEventType type = GameEventType.TOUR_CHANGED;
    private Symbol tour;
}
