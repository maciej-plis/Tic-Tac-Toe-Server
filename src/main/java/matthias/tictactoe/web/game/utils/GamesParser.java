package matthias.tictactoe.web.game.utils;

import matthias.tictactoe.game.TicTacToeGame;
import matthias.tictactoe.game.model.Player;
import matthias.tictactoe.web.game.dtos.GameDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class GamesParser {

    public static Collection<GameDto> parseGamesToGamesDtos(Collection<TicTacToeGame> games) {
        return games.stream()
                .map(game -> {
                    Collection<Player> players = game.getPlayersManager().getPlayers();

                    return GameDto.builder()
                            .id(game.getId())
                            .name(game.getName())
                            .players(
                                players.stream()
                                    .collect(Collectors.toMap(Player::getSymbol, Player::getName))
                            )
                            .build();
                }).collect(Collectors.toList());
    }

}
