package matthias.tictactoe.tictactoe_game.game_room.endpoint.request;

public record CreateGameRoomRequest(
    String gameRoomName,
    Boolean spectatingEnabled,
    String password
) {
}
