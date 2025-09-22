package matthias.tictactoe.tictactoe_game.tictactoe_game;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.shared.command.Command;
import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.Game;
import matthias.tictactoe.tictactoe_game.game_room.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.*;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.GameStatusDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.SymbolDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.TicTacToeGameDetails;
import matthias.tictactoe.tictactoe_game.tictactoe_game.event.*;
import matthias.tictactoe.tictactoe_game.tictactoe_game.exception.GameIsFullException;
import matthias.tictactoe.tictactoe_game.tictactoe_game.exception.GamePlayerNotFoundException;
import matthias.tictactoe.tictactoe_game.tictactoe_game.exception.IllegalGameActionException;
import matthias.tictactoe.tictactoe_game.tictactoe_game.exception.IllegalPlayerMoveException;

import java.util.*;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toSet;
import static matthias.tictactoe.tictactoe_game.tictactoe_game.TicTacToeBoard.BoardResult.*;
import static matthias.tictactoe.tictactoe_game.tictactoe_game.TicTacToeGameStatus.*;

@RequiredArgsConstructor
class TicTacToeGame implements Game {

    private final Consumer<Event> eventConsumer;

    private final List<TicTacToePlayer> players = new ArrayList<>();

    private final Set<TicTacToePlayer> readyPlayers = new HashSet<>();

    private TicTacToePlayer playerTurn = null;

    private TicTacToeGameStatus status = WAITING_FOR_PLAYERS;

    private final TicTacToeBoard board = new TicTacToeBoard(3);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(Command<T> cmd) {
        return (T) switch (cmd) {
            case PlayerReadyCommand c -> handle(c, this::onPlayerReady);
            case PlayerNotReadyCommand c -> handle(c, this::onPlayerNotReady);
            case PlayerMoveCommand c -> handle(c, this::onPlayerMove);
            case PlayerRequestRematchCommand c -> handle(c, this::onPlayerRematch);
            case PlayerCancelRematchCommand c -> handle(c, this::onPlayerNotRematch);
            default -> throw new IllegalArgumentException("Unknown command " + cmd.getClass().getSimpleName() + " was passed to TicTacToeGame.");
        };
    }

    @Override
    public TicTacToeGameDetails getDetails() {
        return TicTacToeGameDetails.builder()
            .gameStatus(GameStatusDTO.valueOf(status.name()))
            .players(getPlayersDTO())
            .board(getBoardDTO())
            .symbolTurn(playerTurn != null ? SymbolDTO.valueOf(playerTurn.symbol().name()) : null)
            .symbolWinner(board.getResult() == WIN_X ? SymbolDTO.X : board.getResult() == WIN_O ? SymbolDTO.O : null)
            .build();
    }

    @Override
    public List<PlayerDTO> getPlayers() {
        return players.stream()
            .map(p -> new PlayerDTO(p.userId(), p.name()))
            .toList();
    }

    @Override
    public Optional<PlayerDTO> findPlayer(UUID userId) {
        return getPlayers().stream()
            .filter(p -> p.userId().equals(userId))
            .findAny();
    }

    @Override
    public void addPlayer(UUID userId) {
        if (status != WAITING_FOR_PLAYERS) throw new IllegalGameActionException("Game is not in phase allowing to add players.");

        final var takenSymbols = players.stream().map(TicTacToePlayer::symbol).collect(toSet());
        final var symbol = Arrays.stream(TicTacToeSymbol.values())
            .filter(s -> !takenSymbols.contains(s))
            .findFirst()
            .orElseThrow(() -> new GameIsFullException("Player cannot be added to the game because it is full."));

        players.add(new TicTacToePlayer(userId, "", symbol));
        eventConsumer.accept(new PlayerJoinedEvent(userId, SymbolDTO.valueOf(symbol.name())));

        if (players.size() == TicTacToeSymbol.values().length) {
            status = WAITING_FOR_PLAYERS_READY;
            eventConsumer.accept(new WaitingForPlayersToBeReadyEvent());
        }
    }

    @Override
    public void removePlayer(UUID userId) {
        final var player = getPlayerOrThrow(userId);

        players.remove(player);
        eventConsumer.accept(new PlayerLeftEvent(player.userId(), SymbolDTO.valueOf(player.symbol().name())));

        if (status == WAITING_FOR_PLAYERS) return;

        status = WAITING_FOR_PLAYERS;
        eventConsumer.accept(new WaitingForPlayersToJoinEvent());
    }

    @Override
    public boolean hasPlayer(UUID userId) {
        return players.stream().anyMatch(p -> p.userId().equals(userId));
    }

    protected void onPlayerReady(PlayerReadyCommand cmd) {
        if (status != WAITING_FOR_PLAYERS_READY) throw new IllegalGameActionException("Game is not in phase waiting for players to be ready.");

        final var player = getPlayerOrThrow(cmd.userId());
        readyPlayers.add(player);
        eventConsumer.accept(new PlayerReadyEvent(player.userId(), SymbolDTO.valueOf(player.symbol().name())));

        if (readyPlayers.size() == players.size()) {
            startGame();
        }
    }

