package matthias.tictactoe.tictactoe_game.game_room.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PlayerDTO(
    UUID id,
    String name
) {
}
