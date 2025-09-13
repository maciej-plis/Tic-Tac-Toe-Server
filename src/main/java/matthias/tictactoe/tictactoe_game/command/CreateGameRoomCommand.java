package matthias.tictactoe.tictactoe_game.command;

import java.util.UUID;
import java.util.function.Consumer;

public record CreateGameRoomCommand(
    String roomName,
    Consumer<UUID> resultHandler
) implements Command {
}
