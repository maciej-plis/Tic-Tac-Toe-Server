package matthias.tictactoe.tictactoe_game;

import matthias.tictactoe.tictactoe_game.command.*;
import matthias.tictactoe.tictactoe_game.dto.GameRoomDTO;
import matthias.tictactoe.tictactoe_game.dto.GameStatusDTO;
import matthias.tictactoe.tictactoe_game.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.dto.SpectatorDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static matthias.tictactoe.tictactoe_game.dto.GameStatusDTO.*;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private final static UUID USER_1_ID = UUID.randomUUID();
    private final static UUID USER_2_ID = UUID.randomUUID();
    private final static UUID USER_3_ID = UUID.randomUUID();

    private UUID gameRoom1Id;
    private UUID gameRoom2Id;
    private UUID gameRoom3Id;

    private TicTacToeGameService ticTacToeGameService;

    @BeforeEach
    void init() {
        ticTacToeGameService = new TicTacToeGameService();
        ticTacToeGameService.resolveCommand(new CreateGameRoomCommand("Room 1", id -> gameRoom1Id = id));
        ticTacToeGameService.resolveCommand(new CreateGameRoomCommand("Room 2", id -> gameRoom2Id = id));
        ticTacToeGameService.resolveCommand(new CreateGameRoomCommand("Room 3", id -> gameRoom3Id = id));
    }

    @DisplayName("User can join game as player")
    @Test
    void userCanJoinGameAsPlayer() {
        // When
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasPlayer(gameRoom1Id, USER_1_ID));
    }

    @DisplayName("User can join game as spectator")
    @Test
    void userCanJoinGameAsSpectator() {
        // When
        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasSpectator(gameRoom1Id, USER_1_ID));
    }

    @DisplayName("player can leave game")
    @Test
    void playerCanLeaveGame() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasNoPlayers(gameRoom1Id));
    }

    @DisplayName("spectator can leave game")
    @Test
    void spectatorCanLeaveGame() {
        // Given
        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new SpectatorLeaveCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasNoSpectators(gameRoom1Id));
    }

    @DisplayName("User cannot join game as player when it is full")
    @Test
    void userCannotJoinGameAsPlayerWhenItIsFull() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_3_ID));
        });
    }

    @DisplayName("User can switch from player to spectator")
    @Test
    void userCanSwitchFromPlayerToSpectator() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasNoPlayers(gameRoom1Id));
        assertTrue(gameHasSpectator(gameRoom1Id, USER_1_ID));
    }

    @DisplayName("User can switch from spectator to player")
    @Test
    void userCanSwitchFromSpectatorToPlayer() {
        // Given
        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasNoSpectators(gameRoom1Id));
        assertTrue(gameHasPlayer(gameRoom1Id, USER_1_ID));
    }

    @DisplayName("Game status should not change after first player joins")
    @Test
    void gameStatusShouldNotChangeAfterFirstPlayerJoins() {
        // When
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status changes to WAITING_FOR_PLAYERS_READY after second player joins")
    @Test
    void gameStatusChangesToWaitingForPlayersReadyAfterSecondPlayerJoins() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS_READY, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status changes to WAITING_FOR_PLAYERS after both players leave")
    @Test
    void gameStatusChangesToWaitingForPlayersAfterPlayerLeaves() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status changes to WAITING_FOR_PLAYERS after player switches to spectator")
    @Test
    void gameStatusChangesToWaitingForPlayersAfterPlayerSwitchToSpectator() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // When
        ticTacToeGameService.resolveCommand(new SpectatorJoinCommand(gameRoom1Id, USER_2_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Player can be set ready")
    @Test
    void playerCanBeSetReady() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gamePlayerIsReady(gameRoom1Id, USER_1_ID));
    }

    @DisplayName("Player cannot be set ready when game is waiting for players")
    @Test
    void playerCannotBeSetReadyWhenGameIsWaitingForPlayers() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        });
    }

    @DisplayName("Player cannot be set ready when game is in progress")
    @Test
    void playerCannotBeSetReadyWhenGameIsInProgress() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        });
    }

    @DisplayName("Player can be unset ready")
    @Test
    void playerCanBeUnsetReady() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerNotReadyCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasNoPlayersReady(gameRoom1Id));
    }

    @DisplayName("Game status should not change after first player is ready")
    @Test
    void gameStatusShouldNotChangeAfterFirstPlayerIsReady() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS_READY, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to IN_PROGRESS after second player is ready")
    @Test
    void gameStatusShouldChangeToInProgressAfterSecondPlayerIsReady() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // Then
        assertEquals(IN_PROGRESS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to FINISHED after game is won by user 1")
    @Test
    void gameStatusShouldChangeToFinishedAfterGameIsWonByUser1() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                O O X
                O O X
                X X -
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 2, 2));

        // Then
        assertEquals(FINISHED, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to FINISHED after game is won by user 2")
    @Test
    void gameStatusShouldChangeToFinishedAfterGameIsWonByUser2() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                - X O
                X - O
                - X -
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_2_ID, 2, 2));

        // Then
        assertEquals(FINISHED, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to FINISHED after game is drawn")
    @Test
    void gameStatusShouldChangeToFinishedAfterGameIsDrawn() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O X
                X - O
                O X O
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 1, 1));

        // Then
        assertEquals(FINISHED, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to WAITING_FOR_PLAYERS when game is in progress and player leaves")
    @Test
    void gameStatusShouldChangeToWaitingForPlayersWhenGameIsInProgressAndPlayerLeaves() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X - -
                - - -
                - - O
                """
        );
        // When
        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to WAITING_FOR_PLAYERS when game is in progress and player leaves")
    @Test
    void gameStatusShouldChangeToWaitingForPlayersAfterGameIsFinishedAndPlayerLeaves() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O -
                - X O
                - - X
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_2_ID));

        // Then
        assertEquals(WAITING_FOR_PLAYERS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Player can request rematch")
    @Test
    void playerCanRequestRematchWhenGameIsFinished() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O -
                - X O
                - - X
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gamePlayerIsReady(gameRoom1Id, USER_1_ID));
    }

    @DisplayName("Player can cancel rematch request")
    @Test
    void playerCanCancelRematchRequest() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O -
                - X O
                - - X
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerCancelRematchCommand(gameRoom1Id, USER_1_ID));

        // Then
        assertTrue(gameHasNoPlayersReady(gameRoom1Id));
    }

    @DisplayName("Game status should not change when first player requests rematch")
    @Test
    void gameStatusShouldNotChangeWhenFirstPlayerRequestsRematch() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O -
                - X O
                - - X
                """
        );

        // When
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_1_ID));

        // Then
        assertEquals(FINISHED, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game status should change to IN_PROGRESS after second player requests rematch")
    @Test
    void gameStatusShouldChangeToInProgressAfterSecondPlayerRequestsRematch() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // And
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O -
                - X O
                - - X
                """
        );

        // And
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_1_ID));

        // When
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_2_ID));

        // Then
        assertEquals(IN_PROGRESS, gameStatus(gameRoom1Id));
    }

    @DisplayName("Game is started by player who joined first")
    @Test
    void gameIsStartedByPlayerWhoJoinedFirst() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerLeaveCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 0, 0));
        });
    }

    @DisplayName("Player starting game changes with each rematch")
    @Test
    void playerStartingGameChangesWithEachRematch() {
        // Given
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerJoinCommand(gameRoom1Id, USER_2_ID));

        // And
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerReadyCommand(gameRoom1Id, USER_2_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_2_ID, 0, 0));
        });

        // Then
        boardIsPlayedWithMoves(
            Pair.of(USER_1_ID, 'X'),
            Pair.of(USER_2_ID, 'O'),
            """
                X O -
                - X O
                - - X
                """
        );

        // And
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_2_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_1_ID, 0, 0));
        });

        // Then
        boardIsPlayedWithMoves(
            Pair.of(USER_2_ID, 'O'),
            Pair.of(USER_1_ID, 'X'),
            """
                O - -
                O - X
                O - X
                """
        );

        // And
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_1_ID));
        ticTacToeGameService.resolveCommand(new PlayerRequestRematch(gameRoom1Id, USER_2_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> {
            ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, USER_2_ID, 0, 0));
        });
    }

    private boolean gameHasPlayer(UUID gameId, UUID userId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        var player = getPlayer(gameInfo, userId);
        return player != null;
    }

    private boolean gamePlayerIsReady(UUID gameId, UUID userId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        var player = getPlayer(gameInfo, userId);
        return player.isReady();
    }

    private boolean gameHasSpectator(UUID gameId, UUID userId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        return gameInfo.spectators().stream().anyMatch(s -> s.id().equals(userId));
    }

    private boolean gameHasNoPlayers(UUID gameId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        return gameInfo.players().isEmpty();
    }

    private boolean gameHasNoSpectators(UUID gameId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        return gameInfo.spectators().isEmpty();
    }

    private boolean gameHasNoPlayersReady(UUID gameId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        return gameInfo.players().stream().noneMatch(PlayerDTO::isReady);
    }

    private GameStatusDTO gameStatus(UUID gameId) {
        var gameInfo = ticTacToeGameService.getGameRoom(gameId);
        return gameInfo.gameStatus();
    }

    private PlayerDTO getPlayer(GameRoomDTO gameRoomDTO, UUID userId) {
        return gameRoomDTO.players().stream()
            .filter(p -> p.id().equals(userId))
            .findFirst()
            .orElse(null);
    }

    private SpectatorDTO getSpectator(GameRoomDTO gameRoomDTO, UUID userId) {
        return gameRoomDTO.spectators().stream()
            .filter(s -> s.id().equals(userId))
            .findFirst()
            .orElse(null);
    }

    private UUID[][] $$(UUID[]... rows) {
        return rows;
    }

    private UUID[] $(UUID... cols) {
        return cols;
    }

    private void boardIsPlayedWithMoves(
        Pair<UUID, Character> player1,
        Pair<UUID, Character> player2,
        String pattern
    ) {
        final var boardPattern = pattern.lines()
            .map(line -> line.split("\\s+"))
            .toList();

        final var player1Moves = new ArrayList<Pair<Integer, Integer>>();
        final var player2Moves = new ArrayList<Pair<Integer, Integer>>();

        for (int row = 0; row < boardPattern.size(); row++) {
            for (int col = 0; col < boardPattern.get(row).length; col++) {
                final var cell = boardPattern.get(row)[col];
                if (cell.equals(String.valueOf(player1.getRight()))) {
                    player1Moves.add(Pair.of(row, col));
                } else if (cell.equals(String.valueOf(player2.getRight()))) {
                    player2Moves.add(Pair.of(row, col));
                }
            }
        }

        for (int i = 0; i < Math.max(player1Moves.size(), player2Moves.size()); i++) {
            if (i < player1Moves.size()) {
                final var move = player1Moves.get(i);
                ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, player1.getLeft(), move.getLeft(), move.getRight()));
            }
            if (i < player2Moves.size()) {
                final var move = player2Moves.get(i);
                ticTacToeGameService.resolveCommand(new PlayerMoveCommand(gameRoom1Id, player2.getLeft(), move.getLeft(), move.getRight()));
            }
        }
    }
}
