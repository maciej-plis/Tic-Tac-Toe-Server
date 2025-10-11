package matthias.tictactoe.tictactoe_game.game_room.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record DetailedGameRoomInfoDTO(
    UUID gameRoomId,
    String gameRoomName,
    UUID ownerId,
    Instant creationDate,
    boolean spectatingEnabled,
    List<PlayerDTO> players,
    List<SpectatorDTO> spectators,
    GameDetails gameDetails
) {
}
