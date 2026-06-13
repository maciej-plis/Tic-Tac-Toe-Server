package matthias.tictactoe.user.dto;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserDto(
    UUID id,
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
