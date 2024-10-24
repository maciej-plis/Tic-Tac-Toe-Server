package matthias.tictactoe.authentication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {
    private String username;
    private String email;
    private String password;
}
