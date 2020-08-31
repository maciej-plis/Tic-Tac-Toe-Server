package matthias.tictactoe.game.model.dto;

import lombok.Builder;
import lombok.Data;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.model.Symbol;

import java.util.Collection;

@Builder
@Data
public class InitialGameData {
    private Collection<Player> players;
    private StateType state;
}
