package matthias.tictactoe.authentication;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.jwt.JwtService;
import matthias.tictactoe.jwt.dto.TokenDto;
import matthias.tictactoe.user.UserFacade;
import matthias.tictactoe.user.dto.UserDto;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class GuestAuthenticationService {

    private final UserFacade userFacade;
    private final JwtService jwtService;

    public TokenDto registerGuest() {
        final var userId = userFacade.createGuestUser();
        return userFacade.findUserById(userId)
            .map(UserDto::username)
            .map(jwtService::generateJWT)
            .orElseThrow(IllegalStateException::new);
    }
}
