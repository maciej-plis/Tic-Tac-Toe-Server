package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerJoinCommand;
import matthias.tictactoe.shared.usecase.FunctionUseCase;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import matthias.tictactoe.user.UserFacade;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static matthias.tictactoe.tictactoe_game.game_room.CreateGameRoomUseCase.CreateGameRoomCommand;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateGameRoomUseCase implements FunctionUseCase<CreateGameRoomCommand, UUID> {

    private final GameRoomManager gameRoomManager;
    private final GameRoomMessagePublisher messagePublisher;
    private final UserFacade userFacade;

    @Override
    public UUID apply(CreateGameRoomCommand cmd) {
        log.info("Creating game room with command: {}", cmd);
        final var gameRoom = new GameRoom(messagePublisher, cmd.gameRoomName());

        final var user = userFacade.getUserOrThrow(cmd.userId());

        log.info("Adding player to created game room: {}", cmd.userId());
        gameRoom.handle(new PlayerJoinCommand(user));

        log.info("Saving created game room");
        gameRoomManager.save(gameRoom);

        return gameRoom.getId();
    }

    public record CreateGameRoomCommand(
        UUID userId,
        String gameRoomName
    ) {
    }
}
