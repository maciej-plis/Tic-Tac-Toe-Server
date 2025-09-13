package matthias.tictactoe.tictactoe_game.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record GameRoomDTO(
    Set<PlayerDTO> players,
    Set<SpectatorDTO> spectators,
    SymbolDTO turn,
    SymbolDTO[][] gameBoard,
    GameStatusDTO gameStatus
) {
}
