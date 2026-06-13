package matthias.tictactoe.authentication;

import matthias.tictactoe.authentication.request.RegisterUserRequest;
import matthias.tictactoe.user.dto.CreateUserRequest;
import org.springframework.stereotype.Component;

@Component
class AuthenticationMapper {

    public CreateUserRequest toCreateUserRequest(RegisterUserRequest request) {
        return CreateUserRequest.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(request.getPassword())
            .build();
    }
}
