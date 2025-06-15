package matthias.tictactoe.tictactoe_game;

import lombok.Getter;

import static java.util.UUID.randomUUID;

class Game {

    @Getter
    private final GameId gameId = new GameId(randomUUID());
    private GameState gameState = new AwaitingPlayers();

    public void join(UserId userId) {
        gameState = gameState.join(userId);
    }

    public void leave(UserId userId) {
        gameState = gameState.leave(userId);
    }

    public void changeSymbol(UserId userId, Symbol symbol) {
        gameState = gameState.changeSymbol(userId, symbol);
    }

    public void ready(UserId userId) {
        gameState = gameState.ready(userId);
    }

    public void notReady(UserId userId) {
        gameState = gameState.notReady(userId);
    }

    public void move(UserId userId, Coordinates coords) {
        gameState = gameState.move(userId, coords);
    }

    public void rematch(UserId userId) {
        gameState = gameState.rematch(userId);
    }

    public boolean hasPlayer(UserId userId) {
        return this.gameState.isThisGamePlayer(userId);
    }
}
