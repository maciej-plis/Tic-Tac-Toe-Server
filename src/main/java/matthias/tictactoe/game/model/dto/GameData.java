package matthias.tictactoe.game.model.dto;

import lombok.Data;
import matthias.tictactoe.game.model.Status;
import matthias.tictactoe.game.model.Symbol;

import java.util.Map;

@Data
public class GameData {
    private Symbol[][] board;
    private Map<Symbol, String> players;
    private Status status;
    private Symbol activePlayer;
}
