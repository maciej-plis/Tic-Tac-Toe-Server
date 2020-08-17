package matthias.tictactoe.game.model;

import matthias.tictactoe.game.services.GameEventPublisher;

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
}
