package matthias.tictactoe.web.controllers;

import lombok.AllArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@CrossOrigin
public class GameController {

    private final TicTacToeGame game;

    @PostMapping("/mark")
    public ResponseEntity<?> makeMove(HttpSession session, @RequestParam int x, @RequestParam int y) {

        Player player = new Player(session.getId());

        try {
            game.markSquare(player, x, y);
        } catch(Exception e) {
            return failureResp(e);
        }

        return successResp("You made your move");
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
