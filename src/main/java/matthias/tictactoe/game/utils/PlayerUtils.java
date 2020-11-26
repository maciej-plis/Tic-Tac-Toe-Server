package matthias.tictactoe.game.utils;

import matthias.tictactoe.game.events.GameEvent;
import matthias.tictactoe.game.events.GameEventFactory;
import matthias.tictactoe.game.model.Player;

import java.util.Collection;
import java.util.function.Consumer;

public class PlayerUtils {

    public static boolean areEveryoneReadyForRematch(Collection<Player> players) {
        for(Player player : players) {
            if(!player.isReadyForRematch()) {
                return false;
            }
        }

        return true;
    }

    public static void untagRematchForEveryone(Collection<Player> players, Consumer<GameEvent> eventCallback) {
        for(Player player : players) {
            player.readyForRematch(false);
            eventCallback.accept(GameEventFactory.createPlayerRequestedRematchEvent(player));
        }
    }

    public static void resetScoreForEveryone(Collection<Player> players, Consumer <GameEvent> eventCallback) {
        for(Player player : players) {
            player.resetScore();
            eventCallback.accept(GameEventFactory.createPlayerWonEvent(player));
        }
    }
}
