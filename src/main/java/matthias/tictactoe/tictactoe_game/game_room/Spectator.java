package matthias.tictactoe.tictactoe_game.game_room;

import java.util.UUID;

record Spectator(
    UUID userId,
    String name
) {
}
