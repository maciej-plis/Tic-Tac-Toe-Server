package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.usecase.FunctionUseCase;
import matthias.tictactoe.tictactoe_game.game_room.command.CreateGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.event.GameRoomCreatedEvent;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import matthias.tictactoe.user.UserFacade;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
class CreateGameRoomUseCase implements FunctionUseCase<CreateGameRoomCommand, UUID> {

    private final GameRoomManager gameRoomManager;
    private final GameRoomMessagePublisher messagePublisher;
    private final UserFacade userFacade;

    @Override
    public UUID apply(CreateGameRoomCommand cmd) {
        log.info("Creating game room with command: {}", cmd);
        final var gameRoom = new GameRoom(messagePublisher, userFacade, cmd.name(), cmd.spectatingEnabled());

        final var user = userFacade.getUserOrThrow(cmd.userId());

        log.info("Adding player to created game room: {}", cmd.userId());
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), user.id()));

        log.info("Saving created game room");
        gameRoomManager.save(gameRoom);

        messagePublisher.publish(new GameRoomCreatedEvent(gameRoom.getId(), gameRoom.getName()));

        return gameRoom.getId();
    }
}
