package matthias.tictactoe.tictactoe_game.command;

import java.util.UUID;

public interface GameRoomCommand extends Command {
    UUID gameRoomId();
}
