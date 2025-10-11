package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.command.Command;
import matthias.tictactoe.shared.command.CommandHandler;
import matthias.tictactoe.tictactoe_game.game_room.command.CreateGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.DeleteGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.GameRoomCommand;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GameRoomCommandHandler implements CommandHandler {

    private final GameRoomManager manager;

    private final CreateGameRoomUseCase createGameRoomUseCase;
    private final DeleteGameRoomUseCase deleteGameRoomUseCase;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(Command<T> cmd) {
        return (T) switch(cmd) {
            case CreateGameRoomCommand c -> handle(c, createGameRoomUseCase);
            case DeleteGameRoomCommand c -> handle(c, deleteGameRoomUseCase);
            case GameRoomCommand<?> c -> handle(c, this::handleGameRoomCommand);
            default -> throw new IllegalArgumentException("Unknown command " + cmd.getClass().getSimpleName() + ".");
        };
    }

    private <T> T handleGameRoomCommand(GameRoomCommand<T> cmd) {
        log.info("Handling game room command: {}", cmd);
        return manager.getById(cmd.gameRoomId()).handle(cmd);
    }
}
