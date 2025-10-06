package matthias.tictactoe.tictactoe_game.game_room.event;

import matthias.tictactoe.shared.event.Event;

import java.util.UUID;

public record GameRoomCreatedEvent(
    UUID gameRoomId,
    String gameRoomName
) implements Event {
}
