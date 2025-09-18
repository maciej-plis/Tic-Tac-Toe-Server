package matthias.tictactoe.tictactoe_game.game_room;

import matthias.tictactoe.tictactoe_game.game_room.command.PlayerJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.PlayerLeaveCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.SpectatorJoinCommand;
import matthias.tictactoe.tictactoe_game.game_room.command.SpectatorLeaveCommand;
import matthias.tictactoe.tictactoe_game.game_room.dto.DetailedGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.SpectatorDTO;
import matthias.tictactoe.tictactoe_game.game_room.event.PlayerChangedToSpectatorEvent;
import matthias.tictactoe.tictactoe_game.game_room.event.PlayerJoinedEvent;
import matthias.tictactoe.tictactoe_game.game_room.event.PlayerLeftEvent;
import matthias.tictactoe.tictactoe_game.game_room.event.SpectatorChangedToPlayerEvent;
import matthias.tictactoe.tictactoe_game.game_room.event.SpectatorJoinedEvent;
import matthias.tictactoe.tictactoe_game.game_room.event.SpectatorLeftEvent;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerReadyCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

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
        final GameRoomMessagePublisher publisher = (roomId, message) -> publishedMessages.add(message);
        gameRoom = new GameRoom(publisher, "Test Room");
    }

    @DisplayName("Spectator can join the room")
    @Test
    void spectatorCanJoinTheRoom() {
        // When
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // Then
        assertTrue(gameRoom.getSpectators().stream().anyMatch(s -> s.userId().equals(USER_1_ID)));
        messageIsPublished(SpectatorJoinedEvent.class, e -> USER_1_ID.equals(e.userId()));
        assertEquals(0, gameRoom.getBasicGameRoomInfo().playersCount());
        assertEquals(1, gameRoom.getBasicGameRoomInfo().spectatorsCount());
    }

    @DisplayName("Spectator can leave the room")
    @Test
    void spectatorCanLeaveTheRoom() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new SpectatorLeaveCommand(USER_1_ID));

        // Then
        assertTrue(gameRoom.getSpectators().isEmpty());
        messageIsPublished(SpectatorLeftEvent.class, e -> USER_1_ID.equals(e.userId()));
    }

    @DisplayName("Player can join the room")
    @Test
    void playerCanJoinTheRoom() {
        // When
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // Then
        assertEquals(1, gameRoom.getBasicGameRoomInfo().playersCount());
        assertEquals(0, gameRoom.getBasicGameRoomInfo().spectatorsCount());
        messageIsPublished(PlayerJoinedEvent.class, e -> USER_1_ID.equals(e.userId()));
        assertNotNull(gameRoom.getDetailedGameRoomInfo(USER_1_ID));
    }

    @DisplayName("Player can leave the room")
    @Test
    void playerCanLeaveTheRoom() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(USER_1_ID));

        // When
        gameRoom.handle(new PlayerLeaveCommand(USER_1_ID));

        // Then
        assertEquals(0, gameRoom.getBasicGameRoomInfo().playersCount());
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
        assertTrue(gameRoom.getSpectators().stream().noneMatch(s -> s.userId().equals(USER_1_ID)));
        assertEquals(1, gameRoom.getBasicGameRoomInfo().playersCount());
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
        assertEquals(0, gameRoom.getBasicGameRoomInfo().playersCount());
        assertTrue(gameRoom.getSpectators().stream().anyMatch(s -> s.userId().equals(USER_1_ID)));
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
        assertEquals(gameRoom.getId(), details.gameRoomId());
        assertEquals("Test Room", details.gameRoomName());
        assertNotNull(details.gameDetails());
        assertTrue(details.players().stream().anyMatch(p -> p.id().equals(USER_1_ID)));
        assertTrue(details.spectators().isEmpty());
    }

    @DisplayName("Detailed room info is accessible for spectators")
    @Test
    void detailedRoomInfoAccessibleForSpectators() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(USER_1_ID));

        // When
        final DetailedGameRoomInfoDTO details = gameRoom.getDetailedGameRoomInfo(USER_1_ID);

        // Then
        assertEquals(gameRoom.getId(), details.gameRoomId());
        assertEquals("Test Room", details.gameRoomName());
        assertNotNull(details.gameDetails());
        assertTrue(details.players().isEmpty());
        assertTrue(details.spectators().stream().map(SpectatorDTO::userId).anyMatch(USER_1_ID::equals));
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

    private <T> void messageIsPublished(Class<T> eventClass, Predicate<T> predicate) {
        assertTrue(publishedMessages.stream().anyMatch(m -> eventClass.isInstance(m) && predicate.test(eventClass.cast(m))));
    }
}

