package matthias.tictactoe.tictactoe_game;

import java.util.UUID;

public interface GameRoomNotifier {
    default void onPlayerJoined(UUID userId) {}
    default void onPlayerLeft(UUID userId) {}
    default void onPlayerChangedToSpectator(UUID userId) {}
    default void onPlayerSymbolChanged(UUID userId) {}
    default void onSpectatorJoined(UUID userId) {}
    default void onSpectatorLeft(UUID userId) {}
    default void onSpectatorChangedToPlayer(UUID userId) {}
    default void onPlayerReady(UUID userId) {}
    default void onPlayerNotReady(UUID userId) {}
    default void onPlayerMove(UUID userId) {}
    default void onPlayerTurnChanged(UUID userId) {}
    default void onRematchRequest(UUID userId) {}
    default void onRematchRequestCanceled(UUID userId) {}
    default void onGameStarted() {}
    default void onGameFinished() {}
}

