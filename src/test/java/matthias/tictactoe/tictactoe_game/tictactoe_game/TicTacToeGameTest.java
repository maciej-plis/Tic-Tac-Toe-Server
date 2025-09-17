package matthias.tictactoe.tictactoe_game.tictactoe_game;

import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerMoveCommand;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerReadyCommand;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerNotReadyCommand;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerRequestRematchCommand;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerCancelRematchCommand;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.GameStatusDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.SymbolDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerJoinedEvent;
import matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerLeftEvent;
import matthias.tictactoe.tictactoe_game.tictactoe_game.exception.IllegalGameActionException;
import matthias.tictactoe.tictactoe_game.tictactoe_game.exception.IllegalPlayerMoveException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

import static matthias.tictactoe.tictactoe_game.tictactoe_game.dto.GameStatusDTO.*;
import static matthias.tictactoe.tictactoe_game.tictactoe_game.dto.GameStatusDTO.FINISHED;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeGameTest {

    private final static UUID USER_1_ID = UUID.randomUUID();
    private final static UUID USER_2_ID = UUID.randomUUID();
    private final static UUID USER_3_ID = UUID.randomUUID();

    private List<Event> events;
    private TicTacToeGame game;

    @BeforeEach
    void init() {
        events = new ArrayList<>();
        game = new TicTacToeGame(events::add);
    }

    @DisplayName("Player can be added to the game")
    @Test
    void playerCanBeAddedToTheGame() {
        // When
        game.addPlayer(USER_1_ID);

        // Then
        gameHasPlayer(USER_1_ID);
    }

    @DisplayName("When player is added to the game, PlayerJoinedEvent is emitted")
    @Test
    void whenPlayerIsAddedToTheGamePlayerJoinedEventIsEmitted() {
        // When
        game.addPlayer(USER_1_ID);

        // Then
        eventIsEmitted(PlayerJoinedEvent.class, e ->
            USER_1_ID.equals(e.userId()) && SymbolDTO.X.equals(e.symbol())
        );
    }

    @DisplayName("player can be removed from the game")
    @Test
    void playerCanBeRemovedFromTheGame() {
        // Given
        game.addPlayer(USER_1_ID);

        // When
        game.removePlayer(USER_1_ID);

        // Then
        gameHasNoPlayers();
    }

    @DisplayName("When player is removed from the game, PlayerLeftEvent is emitted")
    @Test
    void whenPlayerIsRemovedFromTheGamePlayerLeftEventIsEmitted() {
        // Given
        game.addPlayer(USER_1_ID);

        // When
        game.removePlayer(USER_1_ID);

        // Then
        eventIsEmitted(PlayerLeftEvent.class, e ->
            USER_1_ID.equals(e.userId()) && SymbolDTO.X.equals(e.symbol())
        );
    }

    @DisplayName("User cannot be added to the game when it's full")
    @Test
    void userCannotBeAddedToTheGameWhenItsFull() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);

        // Expect
        assertThrows(RuntimeException.class, () -> game.addPlayer(USER_3_ID));
    }

    @DisplayName("Game status should not change after first player joins")
    @Test
    void gameStatusShouldNotChangeAfterFirstPlayerJoins() {
        // When
        game.addPlayer(USER_1_ID);

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus());
    }

    @DisplayName("Game status changes to WAITING_FOR_PLAYERS_READY after second player joins")
    @Test
    void gameStatusChangesToWaitingForPlayersReadyAfterSecondPlayerJoins() {
        // Given
        game.addPlayer(USER_1_ID);

        // When
        game.addPlayer(USER_2_ID);

        // Then
        assertEquals(WAITING_FOR_PLAYERS_READY, gameStatus());
    }

    @DisplayName("Game status changes to WAITING_FOR_PLAYERS after player leaves before ready phase completes")
    @Test
    void gameStatusChangesToWaitingForPlayersAfterPlayerLeaves() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);

        // When
        game.removePlayer(USER_1_ID);

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus());
    }

    @DisplayName("Player can be set ready")
    @Test
    void playerCanBeSetReady() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);

        // When
        game.handle(new PlayerReadyCommand(USER_1_ID));

        // Then
        gamePlayerIsReady(USER_1_ID);
    }

    @DisplayName("Player cannot be set ready when game is waiting for players")
    @Test
    void playerCannotBeSetReadyWhenGameIsWaitingForPlayers() {
        // Given
        game.addPlayer(USER_1_ID);

        // Expect
        assertThrows(IllegalGameActionException.class, () -> game.handle(new PlayerReadyCommand(USER_1_ID)));
    }

    @DisplayName("Player cannot be set ready when game is in progress")
    @Test
    void playerCannotBeSetReadyWhenGameIsInProgress() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);
        game.handle(new PlayerReadyCommand(USER_1_ID));
        game.handle(new PlayerReadyCommand(USER_2_ID));

        // Expect
        assertThrows(IllegalGameActionException.class, () -> game.handle(new PlayerReadyCommand(USER_1_ID)));
    }

    @DisplayName("Player can be unset ready")
    @Test
    void playerCanBeUnsetReady() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);
        game.handle(new PlayerReadyCommand(USER_1_ID));

        // When
        game.handle(new PlayerNotReadyCommand(USER_1_ID));

        // Then
        gameHasNoPlayersReady();
    }

    @DisplayName("Game status should not change after first player is ready")
    @Test
    void gameStatusShouldNotChangeAfterFirstPlayerIsReady() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);

        // When
        game.handle(new PlayerReadyCommand(USER_1_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS_READY, gameStatus());
    }

    @DisplayName("Game status should change to IN_PROGRESS after second player is ready")
    @Test
    void gameStatusShouldChangeToInProgressAfterSecondPlayerIsReady() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);
        game.handle(new PlayerReadyCommand(USER_1_ID));

        // When
        game.handle(new PlayerReadyCommand(USER_2_ID));

        // Then
        assertEquals(IN_PROGRESS, gameStatus());
    }

    @DisplayName("Game status should change to FINISHED after game is won by user 1")
    @Test
    void gameStatusShouldChangeToFinishedAfterGameIsWonByUser1() {
        // Given
        startGame();
        boardIsPlayedWithMoves(
            """
                O . X
                O . X
                . . .
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );

        // When
        move(USER_1_ID,2,2);

        // Then
        assertEquals(FINISHED, gameStatus());
        assertEquals(SymbolDTO.X, game.getDetails().symbolWinner());
    }

    @DisplayName("Game status should change to FINISHED after game is won by user 2")
    @Test
    void gameStatusShouldChangeToFinishedAfterGameIsWonByUser2() {
        // Given
        startGame();
        boardIsPlayedWithMoves(
            """
                X . O
                X . O
                . X .
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );

        // When
        move(USER_2_ID,2,2);

        // Then
        assertEquals(FINISHED, gameStatus());
        assertEquals(SymbolDTO.O, game.getDetails().symbolWinner());
    }

    @DisplayName("Game status should change to FINISHED after game is drawn")
    @Test
    void gameStatusShouldChangeToFinishedAfterGameIsDrawn() {
        // Given
        startGame();
        boardIsPlayedWithMoves(
            """
                X O X
                X O O
                O X .
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );

        // When
        move(USER_1_ID,2,2);

        // Then
        assertEquals(FINISHED, gameStatus());
        assertNull(game.getDetails().symbolWinner());
    }

    @DisplayName("Game status should change to WAITING_FOR_PLAYERS when game is in progress and player leaves")
    @Test
    void gameStatusShouldChangeToWaitingForPlayersWhenGameIsInProgressAndPlayerLeaves() {
        // Given
        startGame();
        boardIsPlayedWithMoves(
            """
                X . .
                . . .
                . . O
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );

        // When
        game.removePlayer(USER_1_ID);

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus());
    }

    @DisplayName("Game status should change to WAITING_FOR_PLAYERS after game is finished and player leaves")
    @Test
    void gameStatusShouldChangeToWaitingForPlayersAfterGameIsFinishedAndPlayerLeaves() {
        // Given
        startGame();
        boardIsPlayedWithMoves(
            """
                X O .
                X O .
                . . .
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );
        move(USER_1_ID,2,0);

        // When
        game.removePlayer(USER_2_ID);

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus());
    }

    @DisplayName("Player can request rematch when game is finished")
    @Test
    void playerCanRequestRematchWhenGameIsFinished() {
        // Given
        finishGameWithUser1Win();

        // When
        game.handle(new PlayerRequestRematchCommand(USER_1_ID));

        // Then
        gamePlayerRequestedRematch(USER_1_ID);
    }

    @DisplayName("Player can cancel rematch request")
    @Test
    void playerCanCancelRematchRequest() {
        // Given
        finishGameWithUser1Win();
        game.handle(new PlayerRequestRematchCommand(USER_1_ID));

        // When
        game.handle(new PlayerCancelRematchCommand(USER_1_ID));

        // Then
        gameHasNoPlayersRequestingRematch();
    }

    @DisplayName("Game status should not change when first player requests rematch")
    @Test
    void gameStatusShouldNotChangeWhenFirstPlayerRequestsRematch() {
        // Given
        finishGameWithUser1Win();

        // When
        game.handle(new PlayerRequestRematchCommand(USER_1_ID));

        // Then
        assertEquals(FINISHED, gameStatus());
    }

    @DisplayName("Game status should change to IN_PROGRESS after second player requests rematch")
    @Test
    void gameStatusShouldChangeToInProgressAfterSecondPlayerRequestsRematch() {
        // Given
        finishGameWithUser1Win();
        game.handle(new PlayerRequestRematchCommand(USER_1_ID));

        // When
        game.handle(new PlayerRequestRematchCommand(USER_2_ID));

        // Then
        assertEquals(IN_PROGRESS, gameStatus());
    }

    @DisplayName("Game is started by player who is first in join order at start")
    @Test
    void gameIsStartedByPlayerWhoJoinedFirst() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);
        game.removePlayer(USER_1_ID);
        game.addPlayer(USER_1_ID);

        // When
        game.handle(new PlayerReadyCommand(USER_2_ID));
        game.handle(new PlayerReadyCommand(USER_1_ID));

        // Then
        assertEquals(IN_PROGRESS, gameStatus());
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_1_ID,0,0));
    }

    @DisplayName("Player starting game changes with each rematch")
    @Test
    void playerStartingGameChangesWithEachRematch() {
        // Given
        startGame();
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_2_ID,0,0));

        boardIsPlayedWithMoves(
            """
                X X .
                O O .
                . . .
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );
        move(USER_1_ID,0,2);

        game.handle(new PlayerRequestRematchCommand(USER_1_ID));
        game.handle(new PlayerRequestRematchCommand(USER_2_ID));

        boardIsPlayedWithMoves(
            """
                O X .
                O X .
                . . .
                """,
            Pair.of(USER_2_ID, SymbolDTO.O),
            Pair.of(USER_1_ID, SymbolDTO.X)
        );
        move(USER_2_ID,2,0);

        game.handle(new PlayerRequestRematchCommand(USER_1_ID));
        game.handle(new PlayerRequestRematchCommand(USER_2_ID));

        // Expect
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_2_ID,0,0));
    }

    @DisplayName("Player cannot move before game starts")
    @Test
    void playerCannotMoveBeforeGameStarts() {
        // Given
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);

        // Expect
        assertThrows(IllegalGameActionException.class, () -> move(USER_1_ID,0,0));
    }

    @DisplayName("Player cannot move when not their turn")
    @Test
    void playerCannotMoveWhenNotPlayersTurn() {
        // Given
        startGame();

        // Expect
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_2_ID,0,0));
    }

    @DisplayName("Player cannot move outside board")
    @Test
    void playerCannotMoveOutsideBoard() {
        // Given
        startGame();

        // Expect
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_1_ID,3,0));
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_1_ID,-1,0));
    }

    @DisplayName("Player cannot move on already taken cell")
    @Test
    void playerCannotMoveOnTakenCell() {
        // Given
        startGame();
        move(USER_1_ID,0,0);

        // Expect
        assertThrows(IllegalPlayerMoveException.class, () -> move(USER_2_ID,0,0));
    }

    private void startGame() {
        game.addPlayer(USER_1_ID);
        game.addPlayer(USER_2_ID);
        game.handle(new PlayerReadyCommand(USER_1_ID));
        game.handle(new PlayerReadyCommand(USER_2_ID));
        assertEquals(IN_PROGRESS, gameStatus());
    }

    private void finishGameWithUser1Win() {
        startGame();
        boardIsPlayedWithMoves(
            """
                X O .
                X O .
                . . .
                """,
            Pair.of(USER_1_ID, SymbolDTO.X),
            Pair.of(USER_2_ID, SymbolDTO.O)
        );
        move(USER_1_ID,2,0);
        assertEquals(FINISHED, gameStatus());
    }

    private void move(UUID userId, int row, int col) {
        game.handle(new PlayerMoveCommand(userId, row, col));
    }

    private void gamePlayerRequestedRematch(UUID userId) {
        var player = getPlayer(userId);
        assertTrue(player != null && player.requestedRematch());
    }

    private void gameHasNoPlayersRequestingRematch() {
        assertTrue(game.getDetails().players().stream().noneMatch(PlayerDTO::requestedRematch));
    }

    private void gameHasPlayer(UUID userId) {
        assertNotNull(getPlayer(userId));
    }

    private void gamePlayerIsReady(UUID userId) {
        var player = getPlayer(userId);
        assertTrue(player != null && player.isReady());
    }

    private void gameHasNoPlayers() {
        assertTrue(game.getDetails().players().isEmpty());
    }

    private void gameHasNoPlayersReady() {
        assertTrue(game.getDetails().players().stream().noneMatch(PlayerDTO::isReady));
    }

    private GameStatusDTO gameStatus() {
        return game.getDetails().gameStatus();
    }

    private PlayerDTO getPlayer(UUID userId) {
        return game.getDetails().players().stream()
            .filter(p -> p.userId().equals(userId))
            .findAny()
            .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void eventIsEmitted(Class<T> eventClass, Predicate<T> predicate) {
        assertTrue(events.stream().anyMatch(e -> eventClass.isInstance(e) && predicate.test((T) e)));
    }

    private SymbolDTO[][] $$(SymbolDTO[]... rows) {
        return rows;
    }

    private SymbolDTO[] $(SymbolDTO... cols) {
        return cols;
    }

    private void boardIsPlayedWithMoves(
        String pattern,
        Pair<UUID, SymbolDTO> player1,
        Pair<UUID, SymbolDTO> player2
    ) {
        final var boardPattern = pattern.lines()
            .map(line -> line.split("\\s+"))
            .toList();

        final var player1Moves = new ArrayList<Pair<Integer, Integer>>();
        final var player2Moves = new ArrayList<Pair<Integer, Integer>>();

        for (int row = 0; row < boardPattern.size(); row++) {
            for (int col = 0; col < boardPattern.get(row).length; col++) {
                final var cell = boardPattern.get(row)[col];
                if(Objects.equals(cell, ".")) continue;
                if (player1.getRight().equals(SymbolDTO.valueOf(cell))) {
                    player1Moves.add(Pair.of(row, col));
                } else if (player2.getRight().equals(SymbolDTO.valueOf(cell))) {
                    player2Moves.add(Pair.of(row, col));
                }
            }
        }

        for (int i = 0; i < Math.max(player1Moves.size(), player2Moves.size()); i++) {
            if (i < player1Moves.size()) {
                final var move = player1Moves.get(i);
                game.handle(new PlayerMoveCommand(player1.getLeft(), move.getLeft(), move.getRight()));
            }
            if (i < player2Moves.size()) {
                final var move = player2Moves.get(i);
                game.handle(new PlayerMoveCommand(player2.getLeft(), move.getLeft(), move.getRight()));
            }
        }
    }
}
