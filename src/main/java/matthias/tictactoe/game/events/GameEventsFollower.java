package matthias.tictactoe.game.events;

public interface GameEventsFollower {
    void eventOccurred(String gameId, GameEvent event);
}
