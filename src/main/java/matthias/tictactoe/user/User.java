package matthias.tictactoe.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
class User {

    private final String username;
    private final String email;
    private final String password;
    private final Set<Role> roles;

    enum Role {
        GUEST,
        USER,
        ADMIN
    }
}
