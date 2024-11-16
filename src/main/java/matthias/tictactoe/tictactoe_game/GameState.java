package matthias.tictactoe.tictactoe_game;

import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.synchronizedSet;
import static java.util.Objects.isNull;

abstract class GameState {

    protected final Board board;
    protected final EnumMap<Symbol, PlayerId> players;
    protected Symbol symbolTurn = Symbol.X;

    GameState() {
        this(new Board(3), new EnumMap<>(Symbol.class));
    }

    public GameState(Board board, EnumMap<Symbol, PlayerId> players) {
        this.board = board.deepClone();
        this.players = new EnumMap<>(players);
    }

    GameState(GameState gameState) {
        this(gameState.board, gameState.players);
        this.symbolTurn = gameState.symbolTurn;
    }

    GameState join(PlayerId playerId) {
        throw new IllegalStateActionException();
    }

    GameState leave(PlayerId playerId) {
        synchronized (players) {
            if (isNotThisGamePlayer(playerId)) return this;
            players.remove(Symbol.X, playerId);
            players.remove(Symbol.O, playerId);
            return this instanceof AwaitingPlayers ? this : new AwaitingPlayers(this);
        }
    }

    GameState changeSymbol(PlayerId playerId, Symbol symbol) {
        throw new IllegalStateActionException();
    }

    GameState ready(PlayerId playerId) {
        throw new IllegalStateActionException();
    }

    GameState notReady(PlayerId playerId) {
        throw new IllegalStateActionException();
    }

    GameState move(PlayerId playerId, Coordinates coords) {
        throw new IllegalStateActionException();
    }

    GameState rematch(PlayerId playerId) {
        throw new IllegalStateActionException();
    }

    protected boolean isThisGamePlayer(PlayerId playerId) {
        return players.containsValue(playerId);
    }

    protected boolean isNotThisGamePlayer(PlayerId playerId) {
        return !isThisGamePlayer(playerId);
    }

    protected Symbol getPlayerSymbol(PlayerId playerId) {
        return players.keySet().stream()
            .filter(symbol -> players.get(symbol).equals(playerId))
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
    GameState join(PlayerId playerId) {
        synchronized (players) {
            if (isThisGamePlayer(playerId)) return this;
            if ((addPlayer(Symbol.X, playerId) || addPlayer(Symbol.O, playerId)) && isGameFull()) {
                return new AwaitingPlayerReadiness(this);
            }
        }
        return this;
    }

    @Override
    GameState changeSymbol(PlayerId playerId, Symbol symbol) {
        synchronized (players) {
            if (isNotThisGamePlayer(playerId)) return this;
            players.remove(Symbol.X, playerId);
            players.remove(Symbol.O, playerId);
            players.put(symbol, playerId);
        }
        return this;
    }

    private boolean addPlayer(Symbol symbol, PlayerId playerId) {
        return isNull(players.putIfAbsent(symbol, playerId));
    }

    private boolean isGameFull() {
        return players.size() == Symbol.values().length;
    }
}

final class AwaitingPlayerReadiness extends GameState {

    final Set<PlayerId> playersReady = synchronizedSet(new HashSet<>());

    AwaitingPlayerReadiness(GameState gameState) {
        super(gameState);
    }

    @Override
    GameState ready(PlayerId playerId) {
        if (isNotThisGamePlayer(playerId)) return this;
        playersReady.add(playerId);
        return allPlayersReady() ? new InProgress(this) : this;
    }

    @Override
    GameState notReady(PlayerId playerId) {
        playersReady.remove(playerId);
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
    GameState move(PlayerId playerId, Coordinates coords) {
        final var boardCoords = board.toBoardCoordinates(coords);
        final var playerSymbol = getPlayerSymbol(playerId);
        synchronized (board) {
            if (isNotPlayerTurn(playerId)) return this;
            board.set(boardCoords, playerSymbol);
            if (board.hasResult()) return new Finished(this);
            changeSymbolTurn();
        }
        return this;
    }

    private boolean isNotPlayerTurn(PlayerId playerId) {
        return getPlayerSymbol(playerId) != symbolTurn;
    }

    private void changeSymbolTurn() {
        this.symbolTurn = symbolTurn == Symbol.X ? Symbol.O : Symbol.X;
    }
}

final class Finished extends GameState {

    final Set<PlayerId> playersRematch = synchronizedSet(new HashSet<>());

    Finished(GameState gameState) {
        super(gameState);
    }

    @Override
    GameState rematch(PlayerId playerId) {
        if (isNotThisGamePlayer(playerId)) return this;
        playersRematch.add(playerId);
        return allPlayersRematch() ? new InProgress(this) : this;
    }

    private boolean allPlayersRematch() {
        return playersRematch.size() == Symbol.values().length;
    }
}
