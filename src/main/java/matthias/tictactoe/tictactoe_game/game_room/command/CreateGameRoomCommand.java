package matthias.tictactoe.tictactoe_game.game_room.command;

import matthias.tictactoe.shared.command.Command;

import java.util.UUID;

public record CreateGameRoomCommand(
    UUID userId,
    String name,
    boolean spectatingEnabled
) implements Command<UUID> {
}
