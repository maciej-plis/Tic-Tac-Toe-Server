package matthias.tictactoe.tictactoe_game.game_room.command;

import matthias.tictactoe.shared.command.Command;

import java.util.UUID;

public interface GameRoomCommand<T> extends Command<T> {
    UUID userId();
    UUID gameRoomId();
}
