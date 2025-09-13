package matthias.tictactoe.tictactoe_game;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.tictactoe_game.command.Command;
import matthias.tictactoe.tictactoe_game.command.CreateGameRoomCommand;
import matthias.tictactoe.tictactoe_game.command.GameRoomCommand;
import matthias.tictactoe.tictactoe_game.dto.GameRoomDTO;
import matthias.tictactoe.tictactoe_game.events.GameRoomCreatedEvent;
import matthias.tictactoe.tictactoe_game.events.GameRoomsEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class TicTacToeGameService {

    private final Map<UUID, GameRoom> gameRooms = new HashMap<>();
    private final EventPublisher<GameRoomsEvent> gameRoomsEventPublisher = new EventPublisher<>();

    public void resolveCommand(Command cmd) {
        switch (cmd) {
            case CreateGameRoomCommand c -> onCreateGameRoom(c);
            case GameRoomCommand c -> onGameRoomCommand(c);
            default -> throw new RuntimeException("Unknown command.");
        }
    }

    GameRoomDTO getGameRoom(UUID gameRoomId) {
        final var gameRoom = getGameRoomOrThrow(gameRoomId);
        return gameRoom.toDTO();
    }

    private void onCreateGameRoom(CreateGameRoomCommand cmd) {
        final var gameRoomId = UUID.randomUUID();
        gameRooms.put(gameRoomId, new GameRoom());
        gameRoomsEventPublisher.publishEvent(new GameRoomCreatedEvent());
        cmd.resultHandler().accept(gameRoomId);
    }

    private void onGameRoomCommand(GameRoomCommand cmd) {
        final var gameRoom = getGameRoomOrThrow(cmd.gameRoomId());
        gameRoom.resolveCommand(cmd);
    }

    private GameRoom getGameRoomOrThrow(UUID gameRoomId) {
        return ofNullable(gameRooms.get(gameRoomId)).orElseThrow(() -> new RuntimeException("Game room not found."));
    }
}
