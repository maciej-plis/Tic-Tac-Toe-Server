package matthias.tictactoe.tictactoe_game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
class GameId {
    private final UUID id;
}
