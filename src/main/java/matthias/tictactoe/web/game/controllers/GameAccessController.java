package matthias.tictactoe.web.game.controllers;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.web.authentication.utils.ResponseEntityBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class GameAccessController {

    private final TicTacToeGame game;

    @PostMapping("/join")
    public ResponseEntity<?> joinGame(Principal principal) {

        game.join(principal.getName());

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "Room join success")
                .build();
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveGame(Principal principal) {

        game.leave(principal.getName());

        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", true)
                .addToPayload("message", "Room leave success")
                .build();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception e) {
        return ResponseEntityBuilder
                .status(200)
                .addToPayload("success", false)
                .addToPayload("message", e.getMessage())
                .build();
    }

}
