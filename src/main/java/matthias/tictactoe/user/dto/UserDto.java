package matthias.tictactoe.user.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserDto(
    long id,
    String username,
    String email,
    Set<Role> roles
) {

    public enum Role {
        GUEST,
        USER,
        ADMIN
    }
}
