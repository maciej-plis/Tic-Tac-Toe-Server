package matthias.tictactoe.authentication;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.authentication.dto.LoginUserRequest;
import matthias.tictactoe.authentication.dto.RegisterUserRequest;
import matthias.tictactoe.jwt.JwtService;
import matthias.tictactoe.jwt.dto.TokenDto;
import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserAuthenticationService {

    private final AuthenticationMapper authenticationMapper;
    private final UserFacade userFacade;
    private final JwtService jwtService;

    public void registerUser(RegisterUserRequest request) {
        userFacade.createUser(authenticationMapper.toCreateUserRequest(request));
    }

    public TokenDto loginUser(LoginUserRequest request) {
        return userFacade.findUserByUsername(request.getUsername())
            .map(UserDto::username)
            .map(jwtService::generateJWT)
            .orElseThrow(AuthenticationFailedException::new);
    }
}
