package matthias.tictactoe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long id;
    private String username;
    private String email;
    private Set<Role> roles;

    public enum Role {
        GUEST,
        USER,
        ADMIN
    }
}
