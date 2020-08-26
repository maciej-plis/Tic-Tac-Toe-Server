package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GameStatus {
    private final GameEventPublisher gameEventPublisher;
    private Status status;

    public GameStatus(GameEventPublisher gameEventPublisher) {
        this.gameEventPublisher = gameEventPublisher;
        this.status = Status.NOT_ENOUGH_PLAYERS;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        gameEventPublisher.publishGameStatusChangedEvent(this);
    }

    public boolean hasStatus(Status status) {
        return this.status == status;
    }
}
