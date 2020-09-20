package matthias.tictactoe.web.game.services;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.web.game.exceptions.GameCreationException;
import matthias.tictactoe.web.game.exceptions.GameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class GamesManager {
    private final GameEventWsPublisher eventPublisher;
    private final Map<String, TicTacToeGame> games = new HashMap<>();

    public GamesManager(GameEventWsPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        init();
    }

    public TicTacToeGame createNewGame(String gameName) {
        if(games.containsKey(gameName)) {
            throw new GameCreationException(String.format("Game name %s is already in use.", gameName));
        }

        TicTacToeGame newGame = new TicTacToeGame(gameName);
        games.put(newGame.getName(), newGame);
        eventPublisher.trackGameEvents(newGame);
        return newGame;
    }

    public TicTacToeGame removeGame(String gameName) {
        verifyGameExistence(gameName);
        return games.remove(gameName);
    }

    public TicTacToeGame getGame(String gameName) {
        verifyGameExistence(gameName);
        return games.get(gameName);
    }

    public Collection<TicTacToeGame> getGames() {
        return games.values();
    }

    private void init() {
        for(int i=0; i<15; i++) {
            createNewGame("gameNr" + i);
        }
    }

    private void verifyGameExistence(String gameName) {
        if(!games.containsKey(gameName)) {
            throw new GameNotFoundException(String.format("Game %s doesn't exist.", gameName));
        }
    }
}
