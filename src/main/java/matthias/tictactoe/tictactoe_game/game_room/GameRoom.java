package matthias.tictactoe.tictactoe_game.game_room;

import lombok.Getter;
import matthias.tictactoe.shared.command.Command;
import matthias.tictactoe.shared.command.CommandHandler;
import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.Game;
import matthias.tictactoe.tictactoe_game.game_room.command.*;
import matthias.tictactoe.tictactoe_game.game_room.dto.BasicGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.DetailedGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.SpectatorDTO;
import matthias.tictactoe.tictactoe_game.game_room.event.*;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomAccessExeption;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomSpectatorNotFoundException;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.GameCommand;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static matthias.tictactoe.tictactoe_game.tictactoe_game.TicTacToeGameFactory.createTicTacToeGame;

class GameRoom implements CommandHandler {

    private final GameRoomMessagePublisher messagePublisher;

    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    private String name;

    @Getter
    private final UUID ownerId;

    @Getter
    private final Instant creationDate = Instant.now();

    @Getter
    private boolean spectatingEnabled;

    private final Set<UUID> bannedUsers = new HashSet<>();

    private final Game game = createTicTacToeGame(this::publishGameEvent);
    private final Set<Spectator> spectators = new HashSet<>();

    public GameRoom(GameRoomMessagePublisher messagePublisher, String name, UUID ownerId, boolean spectatingEnabled) {
        this.messagePublisher = messagePublisher;
        this.name = name;
        this.ownerId = ownerId;
        this.spectatingEnabled = spectatingEnabled;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(Command<T> cmd) {
        return (T) switch (cmd) {
            case UpdateGameRoomCommand c -> handle(c, this::updateDetails);
            case SpectatorJoinCommand c -> handle(c, this::onSpectatorJoin);
            case SpectatorLeaveCommand c -> handle(c, this::onSpectatorLeave);
            case PlayerJoinCommand c -> handle(c, this::onPlayerJoin);
            case PlayerLeaveCommand c -> handle(c, this::onPlayerLeave);
            case KickUserCommand c -> handle(c, this::kickUser);
            case BanUserCommand c -> handle(c, this::banUser);
            case UnbanUserCommand c -> handle(c, this::unbanUser);
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
            .ownerId(ownerId)
            .creationDate(creationDate)
            .spectatingEnabled(spectatingEnabled)
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

    public boolean isOwner(UUID userId) {
        return ownerId.equals(userId);
    }

    private void updateDetails(UpdateGameRoomCommand cmd) {
        if (!isOwner(cmd.userId())) {
            throw new GameRoomAccessExeption("User '" + cmd.userId() + "' is not allowed to update game room '" + id + "'.");
        }

        this.name = cmd.name();
        this.spectatingEnabled = cmd.spectatingEnabled();

        messagePublisher.publish(id, new GameRoomUpdatedEvent(id, name, spectatingEnabled));
    }

    private void onPlayerJoin(PlayerJoinCommand cmd) {
        if (bannedUsers.contains(cmd.userId())) {
            throw new GameRoomAccessExeption("User '" + cmd.userId() + "' is banned from this game room.");
        } else if (hasSpectator(cmd.userId())) {
            final var spectator = getSpectatorOrThrow(cmd.userId());
            spectators.remove(spectator);
            game.addPlayer(cmd.userId(), cmd.user().username());
            messagePublisher.publish(id, new SpectatorChangedToPlayerEvent(cmd.userId(), cmd.user().username()));
        } else {
            game.addPlayer(cmd.userId(), cmd.user().username());
            messagePublisher.publish(id, new PlayerJoinedEvent(cmd.userId(), cmd.user().username()));

        }
    }

    private void onPlayerLeave(PlayerLeaveCommand cmd) {
        game.removePlayer(cmd.userId());
        messagePublisher.publish(id, new PlayerLeftEvent(cmd.userId()));
    }

    private void onSpectatorJoin(SpectatorJoinCommand cmd) {
        if (!spectatingEnabled) {
            throw new GameRoomAccessExeption("Spectators are disabled in this game room.");
        } else if (bannedUsers.contains(cmd.userId())) {
            throw new GameRoomAccessExeption("User '" + cmd.userId() + "' is banned from this game room.");
        }

        final var spectator = new Spectator(cmd.user().id(), cmd.user().username());
        if (game.hasPlayer(cmd.user().id())) {
            game.removePlayer(cmd.user().id());
            spectators.add(spectator);
            messagePublisher.publish(id, new PlayerChangedToSpectatorEvent(cmd.user().id(), cmd.user().username()));
        } else {
            spectators.add(spectator);
            messagePublisher.publish(id, new SpectatorJoinedEvent(cmd.user().id(), cmd.user().username()));
        }
    }

    private void onSpectatorLeave(SpectatorLeaveCommand cmd) {
        final var spectator = getSpectatorOrThrow(cmd.userId());
        spectators.remove(spectator);
        messagePublisher.publish(id, new SpectatorLeftEvent(cmd.userId()));
    }

    public void kickUser(KickUserCommand cmd) {
        if (!isOwner(cmd.actorId())) {
            throw new GameRoomAccessExeption("User '" + cmd.actorId() + "' is not an owner of game room '" + id + "'.");
        }

        if (hasSpectator(cmd.userId())) {
            spectators.remove(getSpectatorOrThrow(cmd.userId()));
            messagePublisher.publish(id, new UserKickedEvent(cmd.userId()));
        }
        if (game.hasPlayer(cmd.userId())) {
            game.removePlayer(cmd.userId());
            messagePublisher.publish(id, new UserKickedEvent(cmd.userId()));
        }
    }

    public void banUser(BanUserCommand cmd) {
        if (!isOwner(cmd.actorId())) {
            throw new GameRoomAccessExeption("User '" + cmd.actorId() + "' is not an owner of game room '" + id + "'.");
        }

        if (hasSpectator(cmd.userId())) spectators.remove(getSpectatorOrThrow(cmd.userId()));
        if (game.hasPlayer(cmd.userId())) game.removePlayer(cmd.userId());

        if (bannedUsers.add(cmd.userId())) {
            messagePublisher.publish(id, new UserBannedEvent(cmd.userId()));
        }
    }

    private void unbanUser(UnbanUserCommand cmd) {
        if (!cmd.actorId().equals(ownerId)) {
            throw new GameRoomAccessExeption("User '" + cmd.actorId() + "' is not an owner of game room '" + id + "'.");
        }

        if (bannedUsers.remove(cmd.userId())) {
            messagePublisher.publish(id, new UserUnbannedEvent(id, cmd.userId()));
        }
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
            .orElseThrow(() -> new GameRoomSpectatorNotFoundException("Couldn't find spectator with user: " + userId));
    }

    private boolean hasSpectator(UUID userId) {
        return spectators.stream().anyMatch(s -> s.userId().equals(userId));
    }

    private void publishGameEvent(Event event) {
        if (event instanceof matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerJoinedEvent) return; // Swallow event as it's handled by game room
        if (event instanceof matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerLeftEvent) return; // Swallow event as it's handled by game room
        messagePublisher.publish(id, event);
    }
}
