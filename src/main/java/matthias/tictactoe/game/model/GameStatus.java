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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStatus that = (GameStatus) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
