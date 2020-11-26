package matthias.tictactoe.game.states;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.exceptions.GameException;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.game.model.PlayerSymbol;
import matthias.tictactoe.game.model.StateType;
import matthias.tictactoe.game.utils.PlayerUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NotEnoughPlayersGameState extends GameState {
    private Queue<PlayerSymbol> availableSymbols;

    public NotEnoughPlayersGameState(TicTacToeGame game) {
        super(game, StateType.NOT_ENOUGH_PLAYERS);
        this.availableSymbols = initAvailableSymbols();
        PlayerUtils.resetScoreForEveryone(playersManager.getPlayers(), game::newEvent);
        board.clear();
    }

    @Override
    public void join(String name) {
        if(playersManager.containsPlayer(name)) {
            throw new GameException("You already joined the game.");
        }

        Player player = new Player(availableSymbols.poll(), name);
        playersManager.addPlayer(player);

        if(availableSymbols.isEmpty()) {
            game.setState(new InProgressGameState(game));
        }
    }

    @Override
    public void leave(String name) {
        Player removedPlayer = playersManager.removePlayer(name);
        availableSymbols.add(removedPlayer.getSymbol());
    }

    @Override
    public void mark(String name, Point point) {
        throw new GameException("Game hasn't started yet.");
    }

    @Override
    public void rematch(String name) {
        throw new GameException("You can vote for rematch after finished game.");
    }

    private Queue<PlayerSymbol> initAvailableSymbols() {
        Queue<PlayerSymbol> allSymbols = new LinkedList<>(Arrays.asList(PlayerSymbol.values()));

        List<PlayerSymbol> symbolsInUse = playersManager.getPlayers().stream()
            .map(Player::getSymbol)
            .collect(Collectors.toList());

        allSymbols.removeAll(symbolsInUse);
        return allSymbols;
    }
}
