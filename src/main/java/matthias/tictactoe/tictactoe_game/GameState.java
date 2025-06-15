package matthias.tictactoe.tictactoe_game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import matthias.tictactoe.tictactoe_game.exception.IllegalStateActionException;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.synchronizedSet;
import static java.util.Objects.isNull;

@Getter
abstract class GameState {

    protected final Board board;
    protected final EnumMap<Symbol, UserId> players;
    protected Symbol symbolTurn = Symbol.X;

    GameState() {
        this(new Board(3), new EnumMap<>(Symbol.class));
    }

    public GameState(Board board, EnumMap<Symbol, UserId> players) {
        this.board = board.deepClone();
        this.players = new EnumMap<>(players);
    }

    GameState(GameState gameState) {
        this(gameState.board, gameState.players);
        this.symbolTurn = gameState.symbolTurn;
    }

    GameState join(UserId userId) {
        throw new IllegalStateActionException();
    }

    GameState leave(UserId userId) {
        synchronized (players) {
            if (isNotThisGamePlayer(userId)) return this;
            players.remove(Symbol.X, userId);
            players.remove(Symbol.O, userId);
            return this instanceof AwaitingPlayers ? this : new AwaitingPlayers(this);
        }
    }

    GameState changeSymbol(UserId userId, Symbol symbol) {
        throw new IllegalStateActionException();
    }

    GameState ready(UserId userId) {
        throw new IllegalStateActionException();
    }

    GameState notReady(UserId userId) {
        throw new IllegalStateActionException();
    }

    GameState move(UserId userId, Coordinates coords) {
        throw new IllegalStateActionException();
    }

    GameState rematch(UserId userId) {
        throw new IllegalStateActionException();
    }

    protected boolean isThisGamePlayer(UserId userId) {
        return players.containsValue(userId);
    }

    protected boolean isNotThisGamePlayer(UserId userId) {
        return !isThisGamePlayer(userId);
    }

    protected Symbol getPlayerSymbol(UserId userId) {
        return players.keySet().stream()
            .filter(symbol -> players.get(symbol).equals(userId))
            .findAny()
            .orElseThrow(IllegalStateException::new);
    }
}

@NoArgsConstructor
final class AwaitingPlayers extends GameState {

    AwaitingPlayers(GameState gameState) {
        super(gameState);
    }

    @Override
    GameState join(UserId userId) {
        synchronized (players) {
            if (isThisGamePlayer(userId)) return this;
            if ((addPlayer(Symbol.X, userId) || addPlayer(Symbol.O, userId)) && isGameFull()) {
                return new AwaitingPlayerReadiness(this);
            }
        }
        return this;
    }

    @Override
    GameState changeSymbol(UserId userId, Symbol symbol) {
        synchronized (players) {
            if (isNotThisGamePlayer(userId)) return this;
            players.remove(Symbol.X, userId);
            players.remove(Symbol.O, userId);
            players.put(symbol, userId);
        }
        return this;
    }

    private boolean addPlayer(Symbol symbol, UserId userId) {
        return isNull(players.putIfAbsent(symbol, userId));
    }

    private boolean isGameFull() {
        return players.size() == Symbol.values().length;
    }
}

final class AwaitingPlayerReadiness extends GameState {

    final Set<UserId> playersReady = synchronizedSet(new HashSet<>());

    AwaitingPlayerReadiness(GameState gameState) {
        super(gameState);
    }

    @Override
    GameState ready(UserId userId) {
        if (isNotThisGamePlayer(userId)) return this;
        playersReady.add(userId);
        return allPlayersReady() ? new InProgress(this) : this;
    }

    @Override
    GameState notReady(UserId userId) {
        playersReady.remove(userId);
        return this;
    }

    private boolean allPlayersReady() {
        return playersReady.size() == Symbol.values().length;
    }
}

final class InProgress extends GameState {

    InProgress(GameState gameState) {
        super(gameState);
    }

    @Override
    GameState move(UserId userId, Coordinates coords) {
        final var boardCoords = board.toBoardCoordinates(coords);
        final var playerSymbol = getPlayerSymbol(userId);
        synchronized (board) {
            if (isNotPlayerTurn(userId)) return this;
            board.set(boardCoords, playerSymbol);
            if (board.hasResult()) return new Finished(this);
            changeSymbolTurn();
        }
        return this;
    }

    private boolean isNotPlayerTurn(UserId userId) {
        return getPlayerSymbol(userId) != symbolTurn;
    }

    private void changeSymbolTurn() {
        this.symbolTurn = symbolTurn == Symbol.X ? Symbol.O : Symbol.X;
    }
}

final class Finished extends GameState {

    final Set<UserId> playersRematch = synchronizedSet(new HashSet<>());

    Finished(GameState gameState) {
        super(gameState);
    }

    @Override
    GameState rematch(UserId userId) {
        if (isNotThisGamePlayer(userId)) return this;
        playersRematch.add(userId);

        final var cleanState = new GameState(new Board(3), this.players) {
        };

        return allPlayersRematch() ? new InProgress(cleanState) : this;
    }

    private boolean allPlayersRematch() {
        return playersRematch.size() == Symbol.values().length;
    }
}
