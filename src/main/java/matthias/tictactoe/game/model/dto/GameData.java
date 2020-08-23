package matthias.tictactoe.game.model.dto;

import lombok.Data;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import matthias.tictactoe.game.model.Status;
import matthias.tictactoe.game.model.Symbol;

import java.util.Collection;
import java.util.Map;

@Data
public class GameData {
    private Symbol[][] board;
    private Collection<Player> players;
    private Status status;
    private PlayerSymbol activePlayer;
}
