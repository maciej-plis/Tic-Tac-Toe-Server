package matthias.tictactoe.web.game.controllers;

import lombok.AllArgsConstructor;
import matthias.tictactoe.web.game.dtos.GameDto;
import matthias.tictactoe.web.game.exceptions.GameCreationException;
import matthias.tictactoe.web.game.exceptions.GameNotFoundException;
import matthias.tictactoe.web.game.services.GamesManager;
import matthias.tictactoe.web.game.utils.GamesParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "${client.url}", allowCredentials = "true")
public class GamesController {
    private final GamesManager gamesManager;

    @GetMapping("/games")
    public Collection<GameDto> getGames() {
        return GamesParser.parseGamesToGamesDtos(gamesManager.getGames());
    }

    @PostMapping("/games")
    public ResponseEntity<String> createGame(@RequestBody String name) {
        try {
            gamesManager.createNewGame(name);
        } catch(GameCreationException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Game created successfully");
    }

    @DeleteMapping("/games/{gameID}")
    public ResponseEntity<String> removeGame(@PathVariable String gameID) {
        try {
            gamesManager.removeGame(gameID);
        } catch(GameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Game removed successfully");
    }
}
