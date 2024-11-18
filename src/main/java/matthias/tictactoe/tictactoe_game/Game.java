package matthias.tictactoe.tictactoe_game;

import lombok.Getter;

import static java.util.UUID.randomUUID;

class Game {

    @Getter
    private final GameId gameId = new GameId(randomUUID());
    private GameState gameState = new AwaitingPlayers();

    void join(PlayerId playerId) {
        gameState = gameState.join(playerId);
    }

    void leave(PlayerId playerId) {
        gameState = gameState.leave(playerId);
    }

    void changeSymbol(PlayerId playerId, Symbol symbol) {
        gameState = gameState.changeSymbol(playerId, symbol);
    }

    void ready(PlayerId playerId) {
        gameState = gameState.ready(playerId);
    }

    void notReady(PlayerId playerId) {
        gameState = gameState.notReady(playerId);
    }

    void move(PlayerId playerId, Coordinates coords) {
        gameState = gameState.move(playerId, coords);
    }

    void rematch(PlayerId playerId) {
        gameState = gameState.rematch(playerId);
    }
}
