package matthias.tictactoe.tictactoe_game.game_room;

import matthias.tictactoe.tictactoe_game.game_room.command.*;
import matthias.tictactoe.tictactoe_game.game_room.dto.DetailedGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.PlayerDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.SpectatorDTO;
import matthias.tictactoe.tictactoe_game.game_room.event.*;
import matthias.tictactoe.tictactoe_game.game_room.exception.GameRoomAccessExeption;
import matthias.tictactoe.tictactoe_game.game_room.port.GameRoomMessagePublisher;
import matthias.tictactoe.tictactoe_game.tictactoe_game.command.PlayerReadyCommand;
import matthias.tictactoe.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;

class GameRoomTestMessagePublisher implements GameRoomMessagePublisher {

    List<Serializable> generalMessages = new ArrayList<>();
    Map<UUID, List<Serializable>> roomMessages = new HashMap<>();

    @Override
    public void publish(Serializable message) {
        generalMessages.add(message);
    }

    @Override
    public void publish(UUID gameRoomId, Serializable message) {
        roomMessages.putIfAbsent(gameRoomId, new ArrayList<>());
        roomMessages.get(gameRoomId).add(message);
    }

    public List<Serializable> getRoomMessages(UUID gameRoomId) {
        return roomMessages.getOrDefault(gameRoomId, emptyList());
    }
}

class GameRoomTest {

    private static final UserDto USER_1 = new UserDto(UUID.randomUUID(), "user-1", "user-1@mail.com", emptySet());
    private static final UserDto USER_2 = new UserDto(UUID.randomUUID(), "user-2", "user-2@mail.com", emptySet());
    private static final UserDto USER_3 = new UserDto(UUID.randomUUID(), "user-3", "user-3@mail.com", emptySet());

    private GameRoomTestMessagePublisher testMessagePublisher;
    private GameRoom gameRoom;

    @BeforeEach
    void init() {
        testMessagePublisher = new GameRoomTestMessagePublisher();
        gameRoom = new GameRoom(testMessagePublisher, "Test Room", USER_1.id(), true);
    }

    @DisplayName("Spectator can join the room")
    @Test
    void spectatorCanJoinTheRoom() {
        // When
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // Then
        gameRoomHasSpectator(USER_1.id());
    }

    @DisplayName("When spectator joins the room, the SpectatorJoinedEvent is published")
    @Test
    void whenSpectatorJoinsTheRoomEventIsPublished() {
        // When
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // Then
        gameRoomMessageIsPublished(SpectatorJoinedEvent.class, gameRoom.getId(), e -> USER_1.id().equals(e.userId()));
    }

