package matthias.tictactoe.tictactoe_game.tictactoe_game;

import java.util.UUID;

record TicTacToePlayer(
    UUID userId,
    String name,
    TicTacToeSymbol symbol
) {
}
