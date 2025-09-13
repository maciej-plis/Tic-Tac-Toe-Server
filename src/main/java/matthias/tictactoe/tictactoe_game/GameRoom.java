package matthias.tictactoe.tictactoe_game;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.tictactoe_game.command.GameRoomCommand;
import matthias.tictactoe.tictactoe_game.command.PlayerJoinCommand;
import matthias.tictactoe.tictactoe_game.command.SpectatorJoinCommand;
import matthias.tictactoe.tictactoe_game.command.SpectatorLeaveCommand;
import matthias.tictactoe.tictactoe_game.dto.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class GameRoom {

    private final GameRoomNotifier notifier = new GameRoomNotifier() {
    };

    private final Game game = new Game(notifier);
    private final Set<Spectator> spectators = new HashSet<>();

    private final UUID id = UUID.randomUUID();
    private final String name;

    void resolveCommand(GameRoomCommand cmd) {
        switch (cmd) {
            case SpectatorJoinCommand c -> onSpectatorJoin(c);
            case SpectatorLeaveCommand c -> onSpectatorLeave(c);
            case PlayerJoinCommand c -> onPlayerJoined(c);
            default -> game.resolveCommand(cmd);
        }
    }

    boolean hasSpectator(UUID userId) {
        return spectators.stream().anyMatch(spectator -> spectator.userId().equals(userId));
    }

    GameRoomDTO toDTO() {
        return GameRoomDTO.builder()
            .players(getPlayers())
            .spectators(getSpectators())
            .turn(game.getSymbolTurn() != null ? SymbolDTO.valueOf(game.getSymbolTurn().name()) : null)
            .gameBoard(getGameBoardDTO())
            .gameStatus(GameStatusDTO.valueOf(game.getStatus().name()))
            .build();
    }

    private Set<PlayerDTO> getPlayers() {
        return game.getPlayers().stream()
            .map(p -> PlayerDTO.builder()
                .id(p.userId())
                .name(p.name())
                .symbol(SymbolDTO.valueOf(p.symbol().name()))
                .isReady(game.isPlayerReady(p.userId()))
                .build())
            .collect(toSet());
    }

    private Set<SpectatorDTO> getSpectators() {
        return spectators.stream()
            .map(s -> SpectatorDTO.builder()
                .id(s.userId())
                .name(s.name())
                .build())
            .collect(toSet());
    }

    private SymbolDTO[][] getGameBoardDTO() {
        return Arrays.stream(game.getBoard().getInnerBoard())
            .map(row -> Arrays.stream(row)
                .map(cell -> cell == null ? null : SymbolDTO.valueOf(cell.name()))
                .toArray(SymbolDTO[]::new))
            .toArray(SymbolDTO[][]::new);
    }

    private void onPlayerJoined(PlayerJoinCommand c) {
        if (hasSpectator(c.userId())) {
            final var spectator = getSpectatorOrThrow(c.userId());
            spectators.remove(spectator);
            game.addPlayer(c.userId());
            notifier.onSpectatorChangedToPlayer(c.userId());
        } else {
            game.resolveCommand(c);
        }
    }

    private void onSpectatorJoin(SpectatorJoinCommand cmd) {
        if (game.hasPlayer(cmd.userId())) {
            game.removePlayer(cmd.userId());
            spectators.add(new Spectator(cmd.userId(), ""));
            notifier.onPlayerChangedToSpectator(cmd.userId());
        } else {
            spectators.add(new Spectator(cmd.userId(), ""));
            notifier.onSpectatorJoined(cmd.userId());
        }
    }

    private void onSpectatorLeave(SpectatorLeaveCommand cmd) {
        final var spectator = getSpectatorOrThrow(cmd.userId());
        spectators.remove(spectator);
        notifier.onSpectatorLeft(cmd.userId());
    }

    private Spectator getSpectatorOrThrow(UUID userId) {
        return spectators.stream()
            .filter(s -> s.userId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Spectator not found."));
    }
}