    protected void onPlayerRematch(PlayerRequestRematchCommand cmd) {
        if (status != FINISHED) throw new IllegalGameActionException("Game is not in phase allowing players to request rematch.");

        final var player = getPlayerOrThrow(cmd.userId());
        readyPlayers.add(player);

        if (readyPlayers.size() == players.size()) {
            eventConsumer.accept(new RematchAccepted(player.userId(), SymbolDTO.valueOf(player.symbol().name())));
            startGame();
        } else {
            eventConsumer.accept(new RematchRequested(player.userId(), SymbolDTO.valueOf(player.symbol().name())));
        }
    }

    protected void onPlayerNotReady(PlayerNotReadyCommand cmd) {
        if (status != WAITING_FOR_PLAYERS_READY) throw new IllegalGameActionException("Game is not in phase waiting for players to be ready.");

        final var player = getPlayerOrThrow(cmd.userId());
        readyPlayers.remove(player);
        eventConsumer.accept(new PlayerNotReadyEvent(player.userId(), SymbolDTO.valueOf(player.symbol().name())));
    }

    protected void onPlayerNotRematch(PlayerCancelRematchCommand cmd) {
        if (status != FINISHED) throw new IllegalGameActionException("Game is not in phase allowing players to request rematch.");

        final var player = getPlayerOrThrow(cmd.userId());
        readyPlayers.remove(player);
        eventConsumer.accept(new RematchCancelled(player.userId(), SymbolDTO.valueOf(player.symbol().name())));
    }

    protected void onPlayerMove(PlayerMoveCommand cmd) {
        if (status != IN_PROGRESS) throw new IllegalGameActionException("Player cannot make move because game is not in progress.");

        final var player = getPlayerOrThrow(cmd.userId());
        if (player != playerTurn) throw new IllegalPlayerMoveException("It's not the turn of player with userId: " + cmd.userId());

        if (!board.isValidBoardCoordinates(cmd.row(), cmd.col())) {
            throw new IllegalPlayerMoveException("Player move coordinates (" + cmd.row() + ", " + cmd.col() + ") are not valid.");
        }

        if (!board.isBoardCoordinatesFree(cmd.row(), cmd.col())) {
            throw new IllegalPlayerMoveException("Player move coordinates (" + cmd.row() + ", " + cmd.col() + ") are already taken.");
        }

        final var boardCoords = board.toBoardCoordinates(cmd.row(), cmd.col());
        board.set(boardCoords, player.symbol());
        eventConsumer.accept(new PlayerMoveEvent(player.userId(), SymbolDTO.valueOf(player.symbol().name()), cmd.row(), cmd.col()));

        if (board.hasResult()) {
            status = FINISHED;
            eventConsumer.accept(board.getResult() == TIE ? new GameDrawnEvent() : new PlayerWonEvent(player.userId(), SymbolDTO.valueOf(player.symbol().name())));
        } else {
            playerTurn = nextPlayerTurn();
            eventConsumer.accept(new PlayerTurnChangedEvent(playerTurn.userId(), SymbolDTO.valueOf(playerTurn.symbol().name())));
        }
    }

    private void startGame() {
        board.reset();
        readyPlayers.clear();
        status = IN_PROGRESS;

        if (playerTurn == null) {
            playerTurn = players.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Game shouldn't be started without players."));
        } else {
            playerTurn = nextPlayerTurn();
        }

        eventConsumer.accept(new PlayerTurnChangedEvent(playerTurn.userId(), SymbolDTO.valueOf(playerTurn.symbol().name())));
        eventConsumer.accept(new GameStartedEvent());
    }

    private TicTacToePlayer nextPlayerTurn() {
        final var currentIndex = players.indexOf(playerTurn);
        final var nextIndex = indexRoundRobin(currentIndex + 1, players.size());
        return players.get(nextIndex);
    }

    private int indexRoundRobin(int index, int size) {
        return index % size;
    }

    private TicTacToePlayer getPlayerOrThrow(UUID userId) {
        return players.stream()
            .filter(p -> p.userId().equals(userId))
            .findAny()
            .orElseThrow(() -> new GamePlayerNotFoundException("Couldn't find player with userId: " + userId));
    }

    private List<matthias.tictactoe.tictactoe_game.tictactoe_game.dto.PlayerDTO> getPlayersDTO() {
        return players.stream()
            .map(p -> {
                final var isReady = readyPlayers.contains(p) && status == WAITING_FOR_PLAYERS_READY;
                final var requestsRematch = readyPlayers.contains(p) && status == FINISHED;
                return new matthias.tictactoe.tictactoe_game.tictactoe_game.dto.PlayerDTO(p.userId(), p.name(), SymbolDTO.valueOf(p.symbol().name()), isReady, requestsRematch);
            }).toList();
    }

    private SymbolDTO[][] getBoardDTO() {
        return Arrays.stream(board.getInnerBoard())
            .map(row -> Arrays.stream(row)
                .map(cell -> cell == null ? null : SymbolDTO.valueOf(cell.name()))
                .toArray(SymbolDTO[]::new))
            .toArray(SymbolDTO[][]::new);
    }
}
