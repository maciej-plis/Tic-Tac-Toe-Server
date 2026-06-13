package matthias.tictactoe.tictactoe_game.tictactoe_game.dto;

import java.util.UUID;

public record PlayerDTO(
    UUID userId,
    String userName,
    SymbolDTO symbol,
    boolean isReady,
    boolean requestedRematch
) {
}
