package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.usecase.ConsumerUseCase;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static matthias.tictactoe.tictactoe_game.game_room.DeleteGameRoomUseCase.DeleteGameRoomCommand;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeleteGameRoomUseCase implements ConsumerUseCase<DeleteGameRoomCommand> {

    private final GameRoomManager gameRoomManager;

    @Override
    public void accept(DeleteGameRoomCommand cmd) {
        log.info("Deleting game room with command: {}", cmd);

        final var gameRoom = gameRoomManager.findById(cmd.gameRoomId()).orElseThrow(() -> {
            log.info("Game room with user '{}' not found", cmd.gameRoomId());
            return new RuntimeException("Game room not found");
        });

        log.info("Deleting game room with user '{}'", gameRoom.getId());
        gameRoomManager.deleteById(gameRoom.getId());
    }

    public record DeleteGameRoomCommand(
        UUID userId,
        UUID gameRoomId
    ) {
    }
}
