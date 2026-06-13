package matthias.tictactoe.tictactoe_game.tictactoe_game.event;

import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.SymbolDTO;

import java.util.UUID;

public record PlayerLeftEvent(
    UUID userId,
    SymbolDTO symbol
) implements Event {
}
