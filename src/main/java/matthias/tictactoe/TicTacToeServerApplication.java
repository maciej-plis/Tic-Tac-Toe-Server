package matthias.tictactoe;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class TicTacToeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicTacToeServerApplication.class, args);
    }

}

