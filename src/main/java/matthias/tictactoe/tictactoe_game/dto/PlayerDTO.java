package matthias.tictactoe.tictactoe_game.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PlayerDTO(
    UUID id,
    String name,
    SymbolDTO symbol,
    boolean isReady
) {
}
