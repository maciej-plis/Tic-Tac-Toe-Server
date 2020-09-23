package matthias.tictactoe.web.game.controllers;

import lombok.AllArgsConstructor;
import matthias.tictactoe.web.game.dtos.GameDto;
import matthias.tictactoe.web.game.exceptions.GameCreationException;
import matthias.tictactoe.web.game.exceptions.GameNotFoundException;
import matthias.tictactoe.web.game.services.GamesManager;
import matthias.tictactoe.web.game.utils.GamesParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class GamesController {
    private final GamesManager gamesManager;

    @GetMapping("/games")
    public Collection<GameDto> getGames() {
        return GamesParser.parseGamesToGamesDtos(gamesManager.getGames());
    }

    @PostMapping("/games")
    public void createGame(@RequestBody String name) {
        gamesManager.createNewGame(name);
    }

    @DeleteMapping("/games/{gameID}")
    public void removeGame(@PathVariable String gameID) {
        gamesManager.removeGame(gameID);
    }

    @ExceptionHandler({GameCreationException.class})
    public ResponseEntity<?> handleException(GameCreationException e) {
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity<?> handleException(GameNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