    @DisplayName("Spectator can leave the room")
    @Test
    void spectatorCanLeaveTheRoom() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new SpectatorLeaveCommand(gameRoom.getId(), USER_1.id()));

        // Then
        gameRoomHasNoSpectators();
    }

    @DisplayName("When spectator leaves the room, the SpectatorLeftEvent is published")
    @Test
    void whenSpectatorLeavesTheRoomEventIsPublished() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new SpectatorLeaveCommand(gameRoom.getId(), USER_1.id()));

        // Then
        gameRoomMessageIsPublished(SpectatorLeftEvent.class, gameRoom.getId(), e -> USER_1.id().equals(e.userId()));
    }

    @DisplayName("Player can join the room")
    @Test
    void playerCanJoinTheRoom() {
        // When
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // Then
        gameRoomHasPlayer(USER_1.id());
    }

    @DisplayName("When Player joins the room, the PlayerJoinedEvent is published")
    @Test
    void whenPlayerJoinsRoomPlayerJoinedEventIsPublished() {
        // When
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // Then
        assertEquals(1, testMessagePublisher.getRoomMessages(gameRoom.getId()).size());
        gameRoomMessageIsPublished(PlayerJoinedEvent.class, gameRoom.getId(), e -> USER_1.id().equals(e.userId()));
    }

    @DisplayName("Player can leave the room")
    @Test
    void playerCanLeaveTheRoom() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new PlayerLeaveCommand(gameRoom.getId(), USER_1.id()));

        // Then
        gameRoomHasNoPlayers();
    }

    @DisplayName("When Player leaves the room, the PlayerLeftEvent is published")
    @Test
    void whenPlayerLeavesRoomPlayerLeftEventIsPublished() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new PlayerLeaveCommand(gameRoom.getId(), USER_1.id()));

        // Then
        assertEquals(2, testMessagePublisher.getRoomMessages(gameRoom.getId()).size());
        gameRoomMessageIsPublished(PlayerLeftEvent.class, gameRoom.getId(), e -> USER_1.id().equals(e.userId()));
    }

    @DisplayName("Spectator can switch to player")
    @Test
    void spectatorCanSwitchToPlayer() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // Then
        gameRoomHasNoSpectators();
        gameRoomHasPlayer(USER_1.id());
    }

    @DisplayName("When Spectator switches to Player event is published")
    @Test
    void whenSpectatorSwitchesToPlayerEventIsPublished() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // Then
        assertEquals(2, testMessagePublisher.getRoomMessages(gameRoom.getId()).size());
        gameRoomMessageIsPublished(SpectatorChangedToPlayerEvent.class, gameRoom.getId(), e -> USER_1.id().equals(e.userId()));
    }

    @DisplayName("Player can switch to spectator")
    @Test
    void playerCanSwitchToSpectator() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // Then
        gameRoomHasNoPlayers();
        gameRoomHasSpectator(USER_1.id());
    }

    @DisplayName("When Player switches to Spectator event is published")
    @Test
    void whenPlayerSwitchesToSpectatorEventIsPublished() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // When
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // Then
        assertEquals(2, testMessagePublisher.getRoomMessages(gameRoom.getId()).size());
        gameRoomMessageIsPublished(PlayerChangedToSpectatorEvent.class, gameRoom.getId(), e -> USER_1.id().equals(e.userId()));
    }

    @DisplayName("Detailed room info is accessible for players")
    @Test
    void detailedRoomInfoAccessibleForPlayers() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));

        // When
        final DetailedGameRoomInfoDTO details = gameRoom.getDetailedGameRoomInfo(USER_1.id());

        // Then
        assertNotNull(details.gameDetails());
        assertEquals(gameRoom.getId(), details.gameRoomId());
        assertEquals("Test Room", details.gameRoomName());
        assertEquals(details.players().stream().map(PlayerDTO::userId).toList(), List.of(USER_1.id()));
        assertEquals(details.spectators(), emptyList());
    }

    @DisplayName("Detailed room info is accessible for spectators")
    @Test
    void detailedRoomInfoAccessibleForSpectators() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_1));

        // When
        final DetailedGameRoomInfoDTO details = gameRoom.getDetailedGameRoomInfo(USER_1.id());

        // Then
        assertNotNull(details.gameDetails());
        assertEquals(gameRoom.getId(), details.gameRoomId());
        assertEquals("Test Room", details.gameRoomName());
        assertEquals(details.players(), emptyList());
        assertEquals(details.spectators().stream().map(SpectatorDTO::userId).toList(), List.of(USER_1.id()));
    }

    @DisplayName("Detailed room info is not accessible for outsiders")
    @Test
    void detailedRoomInfoNotAccessibleForOutsiders() {
        // Expect
        assertThrows(RuntimeException.class, () -> gameRoom.getDetailedGameRoomInfo(USER_3.id()));
    }

    @DisplayName("Only players can execute game commands")
    @Test
    void onlyPlayersCanExecuteGameCommands() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_1));
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2));

        // When
        assertDoesNotThrow(() -> gameRoom.handle(new PlayerReadyCommand(USER_1.id())));

        // Expect
        assertThrows(RuntimeException.class, () -> gameRoom.handle(new PlayerReadyCommand(USER_3.id())));

        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_3));

        // Expect
        assertThrows(RuntimeException.class, () -> gameRoom.handle(new PlayerReadyCommand(USER_3.id())));
    }

    @DisplayName("Owner can ban a player")
    @Test
    void ownerCanBanPlayer() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2));

        // When
        gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2)));
        gameRoomMessageIsPublished(UserBannedEvent.class, gameRoom.getId(), e -> USER_2.id().equals(e.userId()));
    }

    @DisplayName("Owner can ban a spectator")
    @Test
    void ownerCanBanSpectator() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_2));

        // When
        gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_2)));
        gameRoomMessageIsPublished(UserBannedEvent.class, gameRoom.getId(), e -> USER_2.id().equals(e.userId()));
    }

    @DisplayName("Owner can kick a player")
    @Test
    void ownerCanKickPlayer() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2));

        // When
        gameRoom.handle(new KickUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        gameRoomHasNoPlayers();
        gameRoomMessageIsPublished(UserKickedEvent.class, gameRoom.getId(), e -> USER_2.id().equals(e.userId()));
    }

    @DisplayName("Owner can kick a spectator")
    @Test
    void ownerCanKickSpectator() {
        // Given
        gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_2));

        // When
        gameRoom.handle(new KickUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        gameRoomHasNoSpectators();
        gameRoomMessageIsPublished(UserKickedEvent.class, gameRoom.getId(), e -> USER_2.id().equals(e.userId()));
    }

    @DisplayName("Non-owner cannot ban or kick users")
    @Test
    void nonOwnerCannotBanOrKickUsers() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2));

        // Expect
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_2.id(), USER_1.id())));
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new KickUserCommand(gameRoom.getId(), USER_2.id(), USER_1.id())));
    }

    @DisplayName("Spectator cannot join when disabled")
    @Test
    void spectatorCannotJoinWhenDisabled() {
        // Given
        gameRoom = new GameRoom(testMessagePublisher, "No Spectators Room", USER_1.id(), false);

        // Expect
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_2)));
    }

    @DisplayName("Owner can enable/disable spectators via update")
    @Test
    void ownerCanUpdateSpectatorOption() {
        //Given
        gameRoom = new GameRoom(testMessagePublisher, "Room", USER_1.id(), true);

        // When
        gameRoom.handle(new UpdateGameRoomCommand(gameRoom.getId(), USER_1.id(), null, false));

        // Then
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_2)));

        // When
        gameRoom.handle(new UpdateGameRoomCommand(gameRoom.getId(), USER_1.id(), null, true));

        // Then
        assertDoesNotThrow(() -> gameRoom.handle(new SpectatorJoinCommand(gameRoom.getId(), USER_2)));
        gameRoomHasSpectator(USER_2.id());
    }

    @DisplayName("Non-owner cannot update spectator option")
    @Test
    void nonOwnerCannotUpdateSpectatorOption() {
        // Given
        gameRoom = new GameRoom(testMessagePublisher, "Room", USER_1.id(), true);

        // Expect
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new UpdateGameRoomCommand(gameRoom.getId(), USER_2.id(), null, false)));
    }

    @DisplayName("Owner can unban a user and user can rejoin")
    @Test
    void ownerCanUnbanUserAndUserCanRejoin() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2));
        gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // When
        gameRoom.handle(new UnbanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        assertDoesNotThrow(() -> gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2)));
        gameRoomMessageIsPublished(UserUnbannedEvent.class, gameRoom.getId(), e -> e.userId().equals(USER_2.id()));
    }

    @DisplayName("Non-owner cannot unban a user")
    @Test
    void nonOwnerCannotUnbanUser() {
        // Given
        gameRoom.handle(new PlayerJoinCommand(gameRoom.getId(), USER_2));
        gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Expect
        assertThrows(GameRoomAccessExeption.class, () -> gameRoom.handle(new UnbanUserCommand(gameRoom.getId(), USER_2.id(), USER_1.id())));
    }

    @DisplayName("Ban does not publish event if user is already banned")
    @Test
    void banDoesNotPublishEventIfUserAlreadyBanned() {
        // Given
        gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // When
        gameRoom.handle(new BanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        final var userBanEvents = testMessagePublisher.getRoomMessages(gameRoom.getId()).stream()
            .filter(m -> m instanceof UserBannedEvent)
            .count();

        assertEquals(1, userBanEvents);
    }

    @DisplayName("Kick does not publish event if user is not present")
    @Test
    void kickDoesNotPublishEventIfUserNotPresent() {
        // When
        gameRoom.handle(new KickUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        final var userKickEvents = testMessagePublisher.getRoomMessages(gameRoom.getId()).stream()
            .filter(m -> m instanceof UserKickedEvent)
            .count();

        assertEquals(0, userKickEvents);
    }

    @DisplayName("Unban does not publish event if user is not banned")
    @Test
    void unbanDoesNotPublishEventIfUserNotBanned() {
        // Given
        gameRoom.handle(new UnbanUserCommand(gameRoom.getId(), USER_1.id(), USER_2.id()));

        // Then
        final var userUnbanEvents = testMessagePublisher.getRoomMessages(gameRoom.getId()).stream()
            .filter(m -> m instanceof UserUnbannedEvent)
            .count();

        assertEquals(0, userUnbanEvents);
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

    private <T> void gameRoomMessageIsPublished(Class<T> eventClass, UUID gameRoomId, Predicate<T> predicate) {
        assertTrue(testMessagePublisher.roomMessages.getOrDefault(gameRoomId, emptyList()).stream()
            .anyMatch(m -> eventClass.isInstance(m) && predicate.test(eventClass.cast(m))));
    }

    private <T> void generalMessageIsPublished(Class<T> eventClass, Predicate<T> predicate) {
        assertTrue(testMessagePublisher.generalMessages.stream()
            .anyMatch(m -> eventClass.isInstance(m) && predicate.test(eventClass.cast(m))));
    }
}
