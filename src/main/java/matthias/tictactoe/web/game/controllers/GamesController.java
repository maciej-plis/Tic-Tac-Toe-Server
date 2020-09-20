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
@CrossOrigin
public class GamesController {
    private final GamesManager gamesManager;

    @GetMapping("/games")
    public Collection<GameDto> getGames() {
        return GamesParser.parseGamesToGamesDtos(gamesManager.getGames());
    }

    @PostMapping("/games")
    public void createGame(@RequestBody String roomID) {
        gamesManager.createNewGame(roomID);
    }

    @DeleteMapping("/games")
    public void removeGame(@RequestBody String roomID) {
        gamesManager.removeGame(roomID);
    }

    @ExceptionHandler({GameCreationException.class})
    public ResponseEntity handleException(GameCreationException e) {
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler({GameNotFoundException.class})
    public ResponseEntity handleException(GameNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
