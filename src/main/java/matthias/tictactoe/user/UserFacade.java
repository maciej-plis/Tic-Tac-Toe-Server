package matthias.tictactoe.user;

import lombok.RequiredArgsConstructor;
import matthias.tictactoe.user.dto.CreateUserRequest;
import matthias.tictactoe.user.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.Optional;

@Validated
@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public Optional<UserDto> findUserById(long id) {
        final var user = userService.findUserById(id);
        return user.map(userMapper::toDto);
    }

    public Optional<UserDto> findUserByUsername(String username) {
        final var user = userService.findUserByUsername(username);
        return user.map(userMapper::toDto);
    }

    public boolean isUsernameAvailable(String username) {
        return userService.isUsernameAvailable(username);
    }

    public Optional<UserDto> findUserByEmail(String email) {
        final var user = userService.findUserByEmail(email);
        return user.map(userMapper::toDto);
    }

    public boolean isEmailAvailable(String email) {
        return userService.isEmailAvailable(email);
    }

    public long createGuestUser() {
        return userService.createGuestUser();
    }

    public long createUser(@Valid CreateUserRequest request) {
        return userService.createUser(request);
    }
}
