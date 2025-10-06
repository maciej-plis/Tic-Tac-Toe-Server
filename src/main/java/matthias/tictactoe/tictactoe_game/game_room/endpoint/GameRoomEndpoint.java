package matthias.tictactoe.tictactoe_game.game_room.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.tictactoe_game.game_room.command.CreateGameRoomCommand;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
class GameRoomEndpoint {

    @GetMapping("/game-rooms/search")
    public void searchGameRooms(
        Pageable pageable,
        String gameRoomNameQuery
    ) {

    }

    @GetMapping("/game-rooms/{gameRoomId}")
    public void getGameRoom(@PathVariable UUID gameRoomId) {

    }

    @PostMapping("/game-rooms")
    public void createGameRoom(@RequestBody CreateGameRoomCommand cmd) {

    }

//    @PutMapping("/game-rooms/{gameRoomId}")
//    public void updateGameRoom(@PathVariable UUID gameRoomId, @RequestBody UpdateGameRoomCommand cmd) {
//
//    }

//    @DeleteMapping("/game-rooms/{gameRoomId}")
//    public void deleteGameRoom(@PathVariable UUID gameRoomId) {
//
//    }
}
