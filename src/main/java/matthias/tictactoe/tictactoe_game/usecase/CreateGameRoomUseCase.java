package matthias.tictactoe.tictactoe_game.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.tictactoe_game.GameRoom;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static matthias.tictactoe.tictactoe_game.usecase.CreateGameRoomUseCase.CreateGameRoomCommand;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateGameRoomUseCase implements FunctionUseCase<CreateGameRoomCommand, UUID> {

    private final GameRoomService gameRoomService;

    @Override
    public UUID apply(CreateGameRoomCommand cmd) {
        log.info("Creating game room with command {}", cmd);

        new GameRoom(cmd.gameRoomName());

        return gameRoomService.createGameRoom(cmd.gameRoomName());
    }

    public record CreateGameRoomCommand(
        UUID userId,
        String gameRoomName
    ) {
    }
}
