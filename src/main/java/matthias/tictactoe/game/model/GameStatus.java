package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;

import java.util.Objects;

public class GameStatus {
    private  final GameEventPublisher publisher;
    private Status status;

    public GameStatus(Status status, GameEventPublisher publisher) {
        this.status = status;
        this.publisher = publisher;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        publisher.publishGameStatusChangedEvent(status);
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
