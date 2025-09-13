package matthias.tictactoe.tictactoe_game;

import java.util.UUID;

record Player(
    UUID userId,
    String name,
    Symbol symbol
) {
}
