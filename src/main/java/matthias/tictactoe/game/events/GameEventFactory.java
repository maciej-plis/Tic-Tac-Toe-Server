package matthias.tictactoe.game.events;

import matthias.tictactoe.game.model.*;

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

    public static GameEvent createStateChangedEvent(StateType state) {
        return GameEvent.builder()
                .eventType(GameEventType.GAME_STATE_CHANGED)
                .addToPayload("state", state)
                .build();
    }

    public static GameEvent createBoardChangedEvent(Symbol[][] board) {
        return GameEvent.builder()
                .eventType(GameEventType.BOARD_CHANGED)
                .addToPayload("board", board)
                .build();
    }

    public static GameEvent createActiveSymbolChangedEvent(PlayerSymbol activeSymbol) {
        return GameEvent.builder()
                .eventType(GameEventType.ACTIVE_SYMBOL_CHANGED)
                .addToPayload("activeSymbol", activeSymbol)
                .build();
    }

    public static GameEvent createPlayerRequestedRematchEvent(Player player) {
        return GameEvent.builder()
                .eventType(GameEventType.PLAYER_REQUESTED_REMATCH)
                .addToPayload("player", player)
                .build();
    }

    public static GameEvent createPlayerWonEvent(Player player) {
        return GameEvent.builder()
                .eventType(GameEventType.PLAYER_WON)
                .addToPayload("player", player)
                .build();
    }

    public static GameEvent createNewMessageEvent(Message message) {
        return GameEvent.builder()
                .eventType(GameEventType.NEW_MESSAGE)
                .addToPayload("message", message)
                .build();
    }

    public static GameEvent createClearChatEvent() {
        return GameEvent.builder()
                .eventType(GameEventType.CLEAR_CHAT)
                .build();
    }
}
