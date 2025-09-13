package matthias.tictactoe.tictactoe_game.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GameSummaryDTO(
    GameStatusDTO status,
    UUID player1Id,
    UUID player2Id,
    UUID[][] board,
    UUID playerTurn
) {
}
