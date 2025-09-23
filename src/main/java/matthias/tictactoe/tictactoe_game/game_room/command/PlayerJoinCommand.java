package matthias.tictactoe.tictactoe_game.game_room.command;

import matthias.tictactoe.user.dto.UserDto;

import java.util.UUID;

public record PlayerJoinCommand(
    UserDto user
) implements GameRoomCommand<Void> {

    public UUID userId() {
        return user.id();
    }
}
