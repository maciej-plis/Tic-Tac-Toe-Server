package matthias.tictactoe.tictactoe_game.game_room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import matthias.tictactoe.shared.command.Command;
import matthias.tictactoe.shared.command.CommandHandler;
import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.Game;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerLeaveCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.SpectatorJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.SpectatorLeaveCommand;
import matthias.tictactoe.tictactoe_game.game_room.dto.BasicGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.DetailedGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.SpectatorDTO;
import matthias.tictactoe.tictactoe_game.game_room.event.*;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomAccessExeption;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomSpectatorNotFoundException;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.GameCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static matthias.tictactoe.tictactoe_game.tictactoe_game.TicTacToeGameFactory.createTicTacToeGame;

@RequiredArgsConstructor
class GameRoom implements CommandHandler {

    private final GameRoomMessagePublisher messagePublisher;

    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    private final String name;

    private final Game game = createTicTacToeGame(this::publishGameEvent);
    private final Set<Spectator> spectators = new HashSet<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(Command<T> cmd) {
        return (T) switch (cmd) {
            case SpectatorJoinCommand c -> handle(c, this::onSpectatorJoin);
            case SpectatorLeaveCommand c -> handle(c, this::onSpectatorLeave);
            case PlayerJoinCommand c -> handle(c, this::onPlayerJoin);
            case PlayerLeaveCommand c -> handle(c, this::onPlayerLeave);
            case GameCommand<T> c -> handle(c, this::handleGameCommand);
            default -> throw new IllegalArgumentException("Unknown command " + cmd.getClass().getSimpleName() + " was passed to GameRoom.");
        };
    }

    public BasicGameRoomInfoDTO getBasicGameRoomInfo() {
        return BasicGameRoomInfoDTO.builder()
            .gameRoomId(id)
            .gameRoomName(name)
            .playersCount(game.getPlayers().size())
            .spectatorsCount(spectators.size())
            .build();
    }

    public DetailedGameRoomInfoDTO getDetailedGameRoomInfo(UUID userId) {
        if (!game.hasPlayer(userId) && !hasSpectator(userId)) {
            throw new GameRoomAccessExeption("User '" + userId + "' has no access to game room details '" + id + "'.");
        }

        return DetailedGameRoomInfoDTO.builder()
            .gameRoomId(id)
            .gameRoomName(name)
            .players(game.getPlayers())
            .spectators(getSpectators())
            .gameDetails(game.getDetails())
            .build();
    }

    public List<SpectatorDTO> getSpectators() {
        return spectators.stream()
            .map(s -> new SpectatorDTO(s.userId(), s.name()))
            .toList();
    }

    private void onPlayerJoin(PlayerJoinCommand cmd) {
        if (hasSpectator(cmd.userId())) {
            final var spectator = getSpectatorOrThrow(cmd.userId());
            spectators.remove(spectator);
            game.addPlayer(cmd.userId());
            messagePublisher.publish(id, new SpectatorChangedToPlayerEvent(cmd.userId(), ""));
        } else {
            game.addPlayer(cmd.userId());
            messagePublisher.publish(id, new PlayerJoinedEvent(cmd.userId(), ""));

        }
    }

    private void onPlayerLeave(PlayerLeaveCommand cmd) {
        game.removePlayer(cmd.userId());
        messagePublisher.publish(id, new PlayerLeftEvent(cmd.userId(), ""));
    }

    private void onSpectatorJoin(SpectatorJoinCommand cmd) {
        if (game.hasPlayer(cmd.userId())) {
            game.removePlayer(cmd.userId());
            spectators.add(new Spectator(cmd.userId(), ""));
            messagePublisher.publish(id, new PlayerChangedToSpectatorEvent(cmd.userId(), ""));
        } else {
            spectators.add(new Spectator(cmd.userId(), ""));
            messagePublisher.publish(id, new SpectatorJoinedEvent(cmd.userId(), ""));
        }
    }

    private void onSpectatorLeave(SpectatorLeaveCommand cmd) {
        final var spectator = getSpectatorOrThrow(cmd.userId());
        spectators.remove(spectator);
        messagePublisher.publish(id, new SpectatorLeftEvent(cmd.userId(), ""));
    }

    private <T> T handleGameCommand(GameCommand<T> cmd) {
        if (!game.hasPlayer(cmd.userId())) {
            throw new GameRoomAccessExeption("User '" + cmd.userId() + "' cannot run commands in game room '" + id + "'.");
        }

        return game.handle(cmd);
    }

    private Spectator getSpectatorOrThrow(UUID userId) {
        return spectators.stream()
            .filter(s -> s.userId().equals(userId))
            .findAny()
            .orElseThrow(() -> new GameRoomSpectatorNotFoundException("Couldn't find spectator with userId: " + userId));
    }

    private boolean hasSpectator(UUID userId) {
        return spectators.stream().anyMatch(s -> s.userId().equals(userId));
    }

    private void publishGameEvent(Event event) {
        if(event instanceof matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerJoinedEvent) return; // Swallow event as it's handled by game room
        if(event instanceof matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerLeftEvent) return; // Swallow event as it's handled by game room
        messagePublisher.publish(id, event);
    }
}
