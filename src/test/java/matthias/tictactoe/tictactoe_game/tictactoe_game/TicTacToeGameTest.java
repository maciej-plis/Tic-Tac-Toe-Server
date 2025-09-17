package matthias.tictactoe.tictactoe_game.tictactoe_game;

import matthias.tictactoe.shared.event.Event;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerMoveCommand;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.GameStatusDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.dto.SymbolDTO;
import matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerJoinedEvent;
import matthias.tictactoe.tictactoe_game.tictactoe_game.event.PlayerLeftEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

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

//    @DisplayName("User can switch from player to spectator")
//    @Test
//    void userCanSwitchFromPlayerToSpectator() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertTrue(gameHasNoPlayers(gameRoom1Id));
//        assertTrue(gameHasSpectator(gameRoom1Id, USER_1_ID));
//    }
//
//    @DisplayName("User can switch from spectator to player")
//    @Test
//    void userCanSwitchFromSpectatorToPlayer() {
//        // Given
//        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertTrue(gameHasNoSpectators(gameRoom1Id));
//        assertTrue(gameHasPlayer(gameRoom1Id, USER_1_ID));
//    }
//
//    @DisplayName("Game status should not change after first player joins")
//    @Test
//    void gameStatusShouldNotChangeAfterFirstPlayerJoins() {
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status changes to WAITING_FOR_PLAYERS_READY after second player joins")
//    @Test
//    void gameStatusChangesToWaitingForPlayersReadyAfterSecondPlayerJoins() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS_READY, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status changes to WAITING_FOR_PLAYERS after both players leave")
//    @Test
//    void gameStatusChangesToWaitingForPlayersAfterPlayerLeaves() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status changes to WAITING_FOR_PLAYERS after player switches to spectator")
//    @Test
//    void gameStatusChangesToWaitingForPlayersAfterPlayerSwitchToSpectator() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Player can be set ready")
//    @Test
//    void playerCanBeSetReady() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertTrue(gamePlayerIsReady(gameRoom1Id, USER_1_ID));
//    }
//
//    @DisplayName("Player cannot be set ready when game is waiting for players")
//    @Test
//    void playerCannotBeSetReadyWhenGameIsWaitingForPlayers() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // Expect
//        assertThrows(RuntimeException.class, () -> {
//            ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        });
//    }
//
//    @DisplayName("Player cannot be set ready when game is in progress")
//    @Test
//    void playerCannotBeSetReadyWhenGameIsInProgress() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // Expect
//        assertThrows(RuntimeException.class, () -> {
//            ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        });
//    }
//
//    @DisplayName("Player can be unset ready")
//    @Test
//    void playerCanBeUnsetReady() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerNotReadyCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertTrue(gameHasNoPlayersReady(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should not change after first player is ready")
//    @Test
//    void gameStatusShouldNotChangeAfterFirstPlayerIsReady() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS_READY, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to IN_PROGRESS after second player is ready")
//    @Test
//    void gameStatusShouldChangeToInProgressAfterSecondPlayerIsReady() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // Then
//        assertEquals(IN_PROGRESS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to FINISHED after game is won by user 1")
//    @Test
//    void gameStatusShouldChangeToFinishedAfterGameIsWonByUser1() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                O O X
//                O O X
//                X X -
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 2, 2));
//
//        // Then
//        assertEquals(FINISHED, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to FINISHED after game is won by user 2")
//    @Test
//    void gameStatusShouldChangeToFinishedAfterGameIsWonByUser2() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                - X O
//                X - O
//                - X -
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_2_ID, 2, 2));
//
//        // Then
//        assertEquals(FINISHED, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to FINISHED after game is drawn")
//    @Test
//    void gameStatusShouldChangeToFinishedAfterGameIsDrawn() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O X
//                X - O
//                O X O
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 1, 1));
//
//        // Then
//        assertEquals(FINISHED, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to WAITING_FOR_PLAYERS when game is in progress and player leaves")
//    @Test
//    void gameStatusShouldChangeToWaitingForPlayersWhenGameIsInProgressAndPlayerLeaves() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X - -
//                - - -
//                - - O
//                """
//        );
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to WAITING_FOR_PLAYERS when game is in progress and player leaves")
//    @Test
//    void gameStatusShouldChangeToWaitingForPlayersAfterGameIsFinishedAndPlayerLeaves() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O -
//                - X O
//                - - X
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_2_ID));
//
//        // Then
//        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Player can request rematch")
//    @Test
//    void playerCanRequestRematchWhenGameIsFinished() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O -
//                - X O
//                - - X
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertTrue(gamePlayerIsReady(gameRoom1Id, USER_1_ID));
//    }
//
//    @DisplayName("Player can cancel rematch request")
//    @Test
//    void playerCanCancelRematchRequest() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O -
//                - X O
//                - - X
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerCancelRematchCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertTrue(gameHasNoPlayersReady(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should not change when first player requests rematch")
//    @Test
//    void gameStatusShouldNotChangeWhenFirstPlayerRequestsRematch() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O -
//                - X O
//                - - X
//                """
//        );
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_1_ID));
//
//        // Then
//        assertEquals(FINISHED, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game status should change to IN_PROGRESS after second player requests rematch")
//    @Test
//    void gameStatusShouldChangeToInProgressAfterSecondPlayerRequestsRematch() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O -
//                - X O
//                - - X
//                """
//        );
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_1_ID));
//
//        // When
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_2_ID));
//
//        // Then
//        assertEquals(IN_PROGRESS, gameStatus(gameRoom1Id));
//    }
//
//    @DisplayName("Game is started by player who joined first")
//    @Test
//    void gameIsStartedByPlayerWhoJoinedFirst() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // Expect
//        assertThrows(RuntimeException.class, () -> {
//            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 0, 0));
//        });
//    }
//
//    @DisplayName("Player starting game changes with each rematch")
//    @Test
//    void playerStartingGameChangesWithEachRematch() {
//        // Given
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));
//
//        // Expect
//        assertThrows(RuntimeException.class, () -> {
//            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_2_ID, 0, 0));
//        });
//
//        // Then
//        boardIsPlayedWithMoves(
//            Pair.of(USER_1_ID, 'X'),
//            Pair.of(USER_2_ID, 'O'),
//            """
//                X O -
//                - X O
//                - - X
//                """
//        );
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_2_ID));
//
//        // Expect
//        assertThrows(RuntimeException.class, () -> {
//            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 0, 0));
//        });
//
//        // Then
//        boardIsPlayedWithMoves(
//            Pair.of(USER_2_ID, 'O'),
//            Pair.of(USER_1_ID, 'X'),
//            """
//                O - -
//                O - X
//                O - X
//                """
//        );
//
//        // And
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_1_ID));
//        ticTacToeGameService.resolveCommand(new PlayerRequestRematchCommand(gameRoom1Id, USER_2_ID));
//
//        // Expect
//        assertThrows(RuntimeException.class, () -> {
//            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_2_ID, 0, 0));
//        });
//    }

    private void gameHasPlayer(UUID userId) {
        assertNotNull(getPlayer(userId));
    }

    private boolean gamePlayerIsReady(UUID userId) {
        var player = getPlayer(userId);
        return player.isReady();
    }

    private void gameHasNoPlayers() {
        assertTrue(game.getDetails().players().isEmpty());
    }

    private boolean gameHasNoPlayersReady() {
        return game.getDetails().players().stream().noneMatch(PlayerDTO::isReady);
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
                game.handle(new PlayerMoveCommand(player1.getLeft(), move.getLeft(), move.getRight()));
            }
        }
    }
}
