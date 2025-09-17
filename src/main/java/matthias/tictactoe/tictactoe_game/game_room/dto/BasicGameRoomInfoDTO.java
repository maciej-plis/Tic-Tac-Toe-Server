package matthias.tictactoe.tictactoe_game.game_room.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BasicGameRoomInfoDTO(
    UUID gameRoomId,
    String gameRoomName,
    int playersCount,
    int spectatorsCount
) {
}
