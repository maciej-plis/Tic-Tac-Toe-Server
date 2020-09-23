package matthias.tictactoe.web.game.dtos;

import lombok.Builder;
import lombok.Getter;
import matthias.tictactoe.game.model.PlayerSymbol;

import java.util.Map;

@Getter
@Builder
public class GameDto {
    private String id;
    private String name;
    private Map<PlayerSymbol, String> players;
}
