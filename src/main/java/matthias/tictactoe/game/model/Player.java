package matthias.tictactoe.game.model;

import lombok.Getter;

@Getter
public class Player {

    PlayerSymbol symbol;
    String name;
    boolean isReady;

    public Player(PlayerSymbol symbol, String name) {
        this.symbol = symbol;
        this.name = name;
        this.isReady = false;
    }

    public void ready() {
        this.isReady = true;
    }

    public void notReady() {
        this.isReady = false;
    }
}
