package matthias.tictactoe.tictactoe_game;

public interface GameRoomListener {
    default void onPlayerJoined(GameRoomId roomId, UserId userId) {}
    default void onPlayerLeft(GameRoomId roomId, UserId userId) {}
    default void onSpectatorJoined(GameRoomId roomId, UserId userId) {}
    default void onSpectatorLeft(GameRoomId roomId, UserId userId) {}
    default void onSymbolChanged(GameRoomId roomId, UserId userId, Symbol symbol) {}
    default void onPlayerReady(GameRoomId roomId, UserId userId) {}
    default void onPlayerNotReady(GameRoomId roomId, UserId userId) {}
    default void onMoveMade(GameRoomId roomId, UserId userId, Coordinates coords) {}
    default void onRematchRequested(GameRoomId roomId, UserId userId) {}
}

