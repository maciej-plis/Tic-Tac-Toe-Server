package matthias.tictactoe.web.game.controllers;

import lombok.AllArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.web.game.exceptions.GameNotFoundException;
import matthias.tictactoe.web.game.services.GamesManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Point;
import java.security.Principal;
import java.util.Map;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "${client.url}")
public class GameController {
    private final GamesManager gamesManager;

    @PostMapping("/games/{gameID}/join")
    public ResponseEntity<?> joinGame(@PathVariable String gameID, Principal principal) {
        TicTacToeGame game = gamesManager.getGame(gameID);
        game.join(principal.getName());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/games/{gameID}/leave")
    public ResponseEntity<?> leaveGame(@PathVariable String gameID, Principal principal) {
        TicTacToeGame game = gamesManager.getGame(gameID);
        game.leave(principal.getName());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/games/{gameID}/mark")
    public ResponseEntity<?> makeMove(@PathVariable String gameID, Principal principal, @RequestBody Point point) {
        TicTacToeGame game = gamesManager.getGame(gameID);

        game.markSquare(principal.getName(), point);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/games/{gameID}/rematch")
    public ResponseEntity<?> rematch(@PathVariable String gameID, Principal principal) {
        TicTacToeGame game = gamesManager.getGame(gameID);

        game.rematch(principal.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/games/{gameID}")
    public Map<String, Object> getGameData(@PathVariable String gameID) {
        TicTacToeGame game = gamesManager.getGame(gameID);
        return game.getGameData();
    }

    @ExceptionHandler({GameException.class})
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<?> handleException(GameNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
