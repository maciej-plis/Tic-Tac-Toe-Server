package matthias.tictactoe.game;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.events.GameEventsFollower;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.game.states.GameState;
import matthias.tictactoe.game.states.NotEnoughPlayersGameState;
import matthias.tictactoe.web.game.services.GameStateEmitter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TicTacToeGame {
    private final List<GameEventsFollower> followers = new ArrayList<>();
    private GameState state;
    private GameStateEmitter gameStateEmitter;

    private final GamePlayerManager playersManager;

    public TicTacToeGame(GameStateEmitter gameStateEmitter) {
        this.gameStateEmitter = gameStateEmitter;
        this.playersManager = new GamePlayerManager(this::newEvent);
        this.state = new NotEnoughPlayersGameState(this);
    }

    public void join(String name) {
        this.state.join(name);
    }

    public void leave(String name) {
        verifyAccess(name);
        this.state.leave(name);
    }

    public void markSquare(String name, Point point) {
        verifyAccess(name);
        this.state.mark(name, point);
    }

    public void rematch(String name) {
        verifyAccess(name);
        this.state.rematch(name);
    }

    public void followGameEvents(GameEventsFollower follower) {
        followers.add(follower);
    }

    public void newEvent(GameEvent event) {
        for(GameEventsFollower follower : followers) {
            follower.eventOccurred(event);
        }
        gameStateEmitter.emitGameEvent(event);
    }

    public Map<String, Object> getInitialGameData() {
        return state.getGameData();
    }

    public GamePlayerManager getPlayersManager() {
        return playersManager;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    private void verifyAccess(String name) {
        if(!playersManager.containsPlayer(name)) {
            throw new AccessDeniedException("You are not allowed to do that.");
        }
    }
}
