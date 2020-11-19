package matthias.tictactoe.game;

import lombok.Getter;
import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventsFollower;
import matthias.tictactoe.game.model.ActiveSymbol;
import matthias.tictactoe.game.model.GameBoard;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.game.states.GameState;
import matthias.tictactoe.game.states.NotEnoughPlayersGameState;
import matthias.tictactoe.game.utils.RandomIdGenerator;
import org.springframework.security.access.AccessDeniedException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class TicTacToeGame {
    private final List<GameEventsFollower> followers = new ArrayList<>();
    private GameState state;

    private final String id;
    private final String name;

    private final GamePlayerManager playersManager;
    private GameBoard board;
    private ActiveSymbol active;

    public TicTacToeGame(String name) {
        this.id = RandomIdGenerator.getBase62Id(10);
        this.name = name;

        this.playersManager = new GamePlayerManager(this::newEvent);
        this.board = new GameBoard(this::newEvent);
        this.active = new ActiveSymbol(this::newEvent);

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
            follower.eventOccurred(this.id, event);
        }
    }

    public Map<String, Object> getGameData() {
        return state.getGameData();
    }

    public GamePlayerManager getPlayersManager() {
        return playersManager;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    private void verifyAccess(String name) {
        if(!playersManager.containsPlayer(name)) {
            throw new AccessDeniedException("You are not allowed to do that.");
        }
    }
}
