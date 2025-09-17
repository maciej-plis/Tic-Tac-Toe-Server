package matthias.tictactoe.user;

import lombok.Builder;

@Builder
record GuestUser(
    String name
) {
}
