package matthias.tictactoe.authentication;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.authentication.dto.LoginUserRequest;
import matthias.tictactoe.authentication.dto.RegisterUserRequest;
import matthias.tictactoe.jwt.dto.TokenDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "${client.url}")
@RequiredArgsConstructor
class AuthenticationEndpoint {

    private final UserAuthenticationService userAuthenticationService;
    private final GuestAuthenticationService guestAuthenticationService;

    @PostMapping("/register")
    ResponseEntity<Object> registerUser(RegisterUserRequest request) {
        userAuthenticationService.registerUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    ResponseEntity<TokenDto> loginUser(LoginUserRequest request) {
        TokenDto token = userAuthenticationService.loginUser(request);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/login-guest")
    ResponseEntity<TokenDto> loginGuest() {
        TokenDto token = guestAuthenticationService.registerGuest();
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/")
    boolean isOnline() {
        return true;
    }
}
