package matthias.tictactoe.tictactoe_game.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GameSummaryDto {
    private final GameStatus status;
    private final UUID player1Id;
    private final UUID player2Id;
    private final UUID[][] board;
    private final UUID playerTurn;
}
