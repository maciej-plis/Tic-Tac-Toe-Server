package matthias.tictactoe.tictactoe_game.tictactoe_game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.Game;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class TicTacToeGameFactory {

    public static Game createTicTacToeGame(Consumer<Event> eventConsumer) {
        return new TicTacToeGame(eventConsumer);
    }
}
