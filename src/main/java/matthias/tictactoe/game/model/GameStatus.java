package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;

public class GameStatus {
    private Status status;

    public GameStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        GameEventPublisher.publishGameStatusChangedEvent(status);
    }

    public boolean hasStatus(Status status) {
        return this.status == status;
    }
}
