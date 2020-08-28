package matthias.tictactoe.game.utils;

import matthias.tictactoe.game.model.Player;

import java.util.Collection;

public class PlayerUtils {

    public static boolean areEveryoneReadyForRematch(Collection<Player> players) {
        for(Player player : players) {
            if(!player.isReadyForRematch()) {
                return false;
            }
        }

        return true;
    }

    public static void untagRematchForEveryone(Collection<Player> players) {
        for(Player player : players) {
            player.readyForRematch(false);
        }
    }
}
