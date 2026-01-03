package matthias.tictactoe.tictactoe_game.game_room.endpoint.dto;

import matthias.tictactoe.tictactoe_game.game_room.dto.GameDetails;
import matthias.tictactoe.tictactoe_game.game_room.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.SpectatorDTO;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NullMarked
public record GameRoomDto(
    UUID gameRoomId,
    String gameRoomName,
    @Nullable UUID ownerId,
    @Nullable Instant creationDate,
    boolean spectatingEnabled,
    @Nullable List<PlayerDTO> players,
    int playersCount,
    @Nullable List<SpectatorDTO> spectators,
    int spectatorsCount,
    @Nullable GameDetails gameDetails
) {
}
