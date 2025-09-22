package matthias.tictactoe.tictactoe_game.game_room;

import matthias.tictactoe.tictactoe_game.game_room.command.PlayerJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerLeaveCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.SpectatorJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.SpectatorLeaveCommand;
import matthias.tictactoe.tictactoe_game.game_room.dto.DetailedGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.SpectatorDTO;
import matthias.tictactoe.tictactoe_game.game_room.event.*;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerReadyCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class GameRoomTest {

    private static final UUID USER_1_ID = UUID.randomUUID();
    private static final UUID USER_2_ID = UUID.randomUUID();
    private static final UUID USER_3_ID = UUID.randomUUID();

    private List<Serializable> publishedMessages;
    private GameRoom gameRoom;

    @BeforeEach
    void init() {
        publishedMessages = new ArrayList<>();
        gameRoom = new GameRoom((roomId, message) -> publishedMessages.add(message), "Test Room");
    }

    @DisplayName("Spectator can join the room")
    @Test
    void spectatorCanJoinTheRoom() {
        // When
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // Then
        gameRoomHasSpectator(USER_1_ID);
    }

    @DisplayName("When spectator joins the room, the SpectatorJoinedEvent is published")
    @Test
    void whenSpectatorJoinsTheRoomEventIsPublished() {
        // When
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // Then
        messageIsPublished(SpectatorJoinedEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Spectator can leave the room")
    @Test
    void spectatorCanLeaveTheRoom() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new SpectatorLeaveCommand(USER_1_ID));

        // Then
        gameRoomHasNoSpectators();
    }

    @DisplayName("When spectator leaves the room, the SpectatorLeftEvent is published")
    @Test
    void whenSpectatorLeavesTheRoomEventIsPublished() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new SpectatorLeaveCommand(USER_1_ID));

        // Then
        messageIsPublished(SpectatorLeftEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Player can join the room")
    @Test
    void playerCanJoinTheRoom() {
        // When
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // Then
        gameRoomHasPlayer(USER_1_ID);
    }

    @DisplayName("When Player joins the room, the PlayerJoinedEvent is published")
    @Test
    void whenPlayerJoinsRoomPlayerJoinedEventIsPublished() {
        // When
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // Then
        assertEquals(1, publishedMessages.size());
        messageIsPublished(PlayerJoinedEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Player can leave the room")
    @Test
    void playerCanLeaveTheRoom() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new PlayerLeaveCommand(USER_1_ID));

        // Then
        gameRoomHasNoPlayers();
    }

    @DisplayName("When Player leaves the room, the PlayerLeftEvent is published")
    @Test
    void whenPlayerLeavesRoomPlayerLeftEventIsPublished() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new PlayerLeaveCommand(USER_1_ID));

        // Then
        assertEquals(2, publishedMessages.size());
        messageIsPublished(PlayerLeftEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Spectator can switch to player")
    @Test
    void spectatorCanSwitchToPlayer() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // Then
        gameRoomHasNoSpectators();
        gameRoomHasPlayer(USER_1_ID);
    }

    @DisplayName("When Spectator switches to Player event is published")
    @Test
    void whenSpectatorSwitchesToPlayerEventIsPublished() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // Then
        assertEquals(2, publishedMessages.size());
        messageIsPublished(SpectatorChangedToPlayerEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Player can switch to spectator")
    @Test
    void playerCanSwitchToSpectator() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // Then
        gameRoomHasNoPlayers();
        gameRoomHasSpectator(USER_1_ID);
    }

    @DisplayName("When Player switches to Spectator event is published")
    @Test
    void whenPlayerSwitchesToSpectatorEventIsPublished() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // Then
        assertEquals(2, publishedMessages.size());
        messageIsPublished(PlayerChangedToSpectatorEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Detailed room info is accessible for players")
    @Test
    void detailedRoomInfoAccessibleForPlayers() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // When
        final DetailedGameRoomInfoDTO details = gameRoom.getDetailedGameRoomInfo(USER_1_ID);

        // Then
        assertNotNull(details.gameDetails());
        assertEquals(gameRoom.getId(), details.gameRoomId());
        assertEquals("Test Room", details.gameRoomName());
        assertEquals(details.players().stream().map(PlayerDTO::userId).toList(), List.of(USER_1_ID));
        assertEquals(details.spectators(), emptyList());
    }

    @DisplayName("Detailed room info is accessible for spectators")
    @Test
    void detailedRoomInfoAccessibleForSpectators() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        final DetailedGameRoomInfoDTO details = gameRoom.getDetailedGameRoomInfo(USER_1_ID);

        // Then
        assertNotNull(details.gameDetails());
        assertEquals(gameRoom.getId(), details.gameRoomId());
        assertEquals("Test Room", details.gameRoomName());
        assertEquals(details.players(), emptyList());
        assertEquals(details.spectators().stream().map(SpectatorDTO::userId).toList(), List.of(USER_1_ID));
    }

    @DisplayName("Detailed room info is not accessible for outsiders")
    @Test
    void detailedRoomInfoNotAccessibleForOutsiders() {
        // Expect
        assertThrows(RuntimeException.class, () -> gameRoom.getDetailedGameRoomInfo(USER_3_ID));
    }

    @DisplayName("Only players can execute game commands")
    @Test
    void onlyPlayersCanExecuteGameCommands() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));
        gameRoom.handle(new PlayerJoinCommand(USER_2_ID));

        // When
        assertDoesNotThrow(() -> gameRoom.handle(new PlayerReadyCommand(USER_1_ID)));

        // Expect
        assertThrows(RuntimeException.class, () -> gameRoom.handle(new PlayerReadyCommand(USER_3_ID)));

        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_3_ID));

        // Expect
        assertThrows(RuntimeException.class, () -> gameRoom.handle(new PlayerReadyCommand(USER_3_ID)));
    }

    private void gameRoomHasSpectator(UUID userId) {
        assertNotNull(getSpectator(userId));
    }

    private void gameRoomHasNoSpectators() {
        assertTrue(gameRoom.getSpectators().isEmpty());
    }

    private SpectatorDTO getSpectator(UUID userId) {
        return gameRoom.getSpectators().stream()
            .filter(s -> s.userId().equals(userId))
            .findAny()
            .orElse(null);
    }

    private void gameRoomHasPlayer(UUID userId) {
        assertNotNull(getPlayer(userId));
    }

    private void gameRoomHasNoPlayers() {
        assertEquals(0, gameRoom.getBasicGameRoomInfo().playersCount());
    }

    private PlayerDTO getPlayer(UUID userId) {
        return gameRoom.getDetailedGameRoomInfo(userId)
            .players().stream()
            .filter(p -> p.userId().equals(userId))
            .findAny()
            .orElse(null);
    }

    private <T> void messageIsPublished(Class<T> eventClass, Predicate<T> predicate) {
        assertTrue(publishedMessages.stream().anyMatch(m -> eventClass.isInstance(m) && predicate.test(eventClass.cast(m))));
    }
}

