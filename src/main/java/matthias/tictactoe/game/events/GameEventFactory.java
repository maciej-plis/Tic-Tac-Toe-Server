package matthias.tictactoe.game.events;

import matthias.tictactoe.game.model.Player;

public class GameEventFactory {

    public static GameEvent createPlayerJoinedEvent(Player player) {
        return GameEvent.builder()
                        .eventType(GameEventType.PLAYER_JOINED)
                        .addToPayload("player", player)
                        .build();
    }

    public static GameEvent createPlayerLeftEvent(Player player) {
        return GameEvent.builder()
                .eventType(GameEventType.PLAYER_LEFT)
                .addToPayload("player", player)
                .build();
    }
}
