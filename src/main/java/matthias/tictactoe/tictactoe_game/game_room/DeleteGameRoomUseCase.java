package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.usecase.ConsumerUseCase;
import matthias.tictactoe.tictactoe_game.game_room.command.DeleteGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.event.GameRoomDeletedEvent;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class DeleteGameRoomUseCase implements ConsumerUseCase<DeleteGameRoomCommand> {

    private final GameRoomManager gameRoomManager;
    private final GameRoomMessagePublisher messagePublisher;

    @Override
    public void accept(DeleteGameRoomCommand cmd) {
        log.info("Deleting game room with command: {}", cmd);

        final var gameRoom = gameRoomManager.findById(cmd.gameRoomId()).orElseThrow(() -> {
            log.info("Game room with user '{}' not found", cmd.gameRoomId());
            return new RuntimeException("Game room not found");
        });

        log.info("Deleting game room with user '{}'", gameRoom.getId());
        gameRoomManager.deleteById(gameRoom.getId());

        messagePublisher.publish(new GameRoomDeletedEvent(gameRoom.getId()));
    }
}
