package matthias.tictactoe.tictactoe_game.tictactoe_game.command;

import matthias.tictactoe.shared.command.Command;

import java.util.UUID;

public interface GameCommand<T> extends Command<T> {
    UUID userId();
}
