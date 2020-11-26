package matthias.tictactoe.game.model;

import lombok.Getter;

@Getter
public class Player {

    PlayerSymbol symbol;
    String name;
    int score;
    boolean readyForRematch;

    public Player(PlayerSymbol symbol, String name) {
        this.symbol = symbol;
        this.name = name;
        this.score = 0;
        this.readyForRematch = false;
    }

    public void win() {
        this.score++;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void readyForRematch(boolean ready) {
        this.readyForRematch = ready;
    }
}
