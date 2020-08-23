package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GameStatus {
    private Status status;

    public GameStatus() {
        this.status = Status.NOT_ENOUGH_PLAYERS;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        GameEventPublisher.publishGameStatusChangedEvent(this);
    }

    public boolean hasStatus(Status status) {
        return this.status == status;
    }
}
