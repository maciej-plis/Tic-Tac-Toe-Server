package matthias.tictactoe.tictactoe_game;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

@Slf4j
class GameRoom {

    @Getter
    private final GameRoomId roomId = new GameRoomId(UUID.randomUUID());
    private final Game game = new Game();
    private final Set<UserId> spectators = ConcurrentHashMap.newKeySet();
    private final Set<GameRoomListener> listeners = new CopyOnWriteArraySet<>();

    public void joinAsPlayer(UserId userId) {
        if (isSpectator(userId)) return;
        leave(userId);
        game.join(userId);
        notifyListeners(l -> l.onPlayerJoined(roomId, userId));
    }

    public void joinAsSpectator(UserId userId) {
        if (isPlayer(userId)) return;
        leave(userId);
        spectators.add(userId);
        notifyListeners(l -> l.onSpectatorJoined(roomId, userId));
    }

    public void leave(UserId userId) {
        if (isPlayer(userId)) {
            game.leave(userId);
            notifyListeners(l -> l.onPlayerLeft(roomId, userId));
        } else if (isSpectator(userId)) {
            spectators.remove(userId);
            notifyListeners(l -> l.onSpectatorLeft(roomId, userId));
        }
    }

    public void changeSymbol(UserId userId, Symbol symbol) {
        if (!isPlayer(userId)) return;
        game.changeSymbol(userId, symbol);
        notifyListeners(l -> l.onSymbolChanged(roomId, userId, symbol));
    }

    public void ready(UserId userId) {
        if (!isPlayer(userId)) return;
        game.ready(userId);
        notifyListeners(l -> l.onPlayerReady(roomId, userId));
    }

    public void notReady(UserId userId) {
        if (!isPlayer(userId)) return;
        game.notReady(userId);
        notifyListeners(l -> l.onPlayerNotReady(roomId, userId));
    }

    public void move(UserId userId, Coordinates coords) {
        if (!isPlayer(userId)) return;
        game.move(userId, coords);
        notifyListeners(l -> l.onMoveMade(roomId, userId, coords));
    }

    public void rematch(UserId userId) {
        if (!isPlayer(userId)) return;
        game.rematch(userId);
        notifyListeners(l -> l.onRematchRequested(roomId, userId));
    }

    public boolean isPlayer(UserId userId) {
        return game.hasPlayer(userId);
    }

    public boolean isSpectator(UserId userId) {
        return spectators.contains(userId);
    }

    public void addListener(GameRoomListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameRoomListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(Consumer<GameRoomListener> notify) {
        for (var listener : listeners) {
            try {
                notify.accept(listener);
            } catch (Exception ex) {
                log.error("Exception occurred while notifying listener", ex);
            }
        }
    }
}