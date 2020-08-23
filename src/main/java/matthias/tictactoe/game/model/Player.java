package matthias.tictactoe.game.model;

import lombok.Getter;

@Getter
public class Player {

    PlayerSymbol symbol;
    String name;
    boolean readyForRematch;

    public Player(PlayerSymbol symbol, String name) {
        this.symbol = symbol;
        this.name = name;
        this.readyForRematch = false;
    }

    public void rematchReady(boolean ready) {
        this.readyForRematch = ready;
    }
}
