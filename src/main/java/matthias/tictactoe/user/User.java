package matthias.tictactoe.user;

import lombok.Builder;

import java.util.Set;

@Builder
record User(
    String username,
    String email,
    String password,
    Set<Role> roles
) {

    enum Role {
        GUEST,
        USER,
        ADMIN
    }
}
