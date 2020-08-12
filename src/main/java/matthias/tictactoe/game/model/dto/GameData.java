package matthias.tictactoe.game.model.dto;

import lombok.Data;
import matthias.tictactoe.game.model.GameStatus;

import java.util.Map;

@Data
public class GameData {

    private Character[][] board;

    private Map<Character, String> players;

    private GameStatus status;
    private Character tour;
}
