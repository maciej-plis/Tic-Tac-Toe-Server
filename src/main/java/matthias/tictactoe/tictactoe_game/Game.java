package matthias.tictactoe.tictactoe_game;

import matthias.tictactoe.shared.command.CommandHandler;
import matthias.tictactoe.tictactoe_game.game_room.dto.GameDetails;
import matthias.tictactoe.tictactoe_game.game_room.dto.PlayerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Game extends CommandHandler {
    List<PlayerDTO> getPlayers();
    Optional<PlayerDTO> findPlayer(UUID userId);
    void addPlayer(UUID userId);
    void removePlayer(UUID userId);
    boolean hasPlayer(UUID userId);
    GameDetails getDetails();
}
