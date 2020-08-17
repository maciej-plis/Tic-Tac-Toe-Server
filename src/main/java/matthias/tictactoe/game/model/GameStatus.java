package matthias.tictactoe.game.model;

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
    }
}
