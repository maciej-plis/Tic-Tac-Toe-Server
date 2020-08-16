package matthias.tictactoe.game.model.dto;

import lombok.Data;
import matthias.tictactoe.game.model.GameStatus;
import matthias.tictactoe.game.model.Symbol;

import java.util.Map;

@Data
public class GameData {
    private Symbol[][] board;
    private Map<Symbol, String> players;
    private GameStatus status;
    private Symbol tour;
}
