package matthias.tictactoe.web.controllers;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class GameAccessController {

    private final TicTacToeGame game;

    @PostMapping("/join")
    public ResponseEntity<?> joinGame(@RequestBody String name, HttpSession session) {

        Player player = new Player(session.getId(), name);

        try {
            game.join(player);
        } catch(Exception e) {
            return failureResp(e);
        }

        return successResp("You joined the game");
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveGame(HttpSession session) {

        Player player = new Player(session.getId());

        try {
            game.leave(player);
        } catch(Exception e) {
            return failureResp(e);
        }

        return successResp("You left the game");
    }

    private ResponseEntity<?> successResp(String message) {
        Map<String, String> payload = new HashMap<>();

        payload.put("message", message);
        payload.put("success", "true");

        return ResponseEntity.ok(payload);
    }

    private ResponseEntity<?> failureResp(Exception e) {
        Map<String, String> payload = new HashMap<>();

        payload.put("message", e.getMessage());
        payload.put("success", "false");

        return ResponseEntity.ok(payload);
    }

}
