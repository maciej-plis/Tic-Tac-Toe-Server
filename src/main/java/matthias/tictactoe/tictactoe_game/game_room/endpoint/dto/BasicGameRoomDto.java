package matthias.tictactoe.tictactoe_game.game_room.endpoint.dto;

import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
public record BasicGameRoomDto(
    UUID gameRoomId,
    String gameRoomName,
    boolean spectatingEnabled,
    int playersCount,
    int spectatorsCount
) {
}
