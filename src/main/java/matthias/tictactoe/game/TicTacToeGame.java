package matthias.tictactoe.game;

import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.model.dto.InitialGameData;
import matthias.tictactoe.game.services.GameEventPublisher;
import matthias.tictactoe.game.services.GamePlayerManager;
import matthias.tictactoe.game.states.GameState;
import matthias.tictactoe.game.states.NotEnoughPlayersGameState;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class TicTacToeGame {
    private final GamePlayerManager playersManager;
    private GameState state;
    private GameEventPublisher eventPublisher;

    public TicTacToeGame(GamePlayerManager playersManager,
                         ApplicationContext context,
                         GameEventPublisher eventPublisher) {
        this.playersManager = playersManager;
        this.state = context.getBean(NotEnoughPlayersGameState.class, this);
        this.eventPublisher = eventPublisher;
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

    public InitialGameData getInitialGameData() {
        return InitialGameData.builder()
                .players(playersManager.getPlayers())
                .state(state.getType())
                .build();
    }

    public GamePlayerManager getPlayersManager() {
        return playersManager;
    }

    public void setState(GameState state) {
        this.state = state;
        this.eventPublisher.publishGameStatusChangedEvent(state.getType());
    }

    private void verifyAccess(String name) {
        if(!playersManager.containsPlayer(name)) {
            throw new AccessDeniedException("You are not allowed to do that.");
        }
    }
}
