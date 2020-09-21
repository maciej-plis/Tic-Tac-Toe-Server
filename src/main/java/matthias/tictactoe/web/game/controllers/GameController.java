package matthias.tictactoe.web.game.controllers;

import lombok.AllArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.web.authentication.utils.ResponseEntityBuilder;
import matthias.tictactoe.web.game.exceptions.GameNotFoundException;
import matthias.tictactoe.web.game.services.GamesManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.Point;
import java.security.Principal;
import java.util.Map;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class GameController {
    private final GamesManager gamesManager;

    @PostMapping("/games/{gameID}/join")
    public ResponseEntity<?> joinGame(@PathVariable String gameID, Principal principal) {
        TicTacToeGame game = gamesManager.getGame(gameID);
        game.join(principal.getName());

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "Room join success")
                .build();
    }

    @PostMapping("/games/{gameID}/leave")
    public ResponseEntity<?> leaveGame(@PathVariable String gameID, Principal principal) {
        TicTacToeGame game = gamesManager.getGame(gameID);
        game.leave(principal.getName());

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "Room leave success")
                .build();
    }

    @PostMapping("/games/{gameID}/mark")
    public ResponseEntity<?> makeMove(@PathVariable String gameID, Principal principal, @RequestBody Point point) {
        TicTacToeGame game = gamesManager.getGame(gameID);

        game.markSquare(principal.getName(), point);

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", String.format("Field x%d y%d has been marked successfully", point.x, point.y))
                .build();
    }

    @PostMapping("/games/{gameID}/rematch")
    public ResponseEntity<?> rematch(@PathVariable String gameID, Principal principal) {
        TicTacToeGame game = gamesManager.getGame(gameID);

        game.rematch(principal.getName());

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "You are tagged as ready for rematch ")
                .build();
    }

    @GetMapping("/games/{gameID}")
    public Map<String, Object> getGameData(@PathVariable String gameID) {
        TicTacToeGame game = gamesManager.getGame(gameID);
        return game.getInitialGameData();
    }

    @ExceptionHandler({GameException.class})
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", false)
                .addToPayload("message", e.getMessage())
                .build();
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<?> handleException(GameNotFoundException e){
        return ResponseEntity.notFound().build();
    }
}
