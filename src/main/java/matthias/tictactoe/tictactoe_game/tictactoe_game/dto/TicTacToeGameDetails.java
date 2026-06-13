package matthias.tictactoe.tictactoe_game.tictactoe_game.dto;

import lombok.Builder;
import matthias.tictactoe.tictactoe_game.game_room.dto.GameDetails;

import java.util.List;

@Builder
public record TicTacToeGameDetails(
    GameStatusDTO gameStatus,
    List<PlayerDTO> players,
    SymbolDTO[][] board,
    SymbolDTO symbolTurn,
    SymbolDTO symbolWinner
) implements GameDetails {
}
