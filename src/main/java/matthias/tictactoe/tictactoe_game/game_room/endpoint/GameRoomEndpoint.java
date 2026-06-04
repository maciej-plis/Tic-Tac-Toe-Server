package matthias.tictactoe.tictactoe_game.game_room.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.AuthenticatedUserProvider;
import matthias.tictactoe.tictactoe_game.game_room.GameRoomCommandHandler;
import matthias.tictactoe.tictactoe_game.game_room.GameRoomQueryService;
import matthias.tictactoe.tictactoe_game.game_room.command.CreateGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.DeleteGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.UpdateGameRoomCommand;
import matthias.tictactoe.tictactoe_game.game_room.endpoint.dto.BasicGameRoomDto;
import matthias.tictactoe.tictactoe_game.game_room.endpoint.dto.GameRoomDto;
import matthias.tictactoe.tictactoe_game.game_room.endpoint.request.CreateGameRoomRequest;
import matthias.tictactoe.tictactoe_game.game_room.endpoint.request.UpdateGameRoomRequest;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
class GameRoomEndpoint {

    private final GameRoomCommandHandler gameRoomCommandHandler;
    private final GameRoomQueryService gameRoomQueryService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping("/game-rooms/search")
    public ResponseEntity<PagedModel<BasicGameRoomDto>> searchGameRooms(
        Pageable pageable,
        String gameRoomNameQuery
    ) {
        final var gameRooms = new PagedModel<>(gameRoomQueryService.searchGameRooms(pageable, gameRoomNameQuery));
//        return ok(gameRooms);
        return ok(null);
    }

    @GetMapping("/game-rooms/{gameRoomId}")
    public ResponseEntity<GameRoomDto> getGameRoom(
        @PathVariable UUID gameRoomId
    ) {
        try {
            final var gameRoom = gameRoomQueryService.getGameRoomById(gameRoomId);
//            return ok(gameRoom);
            return ok(null);
        } catch (GameRoomNotFoundException ex) {
            throw new ErrorResponseException(NOT_FOUND, ex);
        }
    }

    @PostMapping("/game-rooms")
    public ResponseEntity<UUID> createGameRoom(
        @RequestBody @Valid CreateGameRoomRequest request
    ) {
        final var gameRoomId = gameRoomCommandHandler.handle(new CreateGameRoomCommand(
            authenticatedUserProvider.getAuthenticatedUser().id(),
            request.gameRoomName(),
            request.spectatingEnabled()
        ));
        return ok(gameRoomId);
    }

    @PostMapping("/game-rooms/{gameRoomId}/join")
    public ResponseEntity<Void> joinGameRoom(
        @PathVariable UUID gameRoomId

    ) {
        gameRoomCommandHandler.handle(new PlayerJoinCommand(
            gameRoomId,
            authenticatedUserProvider.getAuthenticatedUser().id()
        ));
        return noContent().build();
    }

    @PutMapping("/game-rooms/{gameRoomId}")
    public ResponseEntity<Void> updateGameRoom(
        @PathVariable UUID gameRoomId,
        @RequestBody UpdateGameRoomRequest request
    ) {
        try {
            gameRoomCommandHandler.handle(new UpdateGameRoomCommand(
                gameRoomId,
                authenticatedUserProvider.getAuthenticatedUser().id(),
                request.gameRoomName(),
                request.spectatingEnabled()
            ));
            return noContent().build();
        } catch (GameRoomNotFoundException ex) {
            throw new ErrorResponseException(NOT_FOUND, ex);
        }
    }

    @DeleteMapping("/game-rooms/{gameRoomId}")
    public ResponseEntity<Void> deleteGameRoom(
        @PathVariable UUID gameRoomId
    ) {
        try {
            gameRoomCommandHandler.handle(new DeleteGameRoomCommand(
                authenticatedUserProvider.getAuthenticatedUser().id(),
                gameRoomId
            ));
            return noContent().build();
        } catch (GameRoomNotFoundException ex) {
            throw new ErrorResponseException(NOT_FOUND, ex);
        }
    }
}
