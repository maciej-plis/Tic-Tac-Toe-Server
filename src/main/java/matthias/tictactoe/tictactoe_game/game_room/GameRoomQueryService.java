package matthias.tictactoe.tictactoe_game.game_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matthias.tictactoe.shared.AuthenticatedUserProvider;
import matthias.tictactoe.tictactoe_game.game_room.dto.BasicGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.dto.DetailedGameRoomInfoDTO;
import matthias.tictactoe.tictactoe_game.game_room.endpoint.dto.BasicGameRoomDto;
import matthias.tictactoe.tictactoe_game.game_room.endpoint.dto.GameRoomDto;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.Math.min;

@NullMarked
@Slf4j
@RequiredArgsConstructor
@Service
public class GameRoomQueryService {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final GameRoomManager gameRoomManager;

    public Page<BasicGameRoomInfoDTO> searchGameRooms(Pageable pageable, String gameRoomNameQuery) {
        final var gameRooms = gameRoomManager.getAll(gameRoom -> gameRoom.getName().contains(gameRoomNameQuery));
        return paginate(gameRooms.stream().map(GameRoom::getBasicGameRoomInfo).toList(), pageable);
    }

    public BasicGameRoomDto getGameRoomById(UUID gameRoomId) {
        final var user = authenticatedUserProvider.getAuthenticatedUser();
        final var gameRoom = gameRoomManager.getById(gameRoomId);
        return gameRoom.isPlayerOrSpectator(user.id()) ?
            gameRoom.getBasicGameRoomInfo() :
            gameRoom.getDetailedGameRoomInfo(user.id());
    }

    private <T> Page<T> paginate(List<T> content, Pageable pageable) {
        final var start = (int) pageable.getOffset();
        final var end = min(start + pageable.getPageSize(), content.size());
        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }
}
