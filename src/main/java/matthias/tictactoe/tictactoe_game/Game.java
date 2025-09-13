package matthias.tictactoe.tictactoe_game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import matthias.tictactoe.tictactoe_game.command.*;

import java.util.*;

import static java.util.stream.Collectors.toSet;
import static matthias.tictactoe.tictactoe_game.GameStatus.*;

@RequiredArgsConstructor
class Game {

    private final GameRoomNotifier notifier;

    @Getter
    private final List<Player> players = new ArrayList<>();

    @Getter
    private final Set<Player> readyPlayers = new HashSet<>();

    @Getter
    private Player playerTurn = null;

    @Getter
    private GameStatus status = WAITING_FOR_PLAYERS;

    @Getter
    private final Board board = new Board(3);

    void resolveCommand(GameRoomCommand cmd) {
        switch (cmd) {
            case PlayerJoinCommand c -> onPlayerJoin(c);
            case PlayerLeaveCommand c -> onPlayerLeave(c);
            case PlayerReadyCommand c -> onPlayerReady(c);
            case PlayerNotReadyCommand c -> onPlayerNotReady(c);
            case PlayerMoveCommand c -> onPlayerMove(c);
            case PlayerRequestRematch c -> onPlayerRematch(c);
            case PlayerCancelRematchCommand c -> onPlayerNotRematch(c);
            default -> throw new IllegalStateException("Unknown command.");
        }
    }

    Symbol getSymbolTurn() {
        if (playerTurn == null) return null;
        return playerTurn.symbol();
    }

    boolean isPlayerReady(UUID userId) {
        return readyPlayers.stream().anyMatch(p -> p.userId().equals(userId));
    }

    boolean hasPlayer(UUID userId) {
        return findPlayer(userId).isPresent();
    }

    void addPlayer(UUID userId) {
        if (status != WAITING_FOR_PLAYERS) throw new RuntimeException("Game is not waiting for players.");

        final var takenSymbols = players.stream().map(Player::symbol).collect(toSet());
        final var symbol = Arrays.stream(Symbol.values())
            .filter(s -> !takenSymbols.contains(s))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Game is full"));

        players.add(new Player(userId, "", symbol));

        if (players.size() == Symbol.values().length) {
            status = WAITING_FOR_PLAYERS_READY;
        }
    }

    void removePlayer(UUID userId) {
        final var player = findPlayerOrThrow(userId);

        players.remove(player);
        status = WAITING_FOR_PLAYERS;
    }

    protected void onPlayerJoin(PlayerJoinCommand cmd) {
        addPlayer(cmd.userId());
        notifier.onPlayerJoined(cmd.userId());
    }

    protected void onPlayerLeave(PlayerLeaveCommand cmd) {
        removePlayer(cmd.userId());
        notifier.onPlayerLeft(cmd.userId());
    }

    protected void onPlayerReady(PlayerReadyCommand cmd) {
        final var player = findPlayerOrThrow(cmd.userId());
        if (status != WAITING_FOR_PLAYERS_READY) throw new RuntimeException("Game is not waiting for players ready.");

        readyPlayers.add(player);
        notifier.onPlayerReady(cmd.userId());

        if (readyPlayers.size() == players.size()) {
            startGame();
        }
    }

    protected void onPlayerRematch(PlayerRequestRematch cmd) {
        final var player = findPlayerOrThrow(cmd.userId());
        if (status != FINISHED) throw new RuntimeException("Game is not waiting for players rematch.");

        readyPlayers.add(player);
        notifier.onRematchRequest(cmd.userId());

        if (readyPlayers.size() == players.size()) {
            startGame();
        }
    }

    protected void onPlayerNotReady(PlayerNotReadyCommand cmd) {
        final var player = findPlayerOrThrow(cmd.userId());
        if (status != WAITING_FOR_PLAYERS_READY) throw new RuntimeException("Game is not waiting for players to be ready.");

        readyPlayers.remove(player);
        notifier.onPlayerNotReady(cmd.userId());
    }

    protected void onPlayerNotRematch(PlayerCancelRematchCommand cmd) {
        final var player = findPlayerOrThrow(cmd.userId());
        if (status != FINISHED) throw new RuntimeException("Game is not waiting for players to be ready.");

        readyPlayers.remove(player);
        notifier.onRematchRequestCanceled(cmd.userId());
    }

    protected void onPlayerMove(PlayerMoveCommand cmd) {
        final var player = findPlayerOrThrow(cmd.userId());
        if (status != IN_PROGRESS) throw new RuntimeException("Game is not in progress.");
        if (playerTurn != player) throw new RuntimeException("It's not a player's turn.");

        final var boardCoords = board.toBoardCoordinates(cmd.row(), cmd.col());
        board.set(boardCoords, player.symbol());
        notifier.onPlayerMove(cmd.userId());

        if (board.hasResult()) {
            status = FINISHED;
            notifier.onGameFinished();
        } else {
            playerTurn = nextPlayerTurn();
            notifier.onPlayerTurnChanged(cmd.userId());
        }
    }

    private void startGame() {
        board.reset();
        readyPlayers.clear();
        status = IN_PROGRESS;

        if (playerTurn == null) {
            playerTurn = players.stream().findFirst().orElseThrow(IllegalStateException::new);
        } else {
            playerTurn = nextPlayerTurn();
        }

        notifier.onPlayerTurnChanged(playerTurn.userId());
        notifier.onGameStarted();
    }

    private Player nextPlayerTurn() {
        final var currentIndex = players.indexOf(playerTurn);
        final var nextIndex = indexRoundRobin(currentIndex + 1, players.size());
        return players.get(nextIndex);
    }

    private int indexRoundRobin(int index, int size) {
        return index % size;
    }

    private Optional<Player> findPlayer(UUID userId) {
        return players.stream()
            .filter(p -> p.userId().equals(userId))
            .findAny();
    }

    private Player findPlayerOrThrow(UUID userId) {
        return findPlayer(userId).orElseThrow(() -> new RuntimeException("Player not found."));
    }
}
