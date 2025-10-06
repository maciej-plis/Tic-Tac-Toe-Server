package matthias.tictactoe.tictactoe_game.game_room.event;

import matthias.tictactoe.shared.event.Event;

import java.util.UUID;

public record GameRoomDeletedEvent(
    UUID gameRoomId
) implements Event {
}
